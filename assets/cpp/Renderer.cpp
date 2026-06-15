#include <GLFW/glfw3.h>
#include <GL/glu.h>

#include <algorithm>
#include <cctype>
#include <cstdint>
#include <cstring>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <nlohmann/json.hpp>
#include <sstream>
#include <stdexcept>
#include <string>
#include <vector>

struct Vertex {
    float x;
    float y;
    float z;
};

struct Triangle {
    Vertex a;
    Vertex b;
    Vertex c;
};

struct ModelData {
    std::vector<Triangle> triangles;
    Vertex center;
    float scale;
};

struct Vec3 {
    float x;
    float y;
    float z;
};

struct SceneObject {
    std::string name;
    std::string source;
    Vec3 position;
    Vec3 rotation;
    Vec3 scale;
};

static std::string trim(const std::string& input) {
    size_t start = 0;
    while (start < input.size() && std::isspace(static_cast<unsigned char>(input[start])) != 0) {
        start++;
    }
    size_t end = input.size();
    while (end > start && std::isspace(static_cast<unsigned char>(input[end - 1])) != 0) {
        end--;
    }
    return input.substr(start, end - start);
}

static std::string parseQuoted(const std::string& value) {
    const std::string t = trim(value);
    if (t.size() < 2 || t.front() != '"' || t.back() != '"') {
        throw std::runtime_error("Expected quoted string but got: " + value);
    }
    return t.substr(1, t.size() - 2);
}

static Vec3 parseVec3(const std::string& value) {
    std::string t = trim(value);
    if (t.size() < 5 || t.front() != '(' || t.back() != ')') {
        throw std::runtime_error("Expected vector tuple (x,y,z), got: " + value);
    }
    t = t.substr(1, t.size() - 2);

    std::stringstream ss(t);
    std::string xStr, yStr, zStr;
    if (!std::getline(ss, xStr, ',') || !std::getline(ss, yStr, ',') || !std::getline(ss, zStr, ',')) {
        throw std::runtime_error("Expected three vector components in: " + value);
    }
    return {std::stof(trim(xStr)), std::stof(trim(yStr)), std::stof(trim(zStr))};
}

static SceneObject loadSceneObjectFromToml(const std::string& path) {
    std::ifstream in(path);
    if (!in) {
        throw std::runtime_error("Cannot open scene descriptor: " + path);
    }

    SceneObject object{};
    object.name = "Object";
    object.rotation = {0.0f, 0.0f, 0.0f};
    object.position = {0.0f, 0.0f, 0.0f};
    object.scale = {1.0f, 1.0f, 1.0f};

    enum class Section { Root, Object };
    Section section = Section::Root;

    std::string line;
    int lineNo = 0;
    while (std::getline(in, line)) {
        lineNo++;
        const size_t commentPos = line.find('#');
        if (commentPos != std::string::npos) {
            line = line.substr(0, commentPos);
        }
        line = trim(line);
        if (line.empty()) continue;

        if (line.front() == '[' && line.back() == ']') {
            const std::string header = line.substr(1, line.size() - 2);
            if (header == "root") {
                section = Section::Root;
            } else if (header.rfind("root.", 0) == 0) {
                section = Section::Object;
                object.name = header.substr(5);
            } else {
                throw std::runtime_error("Unsupported section [" + header + "] in line " + std::to_string(lineNo));
            }
            continue;
        }

        const size_t eqPos = line.find('=');
        if (eqPos == std::string::npos) {
            throw std::runtime_error("Invalid key/value line " + std::to_string(lineNo));
        }

        const std::string key = trim(line.substr(0, eqPos));
        const std::string value = trim(line.substr(eqPos + 1));

        if (section == Section::Root) {
            if (key == "rotation") object.rotation = parseVec3(value);
            else if (key == "position") object.position = parseVec3(value);
            else if (key == "scale") object.scale = parseVec3(value);
        } else {
            if (key == "source") object.source = parseQuoted(value);
            else if (key == "rotation") object.rotation = parseVec3(value);
            else if (key == "position") object.position = parseVec3(value);
            else if (key == "scale") object.scale = parseVec3(value);
        }
    }

    if (object.source.empty()) {
        throw std::runtime_error("No object source found in scene descriptor: " + path);
    }
    return object;
}

static uint32_t readU32(const std::vector<uint8_t>& bytes, size_t offset) {
    if (offset + 4 > bytes.size()) {
        throw std::runtime_error("Unexpected end of file while reading u32.");
    }
    uint32_t out = 0;
    std::memcpy(&out, bytes.data() + offset, 4);
    return out;
}

static std::vector<uint8_t> loadFileBytes(const std::string& path) {
    std::ifstream in(path, std::ios::binary | std::ios::ate);
    if (!in) {
        throw std::runtime_error("Cannot open file: " + path);
    }
    const std::streamsize size = in.tellg();
    if (size <= 0) {
        throw std::runtime_error("File is empty: " + path);
    }
    in.seekg(0, std::ios::beg);
    std::vector<uint8_t> data(static_cast<size_t>(size));
    if (!in.read(reinterpret_cast<char*>(data.data()), size)) {
        throw std::runtime_error("Failed to read file: " + path);
    }
    return data;
}

static const nlohmann::json& getBufferView(const nlohmann::json& gltf, int index) {
    return gltf.at("bufferViews").at(index);
}

static const nlohmann::json& getAccessor(const nlohmann::json& gltf, int index) {
    return gltf.at("accessors").at(index);
}

static std::vector<Vertex> readPositions(const nlohmann::json& gltf, const std::vector<uint8_t>& bin, int accessorIndex) {
    const auto& accessor = getAccessor(gltf, accessorIndex);
    const int count = accessor.at("count").get<int>();
    const int componentType = accessor.at("componentType").get<int>();
    const std::string type = accessor.at("type").get<std::string>();
    if (componentType != 5126 || type != "VEC3") {
        throw std::runtime_error("POSITION accessor is not float VEC3.");
    }

    const auto& bufferView = getBufferView(gltf, accessor.at("bufferView").get<int>());
    const size_t viewOffset = bufferView.value("byteOffset", 0);
    const size_t accessorOffset = accessor.value("byteOffset", 0);
    const size_t stride = bufferView.value("byteStride", static_cast<int>(sizeof(float) * 3));
    const size_t start = viewOffset + accessorOffset;

    std::vector<Vertex> out;
    out.reserve(static_cast<size_t>(count));
    for (int i = 0; i < count; ++i) {
        const size_t pos = start + static_cast<size_t>(i) * stride;
        if (pos + sizeof(float) * 3 > bin.size()) {
            throw std::runtime_error("POSITION accessor points outside BIN chunk.");
        }
        Vertex v{};
        std::memcpy(&v.x, bin.data() + pos, sizeof(float));
        std::memcpy(&v.y, bin.data() + pos + sizeof(float), sizeof(float));
        std::memcpy(&v.z, bin.data() + pos + sizeof(float) * 2, sizeof(float));
        out.push_back(v);
    }
    return out;
}

static std::vector<uint32_t> readIndices(const nlohmann::json& gltf, const std::vector<uint8_t>& bin, int accessorIndex) {
    const auto& accessor = getAccessor(gltf, accessorIndex);
    const int count = accessor.at("count").get<int>();
    const int componentType = accessor.at("componentType").get<int>();
    const std::string type = accessor.at("type").get<std::string>();
    if (type != "SCALAR") {
        throw std::runtime_error("Indices accessor must be SCALAR.");
    }

    const auto& bufferView = getBufferView(gltf, accessor.at("bufferView").get<int>());
    const size_t viewOffset = bufferView.value("byteOffset", 0);
    const size_t accessorOffset = accessor.value("byteOffset", 0);

    size_t componentSize = 0;
    if (componentType == 5121) componentSize = 1;
    if (componentType == 5123) componentSize = 2;
    if (componentType == 5125) componentSize = 4;
    if (componentSize == 0) {
        throw std::runtime_error("Unsupported index componentType.");
    }

    const size_t stride = bufferView.value("byteStride", static_cast<int>(componentSize));
    const size_t start = viewOffset + accessorOffset;

    std::vector<uint32_t> out;
    out.reserve(static_cast<size_t>(count));
    for (int i = 0; i < count; ++i) {
        const size_t pos = start + static_cast<size_t>(i) * stride;
        if (pos + componentSize > bin.size()) {
            throw std::runtime_error("Index accessor points outside BIN chunk.");
        }
        uint32_t index = 0;
        if (componentType == 5121) {
            index = bin[pos];
        } else if (componentType == 5123) {
            uint16_t x = 0;
            std::memcpy(&x, bin.data() + pos, sizeof(uint16_t));
            index = x;
        } else {
            std::memcpy(&index, bin.data() + pos, sizeof(uint32_t));
        }
        out.push_back(index);
    }
    return out;
}

static ModelData loadModelFromGlb(const std::string& modelPath) {
    const std::vector<uint8_t> bytes = loadFileBytes(modelPath);
    if (bytes.size() < 20) {
        throw std::runtime_error("Invalid GLB: file too small.");
    }

    const uint32_t magic = readU32(bytes, 0);
    const uint32_t version = readU32(bytes, 4);
    if (magic != 0x46546C67 || version != 2) {
        throw std::runtime_error("Invalid GLB header (expecting glTF 2.0 binary).");
    }

    size_t offset = 12;
    std::string jsonChunk;
    std::vector<uint8_t> binChunk;
    while (offset + 8 <= bytes.size()) {
        const uint32_t chunkLength = readU32(bytes, offset);
        const uint32_t chunkType = readU32(bytes, offset + 4);
        offset += 8;
        if (offset + chunkLength > bytes.size()) {
            throw std::runtime_error("Invalid GLB chunk size.");
        }
        if (chunkType == 0x4E4F534A) {
            jsonChunk.assign(reinterpret_cast<const char*>(bytes.data() + offset), chunkLength);
        } else if (chunkType == 0x004E4942) {
            binChunk.assign(bytes.begin() + static_cast<long>(offset), bytes.begin() + static_cast<long>(offset + chunkLength));
        }
        offset += chunkLength;
    }
    if (jsonChunk.empty() || binChunk.empty()) {
        throw std::runtime_error("GLB is missing JSON or BIN chunk.");
    }

    const nlohmann::json gltf = nlohmann::json::parse(jsonChunk);
    if (!gltf.contains("meshes")) {
        throw std::runtime_error("GLB has no meshes.");
    }

    std::vector<Triangle> triangles;
    for (const auto& mesh : gltf.at("meshes")) {
        if (!mesh.contains("primitives")) continue;
        for (const auto& primitive : mesh.at("primitives")) {
            const int mode = primitive.value("mode", 4);
            if (mode != 4) continue; // triangles only
            if (!primitive.contains("attributes") || !primitive.at("attributes").contains("POSITION")) {
                continue;
            }

            const int positionAccessor = primitive.at("attributes").at("POSITION").get<int>();
            const std::vector<Vertex> positions = readPositions(gltf, binChunk, positionAccessor);
            if (positions.empty()) continue;

            if (primitive.contains("indices")) {
                const int indexAccessor = primitive.at("indices").get<int>();
                const std::vector<uint32_t> indices = readIndices(gltf, binChunk, indexAccessor);
                for (size_t i = 0; i + 2 < indices.size(); i += 3) {
                    const uint32_t ia = indices[i];
                    const uint32_t ib = indices[i + 1];
                    const uint32_t ic = indices[i + 2];
                    if (ia >= positions.size() || ib >= positions.size() || ic >= positions.size()) continue;
                    triangles.push_back({positions[ia], positions[ib], positions[ic]});
                }
            } else {
                for (size_t i = 0; i + 2 < positions.size(); i += 3) {
                    triangles.push_back({positions[i], positions[i + 1], positions[i + 2]});
                }
            }
        }
    }
    if (triangles.empty()) {
        throw std::runtime_error("Model has no triangle primitives.");
    }

    float minX = triangles[0].a.x, maxX = triangles[0].a.x;
    float minY = triangles[0].a.y, maxY = triangles[0].a.y;
    float minZ = triangles[0].a.z, maxZ = triangles[0].a.z;
    auto include = [&](const Vertex& v) {
        if (v.x < minX) minX = v.x;
        if (v.x > maxX) maxX = v.x;
        if (v.y < minY) minY = v.y;
        if (v.y > maxY) maxY = v.y;
        if (v.z < minZ) minZ = v.z;
        if (v.z > maxZ) maxZ = v.z;
    };
    for (const Triangle& t : triangles) {
        include(t.a);
        include(t.b);
        include(t.c);
    }

    const float sizeX = maxX - minX;
    const float sizeY = maxY - minY;
    const float sizeZ = maxZ - minZ;
    const float largestSize = std::max(sizeX, std::max(sizeY, sizeZ));
    const float safeLargestSize = (largestSize > 0.0001f) ? largestSize : 1.0f;

    ModelData model{};
    model.triangles = std::move(triangles);
    model.center = {(minX + maxX) * 0.5f, (minY + maxY) * 0.5f, (minZ + maxZ) * 0.5f};
    model.scale = 2.0f / safeLargestSize;
    return model;
}

static void drawTriangles(const std::vector<Triangle>& triangles) {
    glColor3f(0.85f, 0.85f, 0.9f);
    glBegin(GL_TRIANGLES);
    for (const Triangle& tri : triangles) {
        glVertex3f(tri.a.x, tri.a.y, tri.a.z);
        glVertex3f(tri.b.x, tri.b.y, tri.b.z);
        glVertex3f(tri.c.x, tri.c.y, tri.c.z);
    }
    glEnd();
}

int main() {
    const std::string scenePath = "scene.toml";
    SceneObject sceneObject{};
    ModelData model;

    try {
        sceneObject = loadSceneObjectFromToml(scenePath);
    } catch (const std::exception& ex) {
        std::cerr << "Failed to parse scene descriptor '" << scenePath << "': " << ex.what() << "\n";
        return 1;
    }

    if (!glfwInit()) {
        std::cerr << "Failed to initialize GLFW\n";
        return 1;
    }

    GLFWwindow* window = glfwCreateWindow(900, 600, "Renderer.cpp - model.glb", nullptr, nullptr);
    if (!window) {
        std::cerr << "Failed to create window\n";
        glfwTerminate();
        return 1;
    }

    glfwMakeContextCurrent(window);
    glfwSwapInterval(1);

    glEnable(GL_DEPTH_TEST);
    glClearColor(0.07f, 0.08f, 0.10f, 1.0f);

    if (!std::filesystem::exists(sceneObject.source)) {
        std::cerr << "Error: '" << sceneObject.source << "' was not found.\n";
        glfwDestroyWindow(window);
        glfwTerminate();
        return 1;
    }

    try {
        model = loadModelFromGlb(sceneObject.source);
    } catch (const std::exception& ex) {
        std::cerr << "Failed to load model '" << sceneObject.source << "': " << ex.what() << "\n";
        glfwDestroyWindow(window);
        glfwTerminate();
        return 1;
    }
    std::cout << "Loaded model '" << sceneObject.source << "' with "
              << model.triangles.size() << " triangles.\n";

    while (!glfwWindowShouldClose(window)) {
        int width = 1;
        int height = 1;
        glfwGetFramebufferSize(window, &width, &height);
        if (height <= 0) {
            height = 1;
        }

        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(60.0, static_cast<double>(width) / static_cast<double>(height), 0.1, 200.0);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        gluLookAt(0.0, 2.0, 4.0, 0.0, 1.0, -6.0, 0.0, 1.0, 0.0);

        glPushMatrix();
        glTranslatef(sceneObject.position.x, sceneObject.position.y, sceneObject.position.z);
        glRotatef(sceneObject.rotation.x, 1.0f, 0.0f, 0.0f);
        glRotatef(sceneObject.rotation.y, 0.0f, 1.0f, 0.0f);
        glRotatef(sceneObject.rotation.z, 0.0f, 0.0f, 1.0f);
        glScalef(sceneObject.scale.x * model.scale, sceneObject.scale.y * model.scale, sceneObject.scale.z * model.scale);
        glTranslatef(-model.center.x, -model.center.y, -model.center.z);
        drawTriangles(model.triangles);
        glPopMatrix();

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    glfwDestroyWindow(window);
    glfwTerminate();
    return 0;
}
