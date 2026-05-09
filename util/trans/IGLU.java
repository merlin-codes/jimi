package dev.levia.jimi.util.trans;

import dev.levia.jimi.util.anot.Library;

@Library(prefix = "glu", name = "libGLU.so")
public interface IGLU {
    void lookAt(
            float eyeX, float eyeY, float eyeZ,
            float viewX, float viewY, float viewZ,
            float upX, float upY, float upZ
    );
    void perspective(float angle, double rotation, float x, float y);
}
