package dev.levia.jimi.util.trans;

import java.lang.foreign.AddressLayout;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Call {
    public static final ValueLayout.OfInt I32 = (ValueLayout.OfInt) Linker.nativeLinker().canonicalLayouts().get("int");
    public static final ValueLayout.OfLong I64 = (ValueLayout.OfLong) Linker.nativeLinker().canonicalLayouts().get("long");
    public static final ValueLayout.OfByte U8 = (ValueLayout.OfByte) Linker.nativeLinker().canonicalLayouts().get("char");
    public static final ValueLayout.OfBoolean BOOL = (ValueLayout.OfBoolean) Linker.nativeLinker().canonicalLayouts().get("boolean");
    public static final AddressLayout ADDRESS = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("void*");
    public static final ValueLayout.OfFloat F32 = (ValueLayout.OfFloat) Linker.nativeLinker().canonicalLayouts().get("float");
    public static final ValueLayout.OfDouble F64 = (ValueLayout.OfDouble) Linker.nativeLinker().canonicalLayouts().get("double");
    public static final AddressLayout ADDRESS_CHAR = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("char*");

    private static Linker link = Linker.nativeLinker();
    private static Arena arena = Arena.ofShared();

    // FIXME probably something wrong it had before two arguments one Method call and MemoryLayout... arguments
    // but in the method call is now use only the first
    public static MemorySegment get(Method call) {
        var argument = Arrays.stream(call.getParameters()).map(i -> switch(i.getType().getName()) {
            case "int", "java.lang.Integer" -> I32;
            case "long", "java.lang.Long" -> I64;
            case "float", "java.lang.Float" -> F32;
            case "double", "java.lang.Double" -> F64;
            // TODO maybe default should be converting to MemorySegment and then using it
            default -> ADDRESS;
        }).toArray(MemoryLayout[]::new);
        try {
            var desc = FunctionDescriptor.ofVoid(argument);
            var handle = MethodHandles.lookup().unreflect(call);
            return link.upcallStub(handle, desc, arena);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
