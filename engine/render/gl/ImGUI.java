package dev.levia.jimi.engine.render.gl;

import dev.levia.jimi.engine.input.FileManager;
import dev.levia.jimi.util.trans.NativeHelper;
import dev.levia.jimi.engine.win.Window;
import dev.levia.jimi.util.trans.GL;

import java.awt.*;
import java.lang.foreign.Arena;
import java.lang.foreign.ValueLayout;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

import static dev.levia.jimi.engine.input.FileManager.*;

public class ImGUI {
    static final StringBuilder buffer = new StringBuilder(500);
    public static void font(String path, Arena alloc) {
        var image = imageFromFont(path, 20f);
        var fontId = alloc.allocate(NativeHelper.I32);
        GL.genTextures(1, fontId);
        ImGUI.fontId = fontId.get(NativeHelper.I32, 0);
        GL.bindTexture(GL.GL_TEXTURE_2D, ImGUI.fontId);

        GL.texParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, 9987);
        GL.texParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, 9729);
        int width = image.getWidth(), height = image.getHeight();
        fontImageSize = new Window.P((short)width, (short)height);

        var pixels = new int[width*height];
        pixels = image.getRGB(0, 0, width, height, pixels, 0, width);
        var buff = alloc.allocate((long) width * height * 4);
        long offset = 0;
        for (int pixel : pixels) {
            buff.set(ValueLayout.JAVA_BYTE, offset++, (byte) (pixel >> 16 & 0xff));
            buff.set(ValueLayout.JAVA_BYTE, offset++, (byte) (pixel >> 8 & 0xff));
            buff.set(ValueLayout.JAVA_BYTE, offset++, (byte) (pixel & 0xff));
            buff.set(ValueLayout.JAVA_BYTE, offset++, (byte) (pixel >> 24 & 0xff));
        }
        GL.texImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buff);
        GL.generateMipmap(GL.GL_TEXTURE_2D);
    }
    public static int fontId = 0;
    public static Window.P fontImageSize;
    public static int LastID = 0;
    public static int lastPossibleClickedId = 0;
    public static final float[]  quad_vert = { -0.5f,  0.5f,  0.0f, 1.0f, -0.5f, -0.5f,  0.0f, 0.0f, 0.5f,  0.5f,  1.0f, 1.0f, 0.5f, -0.5f,  1.0f, 0.0f  };

    public record ClickButton(int id, int x, int y, int x2, int y2) {
        public boolean clicked(int mouseX, int mouseY) {
            return (x < mouseX && x2 > mouseX) && (y < mouseY && y2 > mouseY);}
    }
    public static void startMenu() {}

    public static void label(Object... text) {
        label(Color.BLACK, text);
    }
    public static void label(Color color, Object... text) {
        buffer.setLength(0);
        GL.bindTexture(GL.GL_TEXTURE_2D, fontId);
        var font = FileManager.text.toCharArray();
        var fw = 1f/font.length;
        var fontWidth = fontImageSize.width()/font.length;
        GL.begin(GL.GL_QUADS);


        for (var t : text) {
            var str = t.toString().toCharArray();
            var size = (float) str.length;
            for (float i = 0f; i < size; i++) {
                var u = fw * FileManager.text.indexOf(str[(int) i]);
                var width = i*fontWidth;
                System.out.printf("{%f, %d, %f, %d}", u, 1, width, fontImageSize.height());
                System.out.printf("{%f, %d, %f, %d}", u, 0, width, 0);
                System.out.printf("{%f, %d, %f, %d}", u+fw, 0, width+fontWidth, 0);
                System.out.printf("{%f, %d, %f, %d}\n", u+fw, 0, width+fontWidth, fontImageSize.height());

                GL.texCoord2f(u, 1); GL.vertex2f(width, fontImageSize.height());
                GL.texCoord2f(u, 0); GL.vertex2f(width, 0);
                GL.texCoord2f(u+fw, 0); GL.vertex2f(width+fontWidth, 0);
                GL.texCoord2f(u+fw, 1); GL.vertex2f(width+fontWidth, fontImageSize.height());
            }
        }
        var result = buffer.toString();
        GL.end();
    }
    public static void inputText(AtomicReference<String> input, boolean focus) {
        // check if is focus
    }
    public static void select(String[] options, AtomicReference<Byte> value) {}
    public static void button(String text, BooleanSupplier callback) { }
    public static void sliderFloat(AtomicReference<Float> pointer, float from, float to) {}
}
