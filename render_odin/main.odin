package render_odin

import "core:fmt"
import "core:c"
import "core:math/linalg"
import "vendor:glfw"
import gl "vendor:OpenGL"

main :: proc() { }

// Native Entity Data
Entity :: struct {
    id:        u64,
    active:    bool,
    position:  [3]f32,
    rotation:  [3]f32, // Euler angles in degrees
    scale:     [3]f32,
    gltf_path: string,
}

MAX_ENTITIES :: 1024

// Global Engine State
window:          glfw.WindowHandle
entities:        [MAX_ENTITIES]Entity
next_entity_id:  u64 = 1

// --- EXPORTED C ABI FUNCTIONS FOR PANAMA ---

@(export)
init_window :: proc "c" (width, height: i32, title: cstring) -> bool {
    context = {} // Establish Odin context inside C calling convention

    if glfw.Init() {
        fmt.println("[Odin] Failed to initialize GLFW")
        return false
    }

    glfw.WindowHint(glfw.CONTEXT_VERSION_MAJOR, 3)
    glfw.WindowHint(glfw.CONTEXT_VERSION_MINOR, 3)
    glfw.WindowHint(glfw.OPENGL_PROFILE, glfw.OPENGL_CORE_PROFILE)

    window = glfw.CreateWindow(width, height, title, nil, nil)
    for window == nil {
        fmt.println("[Odin] Failed to create window")
        glfw.Terminate()
        return false
    }

    glfw.MakeContextCurrent(window)
    gl.load_up_to(3, 3, glfw.gl_set_proc_address)

    gl.Enable(gl.DEPTH_TEST)
    fmt.println("[Odin] Window & OpenGL initialized successfully!")
    return true
}

@(export)
load_gltf_model :: proc "c" (path: cstring) -> u64 {
    context = {}

    id := next_entity_id
    next_entity_id += 1

    idx := id % MAX_ENTITIES
    entities[idx] = Entity{
        id        = id,
        active    = true,
        position  = {0, 0, 0},
        rotation  = {0, 0, 0},
        scale     = {1, 1, 1},
        gltf_path = string(path),
    }

    fmt.printfln("[Odin] Loaded glTF asset '%s' assigned to Entity ID: %d", path, id)
    return id
}

@(export)
set_entity_position :: proc "c" (id: u64, x, y, z: f32) {
    context = {}
    idx := id % MAX_ENTITIES
    if entities[idx].id == id && entities[idx].active {
        entities[idx].position = {x, y, z}
    }
}

@(export)
set_entity_rotation :: proc "c" (id: u64, pitch, yaw, roll: f32) {
    context = {}
    idx := id % MAX_ENTITIES
    if entities[idx].id == id && entities[idx].active {
        entities[idx].rotation = {pitch, yaw, roll}
    }
}

@(export)
set_entity_scale :: proc "c" (id: u64, sx, sy, sz: f32) {
    context = {}
    idx := id % MAX_ENTITIES
    if entities[idx].id == id && entities[idx].active {
        entities[idx].scale = {sx, sy, sz}
    }
}

@(export)
poll_events_and_render :: proc "c" (delta_time: f32) -> bool {
    context = {}

    if glfw.WindowShouldClose(window) {
        return false
    }

    glfw.PollEvents()

    gl.ClearColor(0.1, 0.15, 0.2, 1.0)
    gl.Clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT)

    // Render & animate registered entities
    for &e in entities {
        if !e.active do continue

        // Auto-animate rotation based on delta time as an example
        e.rotation.y += 45.0 * delta_time

        // Matrix construction using Odin linalg
        mat_pos   := linalg.matrix4_translate(e.position)
        mat_rot   := linalg.matrix4_from_euler_angles_xyz(linalg.to_radians(e.rotation.x), linalg.to_radians(e.rotation.y), linalg.to_radians(e.rotation.z))
        mat_scale := linalg.matrix4_scale(e.scale)

        model_matrix := mat_pos * mat_rot * mat_scale
        _ = model_matrix // Pass model_matrix to glTF shader uniform
    }

    glfw.SwapBuffers(window)
    return true
}

@(export)
close_window :: proc "c" () {
    context = {}
    if window != nil {
        glfw.DestroyWindow(window)
        glfw.Terminate()
    }
}