package dev.levia.jimi.util.proces;

import dev.levia.jimi.util.anot.Callback;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes("dev.levia.jimi.util.anot.Callback")
@SupportedSourceVersion(SourceVersion.RELEASE_25)
public class CallbackProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var element : roundEnv.getElementsAnnotatedWith(Callback.class)) {
            var method = (ExecutableElement) element;
            var cb = method.getAnnotation(Callback.class);
            var params = method.getParameters();
            check(method, params, cb.value());
        }
        return true;
    }
    private void check(ExecutableElement e, List<? extends VariableElement> params, TypeKind[] kinds) {
        if (params.size() != kinds.length) processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed incorrect amount of paramaters in definition", e);
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i).asType().getKind() != kinds[i]) processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed incorrect type of parameter in function", e);
            // if (params[i].getType()?. == kinds[i])
        }
    }
}
