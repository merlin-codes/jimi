package dev.levia.jimi.ai.ai;

import static dev.levia.jimi.engine.trans.NativeHelper.*;

import dev.levia.jimi.util.trans.GL;
import dev.levia.jimi.util.trans.GLFW;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public class GearWindow {

    private final Arena alloc;

    private float viewRotX = 20f;
    private float viewRotY = 30f;
    private float viewRotZ = 0f;

    public GearWindow() {
        this.alloc = Arena.global();
    }

    public void run() {
        if (GLFW.init() == 0) {
            IO.println("Failed to initialize GLFW");
            System.exit(1);
        }

        MemorySegment window = GLFW.createWindow(
            300,
            300,
            alloc.allocateFrom("Gears"),
            MemorySegment.NULL,
            MemorySegment.NULL
        );
        if (window == MemorySegment.NULL) {
            IO.println("Failed to open GLFW window");
            GLFW.terminate();
            System.exit(1);
        }

        GLFW.makeContextCurrent(window);
        GLFW.swapInterval(1);

        GL.enable(GL.GL_DEPTH_TEST);
        GL.clearColor(0f, 0f, 0f, 1f);

        MemorySegment width = alloc.allocate(I32);
        MemorySegment height = alloc.allocate(I32);

        while (GLFW.windowShouldClose(window) == 0) {
            GLFW.getWindowSize(window, width, height);
            int w = width.get(I32, 0);
            int h = Math.max(1, height.get(I32, 0));

            reshape(w, h);
            draw((float) (100.0 * GLFW.getTime()));

            GLFW.swapBuffers(window);
            GLFW.pollEvents();
        }

        GLFW.destroyWindow(window);
        GLFW.terminate();
    }

    private void reshape(int width, int height) {
        float h = (float) height / (float) width;
        float zNear = 5f;
        float zFar = 30f;
        float xMax = zNear * 0.5f;

        GL.viewport(0, 0, width, height);
        GL.matrixMode(GL.GL_PROJECTION);
        GL.loadIdentity();
        GL.frustum(-xMax, xMax, -xMax * h, xMax * h, zNear, zFar);
        GL.matrixMode(GL.GL_MODELVIEW);
        GL.loadIdentity();
    }

    private void draw(float angle) {
        GL.clear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        GL.matrixMode(GL.GL_MODELVIEW);
        GL.loadIdentity();
        GL.translatef(0f, 0f, -20f);
        GL.rotatef(viewRotX, 1f, 0f, 0f);
        GL.rotatef(viewRotY, 0f, 1f, 0f);
        GL.rotatef(viewRotZ, 0f, 0f, 1f);

        drawGear(-3.0f, -2.0f, angle, 0.8f, 0.1f, 0.0f, 1f, 4f, 1f, 20, 0.7f);
        drawGear(
            3.1f,
            -2.0f,
            -2f * angle - 9f,
            0.0f,
            0.8f,
            0.2f,
            0.5f,
            2f,
            2f,
            10,
            0.7f
        );
        drawGear(
            -3.1f,
            4.2f,
            -2f * angle - 25f,
            0.2f,
            0.2f,
            1.0f,
            1.3f,
            2f,
            0.5f,
            10,
            0.7f
        );
    }

    private void drawGear(
        float tx,
        float ty,
        float rotation,
        float cr,
        float cg,
        float cb,
        float innerRadius,
        float outerRadius,
        float width,
        int teeth,
        float toothDepth
    ) {
        GL.loadIdentity();
        GL.translatef(0f, 0f, -20f);
        GL.rotatef(viewRotX, 1f, 0f, 0f);
        GL.rotatef(viewRotY, 0f, 1f, 0f);
        GL.rotatef(viewRotZ, 0f, 0f, 1f);
        GL.translatef(tx, ty, 0f);
        GL.rotatef(rotation, 0f, 0f, 1f);
        GL.color3f(cr, cg, cb);
        gear(innerRadius, outerRadius, width, teeth, toothDepth);
    }

    private void gear(
        float innerRadius,
        float outerRadius,
        float width,
        int teeth,
        float toothDepth
    ) {
        float r0 = innerRadius;
        float r1 = outerRadius - toothDepth / 2f;
        float r2 = outerRadius + toothDepth / 2f;
        float da = (2f * (float) Math.PI) / teeth / 4f;

        GL.begin(GL.GL_QUAD_STRIP);
        for (int i = 0; i <= teeth; i++) {
            float angle = (i * 2f * (float) Math.PI) / teeth;
            GL.vertex3f(
                r0 * (float) Math.cos(angle),
                r0 * (float) Math.sin(angle),
                width * 0.5f
            );
            GL.vertex3f(
                r1 * (float) Math.cos(angle),
                r1 * (float) Math.sin(angle),
                width * 0.5f
            );
            if (i < teeth) {
                GL.vertex3f(
                    r0 * (float) Math.cos(angle),
                    r0 * (float) Math.sin(angle),
                    width * 0.5f
                );
                GL.vertex3f(
                    r1 * (float) Math.cos(angle + 3f * da),
                    r1 * (float) Math.sin(angle + 3f * da),
                    width * 0.5f
                );
            }
        }
        GL.end();

        GL.begin(GL.GL_QUADS);
        for (int i = 0; i < teeth; i++) {
            float angle = (i * 2f * (float) Math.PI) / teeth;
            GL.vertex3f(
                r1 * (float) Math.cos(angle),
                r1 * (float) Math.sin(angle),
                width * 0.5f
            );
            GL.vertex3f(
                r2 * (float) Math.cos(angle + da),
                r2 * (float) Math.sin(angle + da),
                width * 0.5f
            );
            GL.vertex3f(
                r2 * (float) Math.cos(angle + 2f * da),
                r2 * (float) Math.sin(angle + 2f * da),
                width * 0.5f
            );
            GL.vertex3f(
                r1 * (float) Math.cos(angle + 3f * da),
                r1 * (float) Math.sin(angle + 3f * da),
                width * 0.5f
            );
        }
        GL.end();

        GL.begin(GL.GL_QUAD_STRIP);
        for (int i = 0; i <= teeth; i++) {
            float angle = (i * 2f * (float) Math.PI) / teeth;
            GL.vertex3f(
                r1 * (float) Math.cos(angle),
                r1 * (float) Math.sin(angle),
                -width * 0.5f
            );
            GL.vertex3f(
                r0 * (float) Math.cos(angle),
                r0 * (float) Math.sin(angle),
                -width * 0.5f
            );
            if (i < teeth) {
                GL.vertex3f(
                    r1 * (float) Math.cos(angle + 3f * da),
                    r1 * (float) Math.sin(angle + 3f * da),
                    -width * 0.5f
                );
                GL.vertex3f(
                    r0 * (float) Math.cos(angle),
                    r0 * (float) Math.sin(angle),
                    -width * 0.5f
                );
            }
        }
        GL.end();

        GL.begin(GL.GL_QUADS);
        for (int i = 0; i < teeth; i++) {
            float angle = (i * 2f * (float) Math.PI) / teeth;
            GL.vertex3f(
                r1 * (float) Math.cos(angle + 3f * da),
                r1 * (float) Math.sin(angle + 3f * da),
                -width * 0.5f
            );
            GL.vertex3f(
                r2 * (float) Math.cos(angle + 2f * da),
                r2 * (float) Math.sin(angle + 2f * da),
                -width * 0.5f
            );
            GL.vertex3f(
                r2 * (float) Math.cos(angle + da),
                r2 * (float) Math.sin(angle + da),
                -width * 0.5f
            );
            GL.vertex3f(
                r1 * (float) Math.cos(angle),
                r1 * (float) Math.sin(angle),
                -width * 0.5f
            );
        }
        GL.end();
    }
}
