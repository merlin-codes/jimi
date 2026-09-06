package dev.levia.jimi.engine.win;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import dev.levia.jimi.util.trans.GLFW;
import dev.levia.jimi.util.trans.Vulkan;

public class VulkanWindow extends Window {

    public MemorySegment vkInstance = MemorySegment.NULL;
    public MemorySegment vkSurface = MemorySegment.NULL;

    public VulkanWindow(String name, P size, byte flag) throws Throwable {
        super(name, size, flag);
    }

    @Override
    public void init() throws Throwable {
        // 1. Tell GLFW NOT to initialize an OpenGL context (Required for Vulkan)
        GLFW.windowHint(GLFW.CLIENT_API, GLFW.NO_API);

        // 2. Query required GLFW extensions
        var extensionCountPtr = alloc.allocate(GLFW.I32);
        var extensionsPtr = GLFW.getRequiredInstanceExtensions(extensionCountPtr);

        // 3. Populate Application Info Struct
        var appInfo = alloc.allocate(48);
        appInfo.set(ValueLayout.JAVA_INT, 0, Vulkan.VK_STRUCTURE_TYPE_APPLICATION_INFO);
        appInfo.set(ValueLayout.ADDRESS, 8, alloc.allocateFrom("Jimi Engine"));
        appInfo.set(ValueLayout.JAVA_INT, 16, 1);
        appInfo.set(ValueLayout.ADDRESS, 24, alloc.allocateFrom("Jimi"));
        appInfo.set(ValueLayout.JAVA_INT, 32, 1);
        appInfo.set(ValueLayout.JAVA_INT, 36, (1 << 22) | (2 << 12)); // Vulkan 1.2

        // 4. Populate Instance Create Info Struct
        var createInfo = alloc.allocate(64);
        createInfo.set(ValueLayout.JAVA_INT, 0, Vulkan.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
        createInfo.set(ValueLayout.ADDRESS, 16, appInfo);

        int extCount = extensionCountPtr.get(ValueLayout.JAVA_INT, 0);
        createInfo.set(ValueLayout.JAVA_INT, 24, extCount);
        createInfo.set(ValueLayout.ADDRESS, 32, extensionsPtr);

        // 5. Create Vulkan Instance
        var pInstance = alloc.allocate(ValueLayout.ADDRESS);
        int res = Vulkan.createInstance(createInfo, MemorySegment.NULL, pInstance);
        if (res != Vulkan.VK_SUCCESS) {
            throw new RuntimeException("Failed to create Vulkan Instance, result: " + res);
        }
        vkInstance = pInstance.get(ValueLayout.ADDRESS, 0);

        // 6. Create Vulkan Surface for GLFW Window
        var pSurface = alloc.allocate(ValueLayout.ADDRESS);
        res = GLFW.createWindowSurface(vkInstance, windowId, MemorySegment.NULL, pSurface);
        if (res != Vulkan.VK_SUCCESS) {
            throw new RuntimeException("Failed to create Vulkan Window Surface, result: " + res);
        }
        vkSurface = pSurface.get(ValueLayout.ADDRESS, 0);
    }

    @Override
    public void update(float delta) {
        // Vulkan command buffer submission and swapchain presentation
        GLFW.pollEvents();
    }
}