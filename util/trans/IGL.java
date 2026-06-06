package dev.levia.jimi.util.trans;

import java.lang.foreign.MemorySegment;

import dev.levia.jimi.util.anot.Library;

@Library(name = "libGL.so.1", prefix = "gl")
public interface IGL {
    final static int GL_COLOR_BUFFER_BIT = 0x00004000;
    final static int GL_DEPTH_BUFFER_BIT = 0x00000100;
    final static int GL_PROJECTION = 0x1701;
    final static int Float = 0x1406;
    final static int GL_MODELVIEW = 0x1700;
    final static int GL_TRIANGLES = 0x0004;
    final static int GL_QUADS = 0x0007;
    final static int GL_QUAD_STRIP = 0x0008;
    final static int GL_DEPTH_TEST = 0x0B71;
    final static int GL_LINES = 0x0001;
    final static int GL_LINE_LOOP = 0x0002;
    final static int GL_TEXTURE_2D = 0x0DE1;
    final static int GL_RGBA = 0x1908;
    final static int GL_BGRA = 0x80E1;
    final static int GL_UNSIGNED_BYTE = 0x1401;
    final static int GL_LINEAR = 0x2601;
    final static int GL_CLAMP_TO_EDGE = 0x812F;
    final static int GL_TEXTURE_MAG_FILTER = 0x2800;
    final static int GL_TEXTURE_MIN_FILTER = 0x2801;
    final static int GL_TEXTURE_WRAP_S = 0x2802;
    final static int GL_TEXTURE_WRAP_T = 0x2803;
    final static int GL_TEXTURE_COORD_ARRAY = 0x8078;
    final static int GL_BLEND = 0x0BE2;
    final static int GL_SRC_ALPHA = 0x0302;
    final static int GL_ONE_MINUS_SRC_ALPHA = 0x0303;

    final static MemorySegment NULL = MemorySegment.NULL;

    void viewport(int x, int y, int x1, int y1);
    void clearColor(float r, float g, float b, float a);
    void clear(int bitMask);

    void genVertexArrays(int index, MemorySegment memory);
    void genBuffers(int index, MemorySegment memory);
    void bindVertexArray(int index);
    void bindBuffer(int index, int buffer);
    void bufferData(int index, long i, MemorySegment memory, int j);
    // protected final MethodHandle vertexAttribPointer = link.downcallHandle(gl.findOrThrow("glVertexAttribPointer"), FunctionDescriptor.ofVoid(I32, I32, I32, BOOL, I32, ADDRESS));
    void enableVertexAttribArray(int flag);

    int createShader(int programType);
    void shaderSource(int x, int y, MemorySegment a, MemorySegment b);
    void compileShader(int index);
    int createProgram();
    void attachShader(int program, int index);
    void linkProgram(int program);

    void useProgram(int program);
    void drawArrays(int x, int y, int z);
    void matrixMode(int mode);
    void frustum(double left, double right, double bottom, double top, double zNear, double zFar);
    void loadIdentity();
    void translatef(float x, float y, float z);
    void scalef(float x, float y, float z);
    void begin(int what);
    void color3f(float r, float b, float g);
    void vertex3f(float x, float y, float z);
    void end();
    void rotatef(float x, float y, float z, float angle);
    void enable(int cap);
    void disable(int cap);
    void ortho(double left, double right, double bottom, double top, double zNear, double zFar);
    void vertex2f(float x, float y);
    void texCoord2f(float s, float t);
    void genTextures(int n, MemorySegment textures);
    void bindTexture(int target, int texture);
    void texImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, MemorySegment pixels);
    void texParameteri(int target, int pname, int param);
    void deleteTextures(int n, MemorySegment textures);
    void blendFunc(int sfactor, int dfactor);

    void color4f(float r, float g, float b, float a);
}
