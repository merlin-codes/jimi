package dev.levia.jimi.util.trans;

import java.lang.foreign.AddressLayout;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.lang.reflect.InvocationTargetException;

public class NativeHelper {
    public static final ValueLayout.OfInt I32 = (ValueLayout.OfInt) Linker.nativeLinker().canonicalLayouts().get("int");
    public static final ValueLayout.OfLong I64 = (ValueLayout.OfLong) Linker.nativeLinker().canonicalLayouts().get("long");
    public static final ValueLayout.OfByte U8 = (ValueLayout.OfByte) Linker.nativeLinker().canonicalLayouts().get("char");
    public static final ValueLayout.OfBoolean BOOL = (ValueLayout.OfBoolean) Linker.nativeLinker().canonicalLayouts().get("boolean");
    public static final AddressLayout ADDRESS = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("void*");
    public static final ValueLayout.OfFloat F32 = (ValueLayout.OfFloat) Linker.nativeLinker().canonicalLayouts().get("float");
    public static final ValueLayout.OfDouble F64 = (ValueLayout.OfDouble) Linker.nativeLinker().canonicalLayouts().get("double");
    public static final AddressLayout ADDRESS_CHAR = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("char*");

    public interface StructureInC {
        MemoryLayout SIZE();
        default int bytes() {
            var fields = this.getClass().getRecordComponents();
            var counter = 0;
            for (var field : fields) {
                if (field.getType() == int.class) counter += 4;
                else if (field.getType() == long.class) counter += 8;
                else if (field.getType() == MemorySegment.class) counter += 8;
                else {
                    throw new IllegalArgumentException(this.getClass().getName()+"."+field.getName()+" of record is not of implemented type.");
                }
            }
            return counter;
        }
    }

    /**
     * creates new allocated Memory for Java -> C
     */
    public static MemorySegment buildStruct(SegmentAllocator allocator, StructureInC target) throws IllegalAccessException, InvocationTargetException {
        var pStruct = allocator.allocate(target.SIZE());
        var fields = target.getClass().getRecordComponents();
        var counter = 0;
        for (var field : fields) {
            var type = field.getType();
            if (type == int.class) {
                pStruct.set(I32, 4, (int) field.getAccessor().invoke(target));
            } else if (type == MemoryLayout.class) {
                pStruct.set(ADDRESS, 8, (MemorySegment) field.getAccessor().invoke(target));
            } else if (type == long.class) {
                pStruct.set(I64, 8, (long) field.getAccessor().invoke(target));
            } else {
                throw new IllegalAccessException("Accessing field that are not yet implemented in struct builder");
            }
        }
        return pStruct;
    }

    /**
     * Applies MemorySegment to Record C->Java
     */
    public static<T extends StructureInC> T getStruct(MemorySegment segment, Class<T> target) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        var fields = target.getRecordComponents();
        var values = new Object[fields.length];
        var types = new Class<?>[fields.length];
        var counter = 0;
        for (int i=0;i< fields.length;i++) {
            types[i] = fields[i].getType();
            if (types[i] == int.class) {
                values[i] = segment.get(I32, counter);
                counter += 4;
            } else if (types[i] == MemoryLayout.class) {
                values[i] = segment.get(ADDRESS, counter);
                counter += 8;
            } else if (types[i] == long.class) {
                values[i] = segment.get(I64, counter);
                counter += 8;
            } else {
                throw new IllegalAccessException("Accessing field that are not yet implemented in struct builder");
            }
        }
        return target.getConstructor(types).newInstance(values);
    }

    public record VkAppInfo(
            long sType,
            MemorySegment pNext,
            MemorySegment pApplicationName,
            int applicationVersion,
            MemorySegment pEngineName,
            int engineVersion,
            int apiVersion
    ) implements StructureInC {
        public MemoryLayout SIZE() {
            return MemoryLayout.structLayout(
                    I64.withName("sType"),
                    ADDRESS.withName("pNext"),
                    ADDRESS.withName("pApplicationName"),
                    I64.withName("applicationVersion"),
                    ADDRESS.withName("pEngineName"),
                    I32.withName("engineVersion"),
                    I32.withName("apiVersion"));
        }
    }
    public record VkSurfaceKHR() implements StructureInC {
        @Override
        public MemoryLayout SIZE() {
            return MemoryLayout.structLayout(
            );
        }
    }
    public record VkInstanceCreateInfo(
        long sType,
        MemorySegment pNext,
        int flags,
        MemorySegment pApplicationInfo,
        int enabledLayerCount,
        MemorySegment ppEnabledLayerNames,
        int enabledExtensionCount,
        MemorySegment ppEnabledExtensionNames
    ) implements StructureInC {
        public MemoryLayout SIZE() {
            return MemoryLayout.structLayout(
                    I64.withName("sType"),
                    ADDRESS.withName("pNext"),
                    I64.withName("flags"),
                    ADDRESS.withName("pApplicationInfo"),
                    I64.withName("enabledLayerCount"),
                    ADDRESS.withName("ppEnabledLayerNames"),
                    I64.withName("enabledExtensionCount"),
                    ADDRESS.withName("ppEnabledExtensionNames"));
        }
    }
    public record GLFWVidMode(int width, int height, int redBits, int greenBits, int blueBits, int refreshRate) implements StructureInC {
        public MemoryLayout SIZE() {
            return MemoryLayout.structLayout(
                    I32.withName("width"),
                    I32.withName("height"),
                    I32.withName("redBits"),
                    I32.withName("greenBits"),
                    I32.withName("blueBits"),
                    I32.withName("refreshRate")
            );
        }
    }

    public static class SmartStructBuilder {
    }
}
