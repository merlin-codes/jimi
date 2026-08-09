package dev.levia.jimi.util.trans;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
public class GL {
	public static final ValueLayout.OfInt I32 = (ValueLayout.OfInt) Linker.nativeLinker().canonicalLayouts().get("int");
	public static final ValueLayout.OfLong I64 = (ValueLayout.OfLong) Linker.nativeLinker().canonicalLayouts().get("long");
	public static final ValueLayout.OfByte U8 = (ValueLayout.OfByte) Linker.nativeLinker().canonicalLayouts().get("char");
	public static final ValueLayout.OfBoolean BOOL = (ValueLayout.OfBoolean) Linker.nativeLinker().canonicalLayouts().get("boolean");
	public static final AddressLayout ADDRESS = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("void*");
	public static final ValueLayout.OfFloat F32 = (ValueLayout.OfFloat) Linker.nativeLinker().canonicalLayouts().get("float");
	public static final ValueLayout.OfDouble F64 = (ValueLayout.OfDouble) Linker.nativeLinker().canonicalLayouts().get("double");
	public static final AddressLayout ADDRESS_CHAR = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get("char*");

	private static SymbolLookup lib = SymbolLookup.libraryLookup("libGL.so", Arena.global());

	public static int GL_COLOR_BUFFER_BIT = 16384;
	public static int GL_DEPTH_BUFFER_BIT = 256;
	public static int GL_PROJECTION = 5889;
	public static int Float = 5126;
	public static int GL_MODELVIEW = 5888;
	public static int GL_TRIANGLES = 4;
	public static int GL_QUADS = 7;
	public static int GL_QUAD_STRIP = 8;
	public static int GL_DEPTH_TEST = 2929;
	public static int GL_LINES = 1;
	public static int GL_LINE_LOOP = 2;
	public static int GL_TEXTURE_2D = 3553;
	public static int GL_RGBA = 6408;
	public static int GL_BGRA = 32993;
	public static int GL_UNSIGNED_BYTE = 5121;
	public static int GL_LINEAR = 9729;
	public static int GL_CLAMP_TO_EDGE = 33071;
	public static int GL_TEXTURE_MAG_FILTER = 10240;
	public static int GL_TEXTURE_MIN_FILTER = 10241;
	public static int GL_TEXTURE_WRAP_S = 10242;
	public static int GL_TEXTURE_WRAP_T = 10243;
	public static int GL_TEXTURE_COORD_ARRAY = 32888;
	public static int GL_BLEND = 3042;
	public static int GL_SRC_ALPHA = 770;
	public static int GL_ONE_MINUS_SRC_ALPHA = 771;
	public static int GL_TRIANGLE_STRIP = 5;
	public static java.lang.foreign.MemorySegment NULL = null;

	private static final MethodHandle glViewport = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glViewport"), FunctionDescriptor.ofVoid(I32, I32, I32, I32 ));
	public static void viewport(int x, int y, int x1, int y1) { try { glViewport.invokeExact(x, y, x1, y1);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glClearColor = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glClearColor"), FunctionDescriptor.ofVoid(F32, F32, F32, F32 ));
	public static void clearColor(float r, float g, float b, float a) { try { glClearColor.invokeExact(r, g, b, a);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glClear = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glClear"), FunctionDescriptor.ofVoid(I32 ));
	public static void clear(int bitMask) { try { glClear.invokeExact(bitMask);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glGenVertexArrays = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glGenVertexArrays"), FunctionDescriptor.ofVoid(I32, ADDRESS ));
	public static void genVertexArrays(int index, java.lang.foreign.MemorySegment memory) { try { glGenVertexArrays.invokeExact(index, memory);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glGenBuffers = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glGenBuffers"), FunctionDescriptor.ofVoid(I32, ADDRESS ));
	public static void genBuffers(int index, java.lang.foreign.MemorySegment memory) { try { glGenBuffers.invokeExact(index, memory);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glBindVertexArray = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glBindVertexArray"), FunctionDescriptor.ofVoid(I32 ));
	public static void bindVertexArray(int index) { try { glBindVertexArray.invokeExact(index);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glBindBuffer = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glBindBuffer"), FunctionDescriptor.ofVoid(I32, I32 ));
	public static void bindBuffer(int index, int buffer) { try { glBindBuffer.invokeExact(index, buffer);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glBufferData = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glBufferData"), FunctionDescriptor.ofVoid(I32, I64, ADDRESS, I32 ));
	public static void bufferData(int index, long i, java.lang.foreign.MemorySegment memory, int j) { try { glBufferData.invokeExact(index, i, memory, j);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glEnableVertexAttribArray = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glEnableVertexAttribArray"), FunctionDescriptor.ofVoid(I32 ));
	public static void enableVertexAttribArray(int flag) { try { glEnableVertexAttribArray.invokeExact(flag);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glCreateShader = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glCreateShader"), FunctionDescriptor.of(I32, I32 ));
	public static int createShader(int programType) { try {  return (int) glCreateShader.invokeExact(programType);} catch (Throwable e) { e.printStackTrace();  return 0;}}

	private static final MethodHandle glShaderSource = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glShaderSource"), FunctionDescriptor.ofVoid(I32, I32, ADDRESS, ADDRESS ));
	public static void shaderSource(int x, int y, java.lang.foreign.MemorySegment a, java.lang.foreign.MemorySegment b) { try { glShaderSource.invokeExact(x, y, a, b);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glCompileShader = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glCompileShader"), FunctionDescriptor.ofVoid(I32 ));
	public static void compileShader(int index) { try { glCompileShader.invokeExact(index);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glCreateProgram = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glCreateProgram"), FunctionDescriptor.of(I32));
	public static int createProgram() { try {  return (int) glCreateProgram.invokeExact();} catch (Throwable e) { e.printStackTrace();  return 0;}}

	private static final MethodHandle glAttachShader = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glAttachShader"), FunctionDescriptor.ofVoid(I32, I32 ));
	public static void attachShader(int program, int index) { try { glAttachShader.invokeExact(program, index);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glLinkProgram = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glLinkProgram"), FunctionDescriptor.ofVoid(I32 ));
	public static void linkProgram(int program) { try { glLinkProgram.invokeExact(program);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glUseProgram = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glUseProgram"), FunctionDescriptor.ofVoid(I32 ));
	public static void useProgram(int program) { try { glUseProgram.invokeExact(program);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glDrawArrays = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glDrawArrays"), FunctionDescriptor.ofVoid(I32, I32, I32 ));
	public static void drawArrays(int x, int y, int z) { try { glDrawArrays.invokeExact(x, y, z);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glMatrixMode = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glMatrixMode"), FunctionDescriptor.ofVoid(I32 ));
	public static void matrixMode(int mode) { try { glMatrixMode.invokeExact(mode);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glFrustum = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glFrustum"), FunctionDescriptor.ofVoid(F64, F64, F64, F64, F64, F64 ));
	public static void frustum(double left, double right, double bottom, double top, double zNear, double zFar) { try { glFrustum.invokeExact(left, right, bottom, top, zNear, zFar);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glLoadIdentity = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glLoadIdentity"), FunctionDescriptor.ofVoid());
	public static void loadIdentity() { try { glLoadIdentity.invokeExact();} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glTranslatef = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glTranslatef"), FunctionDescriptor.ofVoid(F32, F32, F32 ));
	public static void translatef(float x, float y, float z) { try { glTranslatef.invokeExact(x, y, z);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glScalef = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glScalef"), FunctionDescriptor.ofVoid(F32, F32, F32 ));
	public static void scalef(float x, float y, float z) { try { glScalef.invokeExact(x, y, z);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glBegin = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glBegin"), FunctionDescriptor.ofVoid(I32 ));
	public static void begin(int what) { try { glBegin.invokeExact(what);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glColor3f = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glColor3f"), FunctionDescriptor.ofVoid(F32, F32, F32 ));
	public static void color3f(float r, float b, float g) { try { glColor3f.invokeExact(r, b, g);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glVertex3f = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glVertex3f"), FunctionDescriptor.ofVoid(F32, F32, F32 ));
	public static void vertex3f(float x, float y, float z) { try { glVertex3f.invokeExact(x, y, z);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glEnd = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glEnd"), FunctionDescriptor.ofVoid());
	public static void end() { try { glEnd.invokeExact();} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glRotatef = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glRotatef"), FunctionDescriptor.ofVoid(F32, F32, F32, F32 ));
	public static void rotatef(float x, float y, float z, float angle) { try { glRotatef.invokeExact(x, y, z, angle);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glEnable = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glEnable"), FunctionDescriptor.ofVoid(I32 ));
	public static void enable(int cap) { try { glEnable.invokeExact(cap);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glDisable = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glDisable"), FunctionDescriptor.ofVoid(I32 ));
	public static void disable(int cap) { try { glDisable.invokeExact(cap);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glOrtho = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glOrtho"), FunctionDescriptor.ofVoid(F64, F64, F64, F64, F64, F64 ));
	public static void ortho(double left, double right, double bottom, double top, double zNear, double zFar) { try { glOrtho.invokeExact(left, right, bottom, top, zNear, zFar);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glVertex2f = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glVertex2f"), FunctionDescriptor.ofVoid(F32, F32 ));
	public static void vertex2f(float x, float y) { try { glVertex2f.invokeExact(x, y);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glTexCoord2f = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glTexCoord2f"), FunctionDescriptor.ofVoid(F32, F32 ));
	public static void texCoord2f(float s, float t) { try { glTexCoord2f.invokeExact(s, t);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glGenTextures = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glGenTextures"), FunctionDescriptor.ofVoid(I32, ADDRESS ));
	public static void genTextures(int n, java.lang.foreign.MemorySegment textures) { try { glGenTextures.invokeExact(n, textures);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glBindTexture = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glBindTexture"), FunctionDescriptor.ofVoid(I32, I32 ));
	public static void bindTexture(int target, int texture) { try { glBindTexture.invokeExact(target, texture);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glTexImage2D = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glTexImage2D"), FunctionDescriptor.ofVoid(I32, I32, I32, I32, I32, I32, I32, I32, ADDRESS ));
	public static void texImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, java.lang.foreign.MemorySegment pixels) { try { glTexImage2D.invokeExact(target, level, internalFormat, width, height, border, format, type, pixels);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glTexParameteri = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glTexParameteri"), FunctionDescriptor.ofVoid(I32, I32, I32 ));
	public static void texParameteri(int target, int pname, int param) { try { glTexParameteri.invokeExact(target, pname, param);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glDeleteTextures = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glDeleteTextures"), FunctionDescriptor.ofVoid(I32, ADDRESS ));
	public static void deleteTextures(int n, java.lang.foreign.MemorySegment textures) { try { glDeleteTextures.invokeExact(n, textures);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glBlendFunc = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glBlendFunc"), FunctionDescriptor.ofVoid(I32, I32 ));
	public static void blendFunc(int sfactor, int dfactor) { try { glBlendFunc.invokeExact(sfactor, dfactor);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glColor4f = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glColor4f"), FunctionDescriptor.ofVoid(F32, F32, F32, F32 ));
	public static void color4f(float r, float g, float b, float a) { try { glColor4f.invokeExact(r, g, b, a);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glGenerateMipmap = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glGenerateMipmap"), FunctionDescriptor.ofVoid(I32 ));
	public static void generateMipmap(int type) { try { glGenerateMipmap.invokeExact(type);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glDrawArraysInstanced = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glDrawArraysInstanced"), FunctionDescriptor.ofVoid(I32, I32, I32, I32 ));
	public static void drawArraysInstanced(int mode, int first, int count, int instanceCount) { try { glDrawArraysInstanced.invokeExact(mode, first, count, instanceCount);} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glPushMatrix = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glPushMatrix"), FunctionDescriptor.ofVoid());
	public static void pushMatrix() { try { glPushMatrix.invokeExact();} catch (Throwable e) { e.printStackTrace(); }}

	private static final MethodHandle glPopMatrix = Linker.nativeLinker().downcallHandle(lib.findOrThrow("glPopMatrix"), FunctionDescriptor.ofVoid());
	public static void popMatrix() { try { glPopMatrix.invokeExact();} catch (Throwable e) { e.printStackTrace(); }}

}