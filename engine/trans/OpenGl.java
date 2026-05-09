package dev.levia.jimi.engine.trans;

import dev.levia.jimi.util.trans.GL;
import dev.levia.jimi.util.trans.GLFW;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.concurrent.locks.LockSupport;

import static dev.levia.jimi.engine.trans.NativeHelper.*;

public class OpenGl {
    protected final Arena allocator;
    private MemorySegment title = MemorySegment.NULL;
    protected MemorySegment window;

    public OpenGl() { this.allocator = Arena.ofShared(); }

    private final boolean COUNT_FRAMES = true;

    public void createWindow(int fpsLimit) {
        if (GLFW.init() == 0) {
            throw new RuntimeException("GLFW init failed");
        }
        title = allocator.allocateFrom("Jimi Game Engine");
        GLFW.initHint(139266, 4);
        GLFW.initHint(139267, 5);

        var monitor = GLFW.getPrimaryMonitor();
        // var alloctedVidMode = allocator.allocate((new GLFWVidMode(0, 0, 0, 0, 0, 0)).bytes());
        var width = allocator.allocate(I32);
        var height = allocator.allocate(I32);

        window = GLFW.createWindow(800, 800, title, MemorySegment.NULL, MemorySegment.NULL);
        GLFW.getFramebufferSize(window, width, height);
        // var vidmode = getStruct((MemorySegment) x, GLFWVidMode.class);
        System.out.println(width.get(I32, 0)+" x "+height.get(I32, 0));

        // if (window != 0) throw new RuntimeException("GLFW window failed to created");
        GLFW.makeContextCurrent(window);
        GLFW.swapInterval(1);
        GLFW.swapBuffers(window);

        long start = System.nanoTime();
        int timeForOneFrame = 1_000_000_000 / fpsLimit;
        IO.println("[FPS]: frame rate set to "+fpsLimit+" that is frame every "+timeForOneFrame+" ns");
        var frames = 0;
        preRender();
        while (GLFW.windowShouldClose(window) == 0) {
            long end = start + timeForOneFrame;


            GL.clearColor(1.0f, 0.0f, 0.0f, 1.0f);
            GL.clear(GL.GL_COLOR_BUFFER_BIT);

            GLFW.swapBuffers(window);
            GLFW.pollEvents();

            // update(((float) ((System.nanoTime() - end)%1_000_000_000))/1_000_000_000f);
            update(((float) (System.nanoTime() - end))/1_000_000_000f);

            if (System.nanoTime() < end) {
                while (System.nanoTime() < end - 2_000_000L) LockSupport.parkNanos(1_000_000L);
                while (System.nanoTime() < end) Thread.onSpinWait();
                frames++;
            } else {
                IO.println("[FPS]: too slow frame "+(System.nanoTime() - end)+"ns");
                frames = fpsLimit;
            }

            if (frames-fpsLimit == 0) {
                if (COUNT_FRAMES) System.out.println("[FPS]: "+timeForOneFrame+"ns "+(System.currentTimeMillis()/1_000)%1_000+"s");
                frames = 0;
            }
            start = System.nanoTime();
        }
        GLFW.destroyWindow(window);
        GLFW.terminate();
    }
    public void preRender() {
        GL.clearColor(1f, 1f, 1f, 1f);
    }
    public void render() {
        GL.clear(0x00004000);
        // System.out.println("clear color is: "+frames+" "+max+" = "+((float) frames)/(float)max);
    }
    public void update(float delta)  {
    }

}
