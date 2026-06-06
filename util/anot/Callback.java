package dev.levia.jimi.util.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.lang.model.type.TypeKind;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Callback {
    TypeKind[] value();
}
