package dev.levia.jimi.editor;

import dev.levia.jimi.engine.win.OpenGLWindow;
import dev.levia.jimi.engine.win.VulkanWindow;
import dev.levia.jimi.engine.win.Window;

public class Editor {
    static void main() throws Throwable {
        var window = new VulkanWindow("Jimi Editor", Window.P720, Window.MAXIMIZED_WINDOW);
        window.start();
    }
}
