package dev.levia.jimi.util.proces;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import dev.levia.jimi.util.anot.CanFail;
import dev.levia.jimi.util.anot.Library;
import dev.levia.jimi.util.anot.NoPrefix;

@SupportedAnnotationTypes("dev.levia.jimi.util.anot.Library")
@SupportedSourceVersion(SourceVersion.RELEASE_25)
public class LibraryProcessor extends AbstractProcessor {
    private static final String ValueLayoutsString =
            "\tpublic static final ValueLayout.OfInt I32 = (ValueLayout.OfInt) Linker.nativeLinker().canonicalLayouts().get(\"int\");\n"
            +"\tpublic static final ValueLayout.OfLong I64 = (ValueLayout.OfLong) Linker.nativeLinker().canonicalLayouts().get(\"long\");\n"
            +"\tpublic static final ValueLayout.OfByte U8 = (ValueLayout.OfByte) Linker.nativeLinker().canonicalLayouts().get(\"char\");\n"
            +"\tpublic static final ValueLayout.OfBoolean BOOL = (ValueLayout.OfBoolean) Linker.nativeLinker().canonicalLayouts().get(\"boolean\");\n"
            +"\tpublic static final AddressLayout ADDRESS = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get(\"void*\");\n"
            +"\tpublic static final ValueLayout.OfFloat F32 = (ValueLayout.OfFloat) Linker.nativeLinker().canonicalLayouts().get(\"float\");\n"
            +"\tpublic static final ValueLayout.OfDouble F64 = (ValueLayout.OfDouble) Linker.nativeLinker().canonicalLayouts().get(\"double\");\n"
            +"\tpublic static final AddressLayout ADDRESS_CHAR = (AddressLayout) Linker.nativeLinker().canonicalLayouts().get(\"char*\");\n\n";
    private static final String imports = "import java.lang.foreign.*;\nimport java.lang.invoke.MethodHandle;\nimport java.util.Optional;\n";
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // output.append("import java.lang.foreign.Linker;\n\n");

        // output.append("public class Impl").append(name).append(" implements ").append(name).append("{\n");
        var pack = processingEnv.getElementUtils();
        for (var type : roundEnv.getElementsAnnotatedWith(Library.class)) {
            if (type.getKind() == ElementKind.INTERFACE) {
                var element = (TypeElement) type;
                var packName = pack.getPackageOf(element).toString();
                var _anot = type.getAnnotation(Library.class);
                assert _anot != null;
                var prefix = _anot.prefix();
                var os = System.getProperty("os.name");
                var library = _anot.name()+(Objects.equals(os, "win") ? ".dll" : Objects.equals(os, "mac") ? ".dylib": ".so");
                var name = element.getSimpleName().toString().substring(1);
                var output = new StringBuilder("package "+packName+";\n\n");

                output.append(imports).append("public class ").append(name).append(" {\n").append(ValueLayoutsString);

                if (library.isBlank()) output.append("\tprivate static SymbolLookup lib = SymbolLookup.loaderLookup();\n")
                        .append("\tprotected final static Arena allocator = Arena.ofShared();\n");
                else output.append("\tprivate static SymbolLookup lib = SymbolLookup.libraryLookup(\"").append(library).append("\", Arena.global());\n\n");

                for (var field : ElementFilter.fieldsIn(element.getEnclosedElements())) {
                    output.append("\tpublic static ").append(field.asType().toString())
                            .append(" ").append(field.getSimpleName()).append(" = ")
                            .append(field.getConstantValue()).append(";\n");
                }
                output.append("\n");
                var canFail = type.getAnnotation(CanFail.class) != null;
                for (var method : ElementFilter.methodsIn(element.getEnclosedElements())) {
                    if (method.getKind() == ElementKind.METHOD){
                        var params = method.getParameters();
                        var simpleName = method.getSimpleName().toString();
                        var prefixName = simpleName.substring(0, 1).toUpperCase()+simpleName.substring(1);
                        var returnType = method.getReturnType();
                        var isVoid = returnType.getKind() == TypeKind.VOID;

                        if (method.getAnnotation(NoPrefix.class) == null) prefixName = prefix+prefixName;
                        if (method.getModifiers().contains(Modifier.STATIC)) {
                            // helper function are just passed down
                            output.append("\tpublic static "+returnType+" "+simpleName).append("(");
                            output.append(params.stream()
                                    .map(arg -> arg.asType().toString()+" "+arg.getSimpleName())
                                    .collect(Collectors.joining(", ")) );
                            output.append(") {");
                            if (!isVoid) output.append("return ");
                            output.append(element.getSimpleName()).append(".").append(simpleName).append("(")
                                    .append(params.stream().map(VariableElement::getSimpleName).collect(Collectors.joining(", ")))
                                    .append(");}\n");
                            continue;
                        }


                        output.append("\tprivate static final MethodHandle ").append(prefixName);
                        output.append(" = Linker.nativeLinker().downcallHandle(lib.findOrThrow(\"").append(prefixName)
                                .append("\"), FunctionDescriptor.of");

                        if (isVoid) output.append("Void(");
                        else {
                            output.append("("+getLayout(returnType.getKind()));
                            if (!method.getParameters().isEmpty()) output.append(", ");
                        }

                        for (VariableElement argument : method.getParameters())
                            output.append(getLayout(argument.asType().getKind()) + ", ");
                        if (!method.getParameters().isEmpty()) output.deleteCharAt(output.length()-2);

                        output.append("));\n");

                        output.append("\tpublic static ").append(returnType).append(" ").append(simpleName).append("(");
                        output.append(params.stream()
                                .map(arg -> arg.asType().toString()+" "+arg.getSimpleName())
                                .collect(Collectors.joining(", ")) );
                        var last = canFail || method.getAnnotation(CanFail.class) != null;
                        if (last) {
                            output.append(") { try { ");
                        } else {
                            output.append(") throws Throwable {");
                        }
                        if(!isVoid) output.append(" return (").append(returnType).append(") ");
                        output.append(prefixName).append(".invokeExact(")
                                .append(params.stream().map(VariableElement::getSimpleName).collect(Collectors.joining(", "))).append(");");
                        if (last){
                            output.append("} catch (Throwable e) {");
                            output.append(" e.printStackTrace(); ");
                            if (returnType.getKind().isPrimitive()) output.append(" return 0;");
                            else if (!isVoid) output.append("return MemorySegment.NULL;");
                            output.append("}");
                        }
                        output.append("}\n\n");
                        // minified
                    }
                }

                output.append("}");
                // System.out.println(output.toString());
                try {
                    var file = processingEnv.getFiler().createSourceFile(packName+"."+name);
                    var out = new PrintWriter(file.openWriter());
                    out.write(output.toString());
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }
    public static String getWrapperOption(TypeKind kind) {
        return switch (kind) {
            case INT -> "Integer";
            case LONG -> "Long";
            case BOOLEAN -> "Boolean";
            case CHAR -> "Character";
            case FLOAT -> "Float";
            case DOUBLE -> "Double";
            default -> "ADDRESS";
        };
    }
    public static String getLayout(TypeKind kind) {
        return switch (kind) {
            case INT -> "I32";
            case LONG -> "I64";
            case BOOLEAN -> "BOOL";
            case CHAR -> "U8";
            case FLOAT -> "F32";
            case DOUBLE -> "F64";
            default -> "ADDRESS";
        };
    }
}
