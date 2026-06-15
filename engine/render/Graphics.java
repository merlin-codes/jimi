package dev.levia.jimi.engine.render;

import dev.levia.jimi.engine.win.Window;

public class Graphics {
    public Graphics() {
    }
    public interface DrawText {
        void drawText(String text, Window.P pos);
        int loadFont(String path); // loads font into memory as texture
        String buildFonts(String font);
    }
    public interface Draw2D {
        void rectangle(double x, double y, double w, double h);
        void triangle(int x, int y, int x2, int y2, int x3, int y3);
    }
    public interface Draw3D {
        void loadGLB(String path);
        void at(int whatId, float x, float y, float z);
    }
}
