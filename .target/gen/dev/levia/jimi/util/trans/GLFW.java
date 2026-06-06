package dev.levia.jimi.util.trans;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
public class GLFW {
	public static final ValueLayout.OfInt I32 = (ValueLayout.OfInt) Linker.nativeLinker().canonicalLayouts().get("int");
	public static final ValueLayout.OfLong I64 = (ValueLayout.OfLong) Linker.nativeLinker().canonicalLayouts().get("long");
	public static final ValueLayout.OfByte U8 = (ValueLayout.OfByte) Linker.nativeLinker().canonicalLayouts().get("char");
	public static final ValueLayout.OfBoolean BOOL = (ValueLayout.OfBoolean) Linker.nativeLinker().canonicalLayouts().get("boolean");
	public static final AddressLayout ADDRESS = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("void*");
	public static final ValueLayout.OfFloat F32 = (ValueLayout.OfFloat) Linker.nativeLinker().canonicalLayouts().get("float");
	public static final ValueLayout.OfDouble F64 = (ValueLayout.OfDouble) Linker.nativeLinker().canonicalLayouts().get("double");
	public static final AddressLayout ADDRESS_CHAR = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("char*");

	private static SymbolLookup lib = SymbolLookup.libraryLookup("libglfw.so", Arena.global());

	public static int GLFW_MAXIMIZED = 131075;

	private static final MethodHandle glfwInit = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwInit"), FunctionDescriptor.of(I32));
	public static int init() { try {  return (int) glfwInit.invokeExact();} catch (Throwable e) { e.printStackTrace();  return 0;}}

	private static final MethodHandle glfwInitHint = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwInitHint"), FunctionDescriptor.ofVoid(I32, I32 ));
	public static void initHint(int hint, int value) { try { glfwInitHint.invokeExact(hint, value);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwWindowHint = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwWindowHint"), FunctionDescriptor.ofVoid(I32, I32 ));
	public static void windowHint(int hint, int value) { try { glfwWindowHint.invokeExact(hint, value);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwCreateWindow = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwCreateWindow"), FunctionDescriptor.of(ADDRESS, I32, I32, ADDRESS, ADDRESS, ADDRESS ));
	public static java.lang.foreign.MemorySegment createWindow(int width, int height, java.lang.foreign.MemorySegment title, java.lang.foreign.MemorySegment monitor, java.lang.foreign.MemorySegment share) { try {  return (java.lang.foreign.MemorySegment) glfwCreateWindow.invokeExact(width, height, title, monitor, share);} catch (Throwable e) { e.printStackTrace(); return MemorySegment.NULL;}}

	private static final MethodHandle glfwWindowShouldClose = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwWindowShouldClose"), FunctionDescriptor.of(I32, ADDRESS ));
	public static int windowShouldClose(java.lang.foreign.MemorySegment window) { try {  return (int) glfwWindowShouldClose.invokeExact(window);} catch (Throwable e) { e.printStackTrace();  return 0;}}

	private static final MethodHandle glfwDestroyWindow = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwDestroyWindow"), FunctionDescriptor.ofVoid(ADDRESS ));
	public static void destroyWindow(java.lang.foreign.MemorySegment window) { try { glfwDestroyWindow.invokeExact(window);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwPollEvents = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwPollEvents"), FunctionDescriptor.ofVoid());
	public static void pollEvents() { try { glfwPollEvents.invokeExact();} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwMakeContextCurrent = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwMakeContextCurrent"), FunctionDescriptor.ofVoid(ADDRESS ));
	public static void makeContextCurrent(java.lang.foreign.MemorySegment window) { try { glfwMakeContextCurrent.invokeExact(window);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwSwapInterval = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSwapInterval"), FunctionDescriptor.ofVoid(I32 ));
	public static void swapInterval(int interval) { try { glfwSwapInterval.invokeExact(interval);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwSwapBuffers = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSwapBuffers"), FunctionDescriptor.ofVoid(ADDRESS ));
	public static void swapBuffers(java.lang.foreign.MemorySegment window) { try { glfwSwapBuffers.invokeExact(window);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwTerminate = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwTerminate"), FunctionDescriptor.ofVoid());
	public static void terminate() { try { glfwTerminate.invokeExact();} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwGetFramebufferSize = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwGetFramebufferSize"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS, ADDRESS ));
	public static void getFramebufferSize(java.lang.foreign.MemorySegment monitor, java.lang.foreign.MemorySegment width, java.lang.foreign.MemorySegment height) { try { glfwGetFramebufferSize.invokeExact(monitor, width, height);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwGetPrimaryMonitor = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwGetPrimaryMonitor"), FunctionDescriptor.of(ADDRESS));
	public static java.lang.foreign.MemorySegment getPrimaryMonitor() { try {  return (java.lang.foreign.MemorySegment) glfwGetPrimaryMonitor.invokeExact();} catch (Throwable e) { e.printStackTrace(); return MemorySegment.NULL;}}

	private static final MethodHandle glfwGetCursorPos = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwGetCursorPos"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS, ADDRESS ));
	public static void getCursorPos(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment pX, java.lang.foreign.MemorySegment pY) { try { glfwGetCursorPos.invokeExact(window, pX, pY);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwGetWindowSize = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwGetWindowSize"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS, ADDRESS ));
	public static void getWindowSize(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment pWidth, java.lang.foreign.MemorySegment pHeight) { try { glfwGetWindowSize.invokeExact(window, pWidth, pHeight);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwGetTime = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwGetTime"), FunctionDescriptor.of(F64));
	public static double getTime() { try {  return (double) glfwGetTime.invokeExact();} catch (Throwable e) { e.printStackTrace();  return 0;}}

	private static final MethodHandle glfwMaximizeWindow = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwMaximizeWindow"), FunctionDescriptor.ofVoid(ADDRESS ));
	public static void maximizeWindow(java.lang.foreign.MemorySegment window) { try { glfwMaximizeWindow.invokeExact(window);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwSetWindowAttrib = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSetWindowAttrib"), FunctionDescriptor.ofVoid(ADDRESS, I32, I32 ));
	public static void setWindowAttrib(java.lang.foreign.MemorySegment window, int attrib, int value) { try { glfwSetWindowAttrib.invokeExact(window, attrib, value);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwGetVideoMode = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwGetVideoMode"), FunctionDescriptor.of(ADDRESS, ADDRESS ));
	public static java.lang.foreign.MemorySegment getVideoMode(java.lang.foreign.MemorySegment monitor) { try {  return (java.lang.foreign.MemorySegment) glfwGetVideoMode.invokeExact(monitor);} catch (Throwable e) { e.printStackTrace(); return MemorySegment.NULL;}}

	private static final MethodHandle glfwSetKeyCallback = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSetKeyCallback"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS ));
	public static void setKeyCallback(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment callback) { try { glfwSetKeyCallback.invokeExact(window, callback);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwSetCursorPosCallback = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSetCursorPosCallback"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS ));
	public static void setCursorPosCallback(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment callback) { try { glfwSetCursorPosCallback.invokeExact(window, callback);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwSetMouseButtonCallback = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSetMouseButtonCallback"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS ));
	public static void setMouseButtonCallback(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment callback) { try { glfwSetMouseButtonCallback.invokeExact(window, callback);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwSetScrollCallback = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSetScrollCallback"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS ));
	public static void setScrollCallback(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment callback) { try { glfwSetScrollCallback.invokeExact(window, callback);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwSetWindowSizeCallback = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSetWindowSizeCallback"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS ));
	public static void setWindowSizeCallback(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment callback) { try { glfwSetWindowSizeCallback.invokeExact(window, callback);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwGetMouseButton = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwGetMouseButton"), FunctionDescriptor.of(I32, ADDRESS, I32 ));
	public static int getMouseButton(java.lang.foreign.MemorySegment window, int button) { try {  return (int) glfwGetMouseButton.invokeExact(window, button);} catch (Throwable e) { e.printStackTrace();  return 0;}}

	private static final MethodHandle glfwSetDropCallback = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSetDropCallback"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS ));
	public static void setDropCallback(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment callback) { try { glfwSetDropCallback.invokeExact(window, callback);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwSetWindowMonitor = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSetWindowMonitor"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS, I32, I32, I32, I32, I32 ));
	public static void setWindowMonitor(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment monitor, int xpos, int ypos, int width, int height, int refreshRate) { try { glfwSetWindowMonitor.invokeExact(window, monitor, xpos, ypos, width, height, refreshRate);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwSetClipboardString = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwSetClipboardString"), FunctionDescriptor.ofVoid(ADDRESS, ADDRESS ));
	public static void setClipboardString(java.lang.foreign.MemorySegment window, java.lang.foreign.MemorySegment string) { try { glfwSetClipboardString.invokeExact(window, string);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glfwGetClipboardString = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glfwGetClipboardString"), FunctionDescriptor.of(ADDRESS, ADDRESS ));
	public static java.lang.foreign.MemorySegment getClipboardString(java.lang.foreign.MemorySegment window) { try {  return (java.lang.foreign.MemorySegment) glfwGetClipboardString.invokeExact(window);} catch (Throwable e) { e.printStackTrace(); return MemorySegment.NULL;}}

	private static final MethodHandle GlfwModelReader = Linker.nativeLinker().downcallHandle(lib.findOrThrow("GlfwModelReader"), FunctionDescriptor.of(ADDRESS));
	public static java.lang.foreign.MemorySegment GlfwModelReader() { try {  return (java.lang.foreign.MemorySegment) GlfwModelReader.invokeExact();} catch (Throwable e) { e.printStackTrace(); return MemorySegment.NULL;}}

}