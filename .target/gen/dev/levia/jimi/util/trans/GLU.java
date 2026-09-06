package dev.levia.jimi.util.trans;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
public class GLU {
	public static final ValueLayout.OfInt I32 = (ValueLayout.OfInt) Linker.nativeLinker().canonicalLayouts().get("int");
	public static final ValueLayout.OfLong I64 = (ValueLayout.OfLong) Linker.nativeLinker().canonicalLayouts().get("long");
	public static final ValueLayout.OfByte U8 = (ValueLayout.OfByte) Linker.nativeLinker().canonicalLayouts().get("char");
	public static final ValueLayout.OfBoolean BOOL = (ValueLayout.OfBoolean) Linker.nativeLinker().canonicalLayouts().get("boolean");
	public static final AddressLayout ADDRESS = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("void*");
	public static final ValueLayout.OfFloat F32 = (ValueLayout.OfFloat) Linker.nativeLinker().canonicalLayouts().get("float");
	public static final ValueLayout.OfDouble F64 = (ValueLayout.OfDouble) Linker.nativeLinker().canonicalLayouts().get("double");
	public static final AddressLayout ADDRESS_CHAR = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("char*");

	private static SymbolLookup lib = SymbolLookup.libraryLookup("libGLU.so", Arena.global());


	private static final MethodHandle gluLookAt = Linker.nativeLinker().downcallHandle(lib.findOrThrow("gluLookAt"), FunctionDescriptor.ofVoid(F64, F64, F64, F64, F64, F64, F64, F64, F64 ));
	public static void lookAt(double eyeX, double eyeY, double eyeZ, double viewX, double viewY, double viewZ, double upX, double upY, double upZ) { try { gluLookAt.invokeExact(eyeX, eyeY, eyeZ, viewX, viewY, viewZ, upX, upY, upZ);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle gluPerspective = Linker.nativeLinker().downcallHandle(lib.findOrThrow("gluPerspective"), FunctionDescriptor.ofVoid(F64, F64, F64, F64 ));
	public static void perspective(double angle, double rotation, double x, double y) { try { gluPerspective.invokeExact(angle, rotation, x, y);} catch (Throwable e) { e.printStackTrace(); }}

}