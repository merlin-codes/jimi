package dev.levia.jimi.engine.lang;

/**
 * object that are accepted as not changing,
 * engine is managing them and if they are needed to see they
 * are rendered
 */
public class Structure {
    private Transform transform = new Transform();
    /**
     * this should be only if defined will be loaded by OpenGL at specified position
     */
    private String gltf;
    public void render() {
        // render
    }
}
