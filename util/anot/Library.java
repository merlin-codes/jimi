package dev.levia.jimi.util.anot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Library {
    /**
     * this is name of library that you want to use to:
     * SymbolLookup
     */
    String name();

    /**
     * C libraries always starts with prefix
     * - this is just simple conventional helper
     * - it calls for function with prefix that is defined
     * EXAMPLE: (baka)
     * void init(); => void bakaInit();
     */
    String prefix();
}
