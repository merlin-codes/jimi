package dev.levia.jimi.engine.ui;

import java.awt.Color;

public record SmallColor(float r, float g, float b, float a) {
    public static SmallColor from(Color color) {
        return new SmallColor(color.getRed(), color.getGreen(), color.getBlue(), 1f);
    }
}
