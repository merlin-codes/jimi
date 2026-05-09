package dev.levia.jimi.engine.lang;

import dev.levia.jimi.engine.trans.OpenGl;
import dev.levia.jimi.util.trans.GL;
import dev.levia.jimi.util.trans.GLFW;

import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.List;

import static dev.levia.jimi.engine.trans.NativeHelper.*;

/**
 * simply put this world is main controlled entity, root of project
 * everything existing, in world is used by this
 */
public class World extends OpenGl {
    private List<Entity> children = new ArrayList<>();

    public int vao = 0;
    public int program = 0;

    @Override
    public void update(float delta) {
        // GL.drawArrays(4, 0, 3);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void preRender() {
        MemorySegment width = allocator.allocate(I32), height = allocator.allocate(I32);
        GLFW.getFramebufferSize(this.window, width, height);
        GL.viewport(0, 0, width.get(I32, 0), height.get(I32, 0));
        IO.println("Viewport is set to "+width+" "+height);
    //     var vertices = new float[]{ -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.0f,  0.5f, 0.0f };
    //     var verts = allocator.allocateFrom(F32, vertices);
    //     var pvao = allocator.allocate(I32);
    //     var pvbo = allocator.allocate(I32);

    //     GL.genVertexArrays(1, pvao);
    //     GL.genBuffers(1, pvbo);

    //     GL.bindVertexArray(pvao.get(I32, 0));
    //     GL.bindBuffer(34962, pvao.get(I32, 0));
    //     GL.bufferData(34962, verts.byteSize(), verts, 35044);
    //     // vertexAttribPointer.invokeExact(0, 3, 5126, false, 3*4, MemorySegment.NULL);
    //     GL.enableVertexAttribArray(0);
    //     GL.bindBuffer(34962, 0);
    //     GL.bindVertexArray(0);

    //     // shaders
    //     var vertex = createShaderFull(35633);
    //     var fragment = createShaderFull(35632);

    //     int program = GL.createProgram();
    //     GL.attachShader(program, vertex);
    //     GL.attachShader(program, fragment);
    //     GL.linkProgram(program);
    //     GL.useProgram(program);
    // }
    // private int createShaderFull(int shaderType) {
    //     int shader = GL.createShader(shaderType);
    //     var source = allocator.allocateFrom(vertexShaderSource);
    //     var pointer = allocator.allocate(ADDRESS);
    //     pointer.set(ADDRESS, 0, source);
    //     GL.shaderSource(shader, 1, pointer, MemorySegment.NULL);
    //     GL.compileShader(shader);
    //     return shader;
    }

    /**
     * This method builds binary scene, this is for performance way
     * scene are mostly immutable so for engine to not need too many changes,
     * and by doing this we can optimize background Entities and collision, Concept etc.
     */
    public void build() {
    }
}
