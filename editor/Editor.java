package dev.levia.jimi.editor;

import dev.levia.jimi.engine.win.OpenGLWindow;
import dev.levia.jimi.engine.win.Window;

public class Editor {
    static void main() {
        var window = new OpenGLWindow("Jimi Editor", Window.P720, Window.MAXIMIZED_WINDOW);
        window.start();
    }
}
