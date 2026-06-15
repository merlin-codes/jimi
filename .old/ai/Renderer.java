package dev.levia.jimi.ai.ai;

import static dev.levia.jimi.engine.trans.NativeHelper.I32;

import dev.levia.jimi.engine.intern.Toml;
import dev.levia.jimi.util.trans.GL;
import dev.levia.jimi.util.trans.GLFW;
import dev.levia.jimi.util.trans.GLU;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private final Arena alloc = Arena.global();
    private List<Toml.SceneObject> objects = new ArrayList<>();

    public void run() {
        try {
            IO.println(Path.of(".").toAbsolutePath());
            objects = Toml.load(Path.of("scene.toml"));
        } catch (Throwable t) {
            IO.println("Failed to parse scene: " + t.getMessage());
            return;
        }

        // if (objects.isEmpty()) { IO.println("Scene has no [root.<name>] objects."); return; }

        if (GLFW.init() == 0) {
            IO.println("Failed to initialize GLFW");
            return;
        }

        MemorySegment window = GLFW.createWindow(
            900,
            600,
            alloc.allocateFrom("Renderer"),
            MemorySegment.NULL,
            MemorySegment.NULL
        );
        if (window == MemorySegment.NULL) {
            IO.println("Failed to open GLFW window");
            GLFW.terminate();
            return;
        }

        GLFW.makeContextCurrent(window);
        GLFW.swapInterval(1);
        GL.enable(GL.GL_DEPTH_TEST);
        GL.clearColor(0.07f, 0.08f, 0.10f, 1.0f);

        MemorySegment width = alloc.allocate(I32);
        MemorySegment height = alloc.allocate(I32);

        GLFW.getWindowSize(window, width, height);
        int w = width.get(I32, 0);
        int h = Math.max(1, height.get(I32, 0));
        GL.viewport(0, 0, w, h);

        var start = System.nanoTime();
        while (GLFW.windowShouldClose(window) == 0) {
            GL.clear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            GL.matrixMode(GL.GL_PROJECTION);
            GL.loadIdentity();
            GLU.perspective(60.0, (double) w / (double) h, 0.1, 200.0);

            GLFW.swapBuffers(window);
            GLFW.pollEvents();

            var actual = (float) (start - System.nanoTime());
            update(actual / 1_000_000_000f);
            start = System.nanoTime();
        }

        GLFW.destroyWindow(window);
        GLFW.terminate();
    }

    private float multiply = 10f;

    private void update(float delta) {
        var object = objects.get(2);
        object.rotation.y += (delta * multiply);
        if (Math.abs(object.rotation.y) > 30f) multiply *= -1;
        IO.println("rotation is: " + object.position.y);
        // do something every update
    }
}
