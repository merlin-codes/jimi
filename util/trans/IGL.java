package dev.levia.jimi.util.trans;

import dev.levia.jimi.util.anot.Library;
import dev.levia.jimi.util.anot.NoPrefix;
import dev.levia.jimi.util.trans.GL;

import java.lang.foreign.MemorySegment;

@Library(name = "libGL.so.1", prefix = "gl")
public interface IGL {
    final static int GL_COLOR_BUFFER_BIT = 0x00004000;
    final static int GL_PROJECTION = 0x1701;
    final static int Float = 0x1406;
    final static int GL_MODELVIEW = 0x1700;
    final static int GL_TRIANGLES = 0x0004;



    final static MemorySegment NULL = MemorySegment.NULL;

    void viewport(int x, int y, int x1, int y1);
    void clearColor(double r, double g, double b, double a);
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
    void loadIdentity();
    void translatef(float x, float y, float z);
    void begin(int what);
    void color3f(float r, float b, float g);
    void vertex3f(float x, float y, float z);
    void end();
    void rotatef(double x, double y, double z, double angle);

    static void addPoint(float r, float b, float g, float x, float y, float z) {
        GL.color3f(r, g, b);
        GL.vertex3f(x, y, z);
    }
}
