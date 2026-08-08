package dev.levia.jimi.editor;

import java.util.function.Consumer;

// defines three different ways
public class MainMenu {
    /** * generates all text menus */
    public void render() {}

    /** * add submenu to menu (on render is automatically handled by Main Menu to display etc.) */
    public void subMenu() {}
    public <T> void button(String text, Consumer<T> target, T object) {
    }
}
