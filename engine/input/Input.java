package dev.levia.jimi.engine.input;

import dev.levia.jimi.util.trans.Call;
import dev.levia.jimi.util.trans.GLFW;
import java.lang.foreign.MemorySegment;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

// Simply put this class saves state of input and makes it possible to not
// need to communicate between Java and C just to get this values
public class Input {
    public static List<ClickCallback> clickCallbacks = new CopyOnWriteArrayList<>();
    public static List<KeyCallback> keyCallbacks = new CopyOnWriteArrayList<>();
    public static List<MoveCallback> moveCallbacks = new CopyOnWriteArrayList<>();

    private MemorySegment window = MemorySegment.NULL;

    public Input(MemorySegment window) {
        try {
            GLFW.setKeyCallback( window, Call.get( Input.class.getMethod( "onKey", MemorySegment.class, int.class, int.class, int.class, int.class ) ) );
            GLFW.setCursorPosCallback( window, Call.get( Input.class.getMethod( "onMouseMove", MemorySegment.class, double.class, double.class ) ) );
            GLFW.setMouseButtonCallback( window, Call.get( Input.class.getMethod( "onClick", MemorySegment.class, int.class, int.class, int.class ) ) );
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void onClick( MemorySegment window, int button, int action, int mods ) {
        clickCallbacks.forEach(callback -> callback.accept(button, action, mods) );
    }

    public static void onKey( MemorySegment window, int key, int scancode, int action, int mods ) {
        keyCallbacks.forEach(callback -> callback.accept(key, scancode, action, mods) );
    }

    public static void onMouseMove(MemorySegment window, double x, double y) {
        moveCallbacks.forEach(callback -> callback.accept(x, y));
    }

    @FunctionalInterface
    public interface ClickCallback {
        void accept(int button, int action, int mods);
    }

    @FunctionalInterface
    public interface KeyCallback {
        void accept(int key, int scancode, int action, int mods);
    }

    @FunctionalInterface
    public interface MoveCallback {
        void accept(double x, double y);
    }

    public static final int SPACE = 32, APOSTROPHE = 39, COMMA = 44, MINUS = 45, PERIOD = 46, SLASH = 47, SEMICOLON = 59, EQUAL = 61, A = 65, B = 66, C = 67, D = 68, E = 69, F = 70, G = 71, H = 72, I = 73, J = 74, K = 75, L = 76, M = 77, N = 78, O = 79, P = 80, Q = 81, R = 82, S = 83, T = 84, U = 85, V = 86, W = 87, X = 88, Y = 89, Z = 90, _0 = 48, _1 = 49, _2 = 50, _3 = 51, _4 = 52, _5 = 53, _6 = 54, _7 = 55, _8 = 56, _9 = 57, KP_0 = 320, KP_1 = 321, KP_2 = 322, KP_3 = 323, KP_4 = 324, KP_5 = 325, KP_6 = 326, KP_7 = 327, KP_8 = 328, KP_9 = 329, LEFT_BRACKET = 91, BACKSLASH = 92, RIGHT_BRACKET = 93, GRAVE_ACCENT = 96, WORLD_1 = 161, WORLD_2 = 162, ESCAPE = 256, ENTER = 257, TAB = 258, BACKSPACE = 259, INSERT = 260, DELETE = 261, RIGHT = 262, LEFT = 263, DOWN = 264, UP = 265, PAGE_UP = 266, PAGE_DOWN = 267, HOME = 268, END = 269, CAPS_LOCK = 280, SCROLL_LOCK = 281, NUM_LOCK = 282, PRINT_SCREEN = 283, PAUSE = 284, F1 = 290, F2 = 291, F3 = 292, F4 = 293, F5 = 294, F6 = 295, F7 = 296, F8 = 297, F9 = 298, F10 = 299, F11 = 300, F12 = 301, F13 = 302, F14 = 303, F15 = 304, F16 = 305, F17 = 306, F18 = 307, F19 = 308, F20 = 309, F21 = 310, F22 = 311, F23 = 312, F24 = 313, F25 = 314, KP_DECIMAL = 330, KP_DIVIDE = 331, KP_MULTIPLY = 332, KP_SUBTRACT = 333, KP_ADD = 334, KP_ENTER = 335, KP_EQUAL = 336, LEFT_SHIFT = 340, LEFT_CONTROL = 341, LEFT_ALT = 342, LEFT_SUPER = 343, RIGHT_SHIFT = 344, RIGHT_CONTROL = 345, RIGHT_ALT = 346, RIGHT_SUPER = 347, MENU = 348;
    public static final int PRESS = 1, RELEASE = 0, REPEAT = 2;
    public static final int SHIFT_BIT = 1, CONTROL_BIT = 2, ALT_BIT = 4, SUPER_BIT = 8, CAPS_LOCK_BIT = 16, NUM_LOCK_BIT = 32;
}
