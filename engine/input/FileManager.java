package dev.levia.jimi.engine.fs;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class FileManager {

    private static final GraphicsEnvironment environment =
        GraphicsEnvironment.getLocalGraphicsEnvironment();
    public static final String text =
        " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789/.,-;:(){}[]";

    public static List<String> findAllFilesEndingWith(String ending) {
        return findAllFilesEndingWith(Path.of("."), ending.toLowerCase());
    }

    public static List<String> findAllFilesEndingWith(
        Path path,
        String ending
    ) {
        var list = new ArrayList<String>();
        try {
            var files = Files.list(path);
            files.forEach(file -> {
                if (Files.isDirectory(file)) {
                    list.addAll(findAllFilesEndingWith(file, ending));
                } else if (file.toString().endsWith(ending)) {
                    list.add(file.toString());
                }
            });
            files.close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // return List.of();
    }

    public static void imageFromFont(String name, float size) {
        IO.println(name);
        try {
            // should find the ending of the filename and define FontType
            var real_path = Files.newInputStream(Path.of(name));
            var font = Font.createFont(Font.TRUETYPE_FONT, real_path);
            font = font.deriveFont(size);

            var image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            var g = image.createGraphics();
            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            var f = g.getFontRenderContext();
            var bounds = font.getStringBounds(text, f);
            int width = (int) bounds.getWidth(),
                height = (int) bounds.getHeight();

            image = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_ARGB
            );
            g = image.createGraphics();
            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            g.setFont(font);
            var color = new Color(0x00000000, true);
            g.setColor(color);
            g.fillRect(0, 0, width, height);
            g.setColor(new Color(0xFFFFFFFF, true));
            g.drawString(text, 0, (int) -bounds.getY());
            var file = new File("./" + name.trim() + ".png");
            ImageIO.write(image, "png", file);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
