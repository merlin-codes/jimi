package dev.levia.jimi.editor;

import java.lang.foreign.MemorySegment;

public class UpCalls {
    public static Editor editor;
    public class Mouse {
        public static double x, y;
        public static boolean left, right, middle;
    }

    public static void onMouseMove(MemorySegment window, double x, double y) {
        Mouse.x = x;
        Mouse.y = y;
    }
    public static void onKey(MemorySegment window, int key, int scancode, int action, int mods) {
        if (editor != null && action == 1) {
            // Handle scene search popup keys

            //if (editor.sceneSearchOpen) {
            //    editor.handleSceneSearchKey(key, mods);
            //    return;
            //}
            //
            //// Handle property popup keys
            //if (editor.popupOpen) {
            //    editor.handlePopupKey(key, mods);
            //    return;
            //}
            //
            //// Handle keyboard shortcuts
            //// Ctrl+S to save (key 115 = 'S', mods 1 = Ctrl)
            //if (key == 115 && (mods & 1) != 0) {
            //    editor.saveScene();
            //    return;
            //}

            //// Ctrl+W to close scene (key 119 = 'W', mods 1 = Ctrl)
            //if (key == 119 && (mods & 1) != 0) {
            //    editor.closeScene();
            //    return;
            //}

            //// Shift+A to open scene search (key 65 = 'A', mods 2 = Shift)
            //if (key == 65 && (mods & 2) != 0) {
            //    editor.openSceneSearch();
            //    return;
            //}
        }
        // IO.println("key just pressed... key: "+key+" scancode: "+scancode+" action: "+action+" mods: "+mods);
    }
    public static void onMouseButton(MemorySegment window, int button, int action, int mods) {
        if (button == 1) Mouse.right = action == 1;
        else if (button == 0) Mouse.left = action == 1;
        else if (button == 2) Mouse.middle = action == 1;

        if (editor != null && action == 1 && button == 0) {
            // editor.onMouseClick(MouseMeta.x, MouseMeta.y);
        }
    }
    public static void onMouseScroll(MemorySegment window, double x, double y) {
        // IO.println("mouse wheel just moved... x: "+x+" y: "+y);
    }
    public static void onFileDrop(MemorySegment window, int count, MemorySegment paths) {
        // IO.println("files just dropped... count: "+count);
    }
}
