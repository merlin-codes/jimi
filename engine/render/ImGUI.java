package dev.levia.jimi.engine.render;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * simple interface that works with others
 */
public class ImGUI {
    static final StringBuilder buffer = new StringBuilder(500);
    public static void font(String path) { }
    public int fontId = 0;
    public int lastPossibleClickedId = 0;

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
        for (var t : text) buffer.append(t.toString());
        var result = buffer.toString();
        // render with color
    }
    public static void inputText(AtomicReference<String> input, boolean focus) {
        // check if is focus
    }
    public static void select(String[] options, AtomicReference<Byte> value) {}
    public static void button(String text, BooleanSupplier callback) { }
    public static void sliderFloat(AtomicReference<Float> pointer, float from, float to) {}
}
