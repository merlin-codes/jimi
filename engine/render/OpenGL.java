package dev.levia.jimi.engine.render;

import dev.levia.jimi.engine.win.Window;
import dev.levia.jimi.util.trans.GL;

public class OpenGL extends Graphics implements Graphics.DrawText, Graphics.Draw2D, Graphics.Draw3D {

    public OpenGL(double left, double right, double bottom, double top) {
        GL.matrixMode(GL.GL_PROJECTION);
        GL.loadIdentity();
        GL.ortho(left, right, bottom, top, -1.0, 1.0);
        GL.matrixMode(GL.GL_MODELVIEW);
        GL.clearColor(0f, 0f, 0f, 1f);
    }
    @Override
    public void rectangle(double x, double y, double w, double h) {
    }

    @Override
    public void triangle(int x, int y, int x2, int y2, int x3, int y3) {
    }

    @Override
    public void loadGLB(String path) {

    }

    @Override
    public void at(int whatId, float x, float y, float z) {

    }

    @Override
    public void drawText(String text, Window.P pos) {

    }

    @Override
    public int loadFont(String path) {
        return 0;
    }

    @Override
    public String buildFonts(String font) {
        return "";
    }
}
