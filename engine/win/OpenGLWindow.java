package dev.levia.jimi.engine.win;

import dev.levia.jimi.engine.input.FileManager;
import dev.levia.jimi.engine.render.gl.ImGUI;
import dev.levia.jimi.util.trans.GL;
import dev.levia.jimi.util.trans.GLFW;

public class OpenGLWindow extends Window {
    public OpenGLWindow(String name, P size, byte flag) { super(name, size, flag); }

    @Override
    public void init() {
        ImGUI.font("./assets/fonts/RoughenCornerRegular-7RjV.ttf", alloc);
        System.out.println("Font is:"+ImGUI.fontImageSize+" "+ImGUI.fontImageSize.width()/FileManager.text.length());
        GLFW.swapBuffers(windowId);
        GL.clearColor(0f, 0f, 0f, 1f);
    }

    @Override
    public void update(float delta) {
        GL.clear(GL.GL_COLOR_BUFFER_BIT);
        GL.matrixMode(GL.GL_PROJECTION);
        GL.loadIdentity();
        GL.ortho(0, size.width(), size.height(), 0, -1, 1);
        GL.matrixMode(GL.GL_MODELVIEW);
        GL.loadIdentity();

        GL.pushMatrix();

        GL.translatef(100f, 200f, 0f);
        ImGUI.label("some thing i dont care about");

        GL.popMatrix();

        GL.disable(GL.GL_TEXTURE_2D);
    }
}
