package dev.levia.jimi.glfw;

import dev.levia.jimi.outcast.glfw3.GLFW;

import java.lang.annotation.Native;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.Linker;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public class Window {
    private MethodHandle name;
    public void name() {}

    public Window() {
        GLFW.init();

        GLFW.createWindow(300, 300, MemorySegment.allocate("Hello World"), MemorySegment.NULL, MemorySegment.NULL);
    }
    public void example(int x, int y, String title) {
        Linker l = Linker.nativeLinker();
        MethodHandle strlen = l.downcallHandle(
                l.defaultLookup().find("strlen").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS)
        );
        try (Arena arena = Arena.ofConfined()) {
            var str = arena.allocateFrom("Hello Explorer");
            var len = (long) strlen.invoke(str);
            System.out.println("len is: "+len);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
