package dev.levia.jimi.engine.win;

import dev.levia.jimi.engine.input.FileManager;
import dev.levia.jimi.engine.render.gl.ImGUI;
import dev.levia.jimi.util.trans.GLFW;

public class VulkanWindow extends Window {
    public VulkanWindow(String name, P size, byte flag) throws Throwable {
        super(name, size, flag);
        ImGUI.font("./assets/fonts/RoughenCornerRegular-7RjV.ttf", alloc);
        System.out.println("Font is:"+ImGUI.fontImageSize+" "+ImGUI.fontImageSize.width()/ FileManager.text.length());
        GLFW.swapBuffers(windowId);
    }
}
