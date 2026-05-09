package dev.levia.jimi.editor;

import dev.levia.jimi.engine.lang.World;
import dev.levia.jimi.engine.render.TriangleWindow;

public class Editor {
    public static void main(String[] args) {
        // load
        // need to init Vulkan/OpenGL renderer
        // translate object etc. to renderer to render it
        try {
            var editor = new Editor();
            editor.run();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    public void run() throws Throwable {
        var path = System.getProperty("java.library.path");
        IO.println(path);
        // var glfw = new World();
        // glfw.createWindow(60);
        var window = new TriangleWindow();
        window.run();
    }
}
