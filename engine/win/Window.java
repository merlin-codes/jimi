package dev.levia.jimi.engine.win;

import dev.levia.jimi.engine.lang.Calls;
import dev.levia.jimi.util.trans.GLFW;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static dev.levia.jimi.util.trans.GLFW.I32;

/**
 * Window is simply wrapper around original GLFW library
 */
public class Window extends Thread {
    public record P(short width, short height) {
        @Override
        public String toString() {
            return String.format("P {width: %d, height: %d}", width, height);
        }
    }
    public static final byte FULLSCREEN_WINDOW = 0x0001, MAXIMIZED_WINDOW = 0x0002, RESIZABLE = 0x0004, BORDER = 0x0008, MAXIMIZED_RESIZABLE = RESIZABLE+MAXIMIZED_WINDOW;
    public static final P P720 = new P((short)1280, (short)720), P1080 = new P((short)1920, (short) 1080);

    public final byte activeFlag;
    public final Arena alloc;
    public final Long FPS = 1_000_000_000L/60L;
    public final MemorySegment windowId;
    public P size;
    public MemorySegment pWidth = MemorySegment.NULL, pHeight = MemorySegment.NULL;

    public Window(String name, P size, byte flag) { this(name, size.width, size.height, flag); }
    public Window(String name, short width, short height, byte flag) {
        if (GLFW.init() == 0) throw new RuntimeException("Couldn't init GLFW");
        alloc = Arena.ofConfined();
        pWidth = alloc.allocate(I32);
        pHeight = alloc.allocate(I32);
        this.activeFlag = flag;
        var monitor = MemorySegment.NULL;
        var title = alloc.allocateFrom(name);
        if ((flag & RESIZABLE) == RESIZABLE) GLFW.windowHint(GLFW.RESIZABLE, 1);
        if ((flag & MAXIMIZED_WINDOW) == MAXIMIZED_WINDOW) GLFW.windowHint(GLFW.MAXIMIZED, 1);
        if ((flag & FULLSCREEN_WINDOW) == FULLSCREEN_WINDOW) monitor = GLFW.getPrimaryMonitor();
        windowId = GLFW.createWindow(width, height, title, monitor, MemorySegment.NULL);
        resize(windowId, width, height);
        GLFW.makeContextCurrent(windowId);
        GLFW.setWindowSizeCallback(windowId, Calls.of(this, "resize", MemorySegment.class, int.class, int.class));
        if (windowId == MemorySegment.NULL) {
            GLFW.terminate();
            throw new RuntimeException("Window couldn't be created using GLFW");
        }
        init();
    }
    public void resize(MemorySegment n, int w, int h) {
        var size = new P((short) w,(short) h);
        // System.out.println("changing view: " + this.size + " => " + size);
        this.size = size;
    }

    @Override @SuppressWarnings("BusyWait")
    public void run() {
        // init threads one is for object manipulation, and it should spawn
        // one for each heavy task, like sometimes
        var start = System.nanoTime();
        var end = 0L;
        while (GLFW.windowShouldClose(windowId) == 0) {
            GLFW.pollEvents();
            // this is dumb way of doing this, probably need to fix it
            this.update(FPS.floatValue());
            end = (start + FPS - System.nanoTime())/1_000_000;
            // System.out.println("Thread time is: "+end);
            try {
                if (end > 0f) Thread.sleep(end);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt(); break;
            }
            GLFW.swapBuffers(this.windowId);
            start = System.nanoTime();

        }
        GLFW.destroyWindow(windowId);
        GLFW.terminate();
    }
    public void update(float delta) { }
    public void init() {}
}
