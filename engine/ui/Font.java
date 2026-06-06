package dev.levia.jimi.engine.ui;

public record Font(
        int width,
        int height,
        String characters,
        int pointer
) {
    public int charWidth() {
        return width / (characters.length()-1);
    }

    @Override
    public String toString() {
        return String.format("Font {w: %d, h: %d, characters: %s, pointer: %s}", width, height, characters, pointer);
    }
}
