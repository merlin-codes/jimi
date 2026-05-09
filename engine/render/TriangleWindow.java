package dev.levia.jimi.engine.render;

import dev.levia.jimi.util.trans.GL;
import dev.levia.jimi.util.trans.GLFW;
import dev.levia.jimi.util.trans.GLU;
import dev.levia.jimi.util.trans.IGL;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static dev.levia.jimi.engine.trans.NativeHelper.*;
import static dev.levia.jimi.util.trans.IGL.*;

public class TriangleWindow {
    private final Arena alloc;
    public TriangleWindow() {
        alloc = Arena.global();
    }
    public void run() {
        MemorySegment width = alloc.allocate(I64), height = alloc.allocate(I64), x = alloc.allocate(F64), y = alloc.allocate(F64), window;

        // Initialise GLFW
        if (GLFW.init() == 0) {
            IO.println("Failed to initialize GLFW");
            System.exit(1);
        }

        // Open a window and create its OpenGL context
        window = GLFW.createWindow(640, 480, alloc.allocateFrom("Spinning Triangle"), MemorySegment.NULL, MemorySegment.NULL);
        if (window == MemorySegment.NULL) {
            IO.println("Failed to open GLFW window");
            System.exit(1);
        }

        // Enable vertical sync (on cards that support it)
        GLFW.makeContextCurrent(window);
        GLFW.swapInterval(1);

        // Ensure we can capture the escape key being pressed below
        // TODO for now skipping this step
        // GLFW.setInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);
        var should = GLFW.windowShouldClose(window) == 0;

        while (should) {
            // double t = glfwGetTime();
            GLFW.getCursorPos(window, x, y);

            // Get window size (may be different than the requested size)
            GLFW.getWindowSize(window, width, height);


            GL.viewport(0, 0, width.get(I32, 0), height.get(I32, 0));

            // Clear color buffer to black
            GL.clearColor(0.f, 0.f, 0.f, 0.f);
            GL.clear(GL.GL_COLOR_BUFFER_BIT);

            // Select and setup the projection matrix
            GL.matrixMode(GL_PROJECTION);
            GL.loadIdentity();
            GLU.perspective(65.f, width.get(F64, 0) / height.get(F64, 0), 1.f, 100.f);

            // Select and setup the modelview matrix
            GL.matrixMode(GL_MODELVIEW);
            GL.loadIdentity();
            GLU.lookAt(0.f, 1.f, 0.f,   // Eye-position
                    0.f, 20.f, 0.f,         // View-point
                    0.f, 0.f, 1.f);            // Up-vector

            // Draw a rotating colorful triangle
            GL.translatef(0.f, 14.f, 0.f);
            GL.rotatef(0.3f * x.get(F64, 0) + 0.001f * 100.f, 0.f, 0.f, 1.f);

            GL.begin(GL_TRIANGLES);
            GL.addPoint(1.f, 0.f, 0.f, -5.f, 0.f, -4.f);
            GL.addPoint(0.f, 1.f, 0.f, 5.f, 0.f, -4.f);
            GL.addPoint(0.f, 0.f, 1.f, 0.f, 0.f, 6.f);
            GL.end();

            // Swap buffers
            GLFW.swapBuffers(window);
            GLFW.pollEvents();

            // Check if the ESC key was pressed or the window should be closed
            should = GLFW.windowShouldClose(window) == 0;
        }

        // Close OpenGL window and terminate GLFW
        GLFW.terminate();
    }
}
