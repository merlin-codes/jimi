package dev.levia.jimi.engine.lang;

import dev.levia.jimi.util.trans.Call;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandles;

public class Calls {
    public static final ValueLayout.OfInt I32 = (ValueLayout.OfInt) Linker.nativeLinker().canonicalLayouts().get("int");
    public static final ValueLayout.OfLong I64 = (ValueLayout.OfLong) Linker.nativeLinker().canonicalLayouts().get("long");
    public static final ValueLayout.OfByte U8 = (ValueLayout.OfByte) Linker.nativeLinker().canonicalLayouts().get("char");
    public static final ValueLayout.OfBoolean BOOL = (ValueLayout.OfBoolean) Linker.nativeLinker().canonicalLayouts().get("boolean");
    public static final AddressLayout ADDRESS = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("void*");
    public static final ValueLayout.OfFloat F32 = (ValueLayout.OfFloat) Linker.nativeLinker().canonicalLayouts().get("float");
    public static final ValueLayout.OfDouble F64 = (ValueLayout.OfDouble) Linker.nativeLinker().canonicalLayouts().get("double");
    public static final AddressLayout ADDRESS_CHAR = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("char*");

    public static <T>MemorySegment of(T instance, String name, Class<?>... args) {
        try {
            var method = instance.getClass().getMethod(name, args);
            var arguments = new ValueLayout[method.getParameterCount()];
            var params = method.getParameterTypes();
            for (int i = 0; i < args.length; i++) {
                arguments[i] =
                    params[i] == int.class || params[i] == Integer.class ? I32 :
                    params[i] == float.class || params[i] == Float.class ? F32 :
                    params[i] == double.class || params[i] == Double.class ? F64 :
                    params[i] == long.class || params[i] == Long.class ? I64 : ADDRESS;
            }
            var des = FunctionDescriptor.ofVoid(arguments);
            var handle = MethodHandles.lookup().bind(instance, name, des.toMethodType());
            return Linker.nativeLinker().upcallStub(handle, des, Arena.ofShared());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("trying to create callback for: "+instance.getClass().getSimpleName());
    }
}
