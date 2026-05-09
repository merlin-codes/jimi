package dev.levia.jimi.util.trans;

import dev.levia.jimi.util.anot.Library;
import dev.levia.jimi.util.anot.NoPrefix;

import java.lang.foreign.MemorySegment;

/**
 * this is simple library Proxy just moving on so its not overhead
 */
@Library(name = "libglfw.so", prefix = "glfw")
public interface IGLFW {
    int init();
    void initHint(int hint, int value);
    MemorySegment createWindow(int width, int height, MemorySegment title, MemorySegment monitor, MemorySegment share);
    int windowShouldClose(MemorySegment window);
    void destroyWindow(MemorySegment window);
    void pollEvents();
    void makeContextCurrent(MemorySegment window);
    void swapInterval(int interval);
    void swapBuffers(MemorySegment window);
    void terminate();
    void getFramebufferSize(MemorySegment monitor, MemorySegment width, MemorySegment height);
    MemorySegment getPrimaryMonitor();

    void getCursorPos(MemorySegment window, MemorySegment pX, MemorySegment pY);
    void getWindowSize(MemorySegment window, MemorySegment pWidth, MemorySegment pHeight);
}
