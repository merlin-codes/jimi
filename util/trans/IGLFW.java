package dev.levia.jimi.util.trans;

import java.lang.foreign.MemorySegment;

import dev.levia.jimi.util.anot.Library;
import dev.levia.jimi.util.anot.NoPrefix;

/**
 * this is simple library Proxy just moving on so its not overhead
 */
@Library(name = "libglfw.so", prefix = "glfw")
public interface IGLFW {
    final static int MAXIMIZED = 0x00020003, RESIZABLE = 0x00020003, DECORATED = 0x00020005;

    int init();
    void initHint(int hint, int value);
    void windowHint(int hint, int value);
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
    double getTime();
    void maximizeWindow(MemorySegment window);
    void setWindowAttrib(MemorySegment window, int attrib, int value);
    MemorySegment getVideoMode(MemorySegment monitor);
    void setKeyCallback(MemorySegment window, MemorySegment callback);
    void setCursorPosCallback(MemorySegment window, MemorySegment callback);
    void setMouseButtonCallback(MemorySegment window, MemorySegment callback);
    void setScrollCallback(MemorySegment window, MemorySegment callback);
    void setWindowSizeCallback(MemorySegment window, MemorySegment callback);
    int getMouseButton(MemorySegment window, int button);
    void setDropCallback(MemorySegment window, MemorySegment callback);

    void setWindowMonitor(MemorySegment window, MemorySegment monitor, int xpos, int ypos, int width, int height, int refreshRate);
    void setClipboardString(MemorySegment window, MemorySegment string);
    MemorySegment getClipboardString(MemorySegment window);
}
