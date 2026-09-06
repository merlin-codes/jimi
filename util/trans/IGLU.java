package dev.levia.jimi.util.trans;

import dev.levia.jimi.util.anot.CanFail;
import dev.levia.jimi.util.anot.Library;

@Library(prefix = "glu", name = "libGLU")
@CanFail
public interface IGLU {
    void lookAt(
            double eyeX, double eyeY, double eyeZ,
            double viewX, double viewY, double viewZ,
            double upX, double upY, double upZ
    );
    void perspective(double angle, double rotation, double x, double y);
}
