package dev.levia.jimi.engine.win;

import dev.levia.jimi.engine.input.FileManager;
import dev.levia.jimi.engine.render.gl.ImGUI;
import dev.levia.jimi.util.trans.GL;
import dev.levia.jimi.util.trans.GLFW;

public class OpenGLWindow extends Window {
    public OpenGLWindow(String name, P size, byte flag) throws Throwable { super(name, size, flag); }

    @Override
    public void init() throws Throwable {
        GLFW.windowHint(0x00022002, 0x00032001); // GLFW_OPENGL_PROFILE = GLFW_OPENGL_COMPAT_PROFILE
        GLFW.windowHint(0x00022003, 0x00010000); // GLFW_CONTEXT_VERSION_MAJOR = 1 (or 2)
        ImGUI.font("./assets/fonts/RoughenCornerRegular-7RjV.ttf", alloc);
        System.out.println("Font is:"+ImGUI.fontImageSize+" "+ImGUI.fontImageSize.width()/FileManager.text.length());
        GLFW.swapBuffers(windowId);
        GL.clearColor(0.2f, 0.3f, 0.4f, 1f);
    }
    @Override
    public void update(float delta) {
        GL.clear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        float x = 0, y = 0, w = 20, h = 20;
        GL.begin(GL.GL_QUADS);
        GL.color3f(1.0f, 0.0f, 0.0f); // Red
        GL.vertex2f((float) x, (float) y);
        GL.vertex2f((float) (x + w), (float) y);
        GL.vertex2f((float) (x + w), (float) (y + h));
        GL.vertex2f((float) x, (float) (y + h));
        GL.end();
        GLFW.pollEvents();
        GLFW.swapBuffers(windowId);
    }
}
