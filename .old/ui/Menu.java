package dev.levia.jimi.engine.ui;

import dev.levia.jimi.engine.fs.FileManager;
import dev.levia.jimi.util.trans.GL;
import dev.levia.jimi.engine.render.Render;
import dev.levia.jimi.engine.ui.Font;

public class Menu<T extends Menu<T>> {
    public final int x, y, width, height;
    protected Render render;
    protected Font font;
    public boolean isFocus = false;

    public Menu(Render render, int x, int y, int width, int height) {
        this.render = render; font = render.fonts.getFirst();
        this.x = x; this.y = y; this.width = width; this.height = height;
    }
    public void draw() {
        drawRect(x, y, width, height, new SmallColor(0.4f, 0.4f, 0.4f, 1f), true);
    }
    public void drawRect(int x, int y, int w, int h, SmallColor color) { drawRect(x, y, w, h, color, false); }
    public void drawRect(int x, int y, int w, int h) { drawRect(x, y, w, h, new SmallColor(1f, 1f, 1f, 1f), false); }
    public void drawRect(int x, int y, int w, int h, SmallColor color, boolean hasBorder) {
        GL.enable(GL.GL_BLEND);
        GL.blendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        GL.color4f(color.r(), color.g(), color.b(), color.a());
        GL.begin(GL.GL_QUADS);
        GL.vertex2f(x, y);
        GL.vertex2f(x + w, y);
        GL.vertex2f(x + w, y + h);
        GL.vertex2f(x, y + h);
        GL.end();
        if (hasBorder) {
            GL.color4f(0.5f, 0.5f, 0.55f, 1f);
            GL.begin(GL.GL_LINE_LOOP);
            GL.vertex2f(x, y);
            GL.vertex2f(x + w, y);
            GL.vertex2f(x + w, y + h);
            GL.vertex2f(x, y + h);
            GL.end();
        }
        GL.disable(GL.GL_BLEND);
    }
    // user is clicking on area, this is where is decided what will happen
    public void click(int x, int y, int button) {}
    public void key(int key, boolean action, int mods) {}
    public void text(String text, int x, int y) {
        GL.enable(GL.GL_BLEND);
        GL.blendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        GL.enable(GL.GL_TEXTURE_2D);
        GL.bindTexture(GL.GL_TEXTURE_2D, font.pointer());
        GL.color3f(1f, 1f, 1f);

        float charTexWidth = 1.0f / FileManager.text.length();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int charIndex = FileManager.text.indexOf(c);
            if (charIndex < 0) charIndex = 0;

            float texX1 = charIndex * charTexWidth;
            float texX2 = texX1 + charTexWidth;

            float screenX = x + i * font.charWidth();

            GL.begin(GL.GL_QUADS);
            texVertex(texX1, 0f, screenX, y);
            texVertex(texX2, 0f, screenX + 13, y);
            texVertex(texX2, 1f, screenX + 13, y + 29);
            texVertex(texX1, 1f, screenX, y + 29);
            GL.end();
        }

        GL.bindTexture(GL.GL_TEXTURE_2D, 0);
        GL.disable(GL.GL_TEXTURE_2D);
        GL.disable(GL.GL_BLEND);

    }
    private void texVertex(float texX, float texY, float screenX, float screenY) {
        GL.texCoord2f(texX, texY);
        GL.vertex2f(screenX, screenY);
    }
}
