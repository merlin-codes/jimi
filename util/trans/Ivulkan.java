package dev.levia.jimi.util.trans;

import java.lang.foreign.MemorySegment;
import dev.levia.jimi.util.anot.CanFail;
import dev.levia.jimi.util.anot.Library;
import dev.levia.jimi.util.anot.NoPrefix;

@Library(name = "libvulkan", prefix = "vk")
@CanFail
public interface Ivulkan {
    // Vulkan Constants & Result Codes
    int VK_SUCCESS = 0;
    int VK_STRUCTURE_TYPE_APPLICATION_INFO = 0;
    int VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO = 1;

    // Core Vulkan API functions
    int createInstance(MemorySegment pCreateInfo, MemorySegment pAllocator, MemorySegment pInstance);
    void destroyInstance(MemorySegment instance, MemorySegment pAllocator);
    int enumeratePhysicalDevices(MemorySegment instance, MemorySegment pPhysicalDeviceCount, MemorySegment pPhysicalDevices);

    // Custom/Extension helpers mapping directly without vk prefix if needed
    int getInstanceProcAddr(MemorySegment instance, MemorySegment pName);
}