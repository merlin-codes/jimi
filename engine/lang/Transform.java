package dev.levia.jimi.engine.lang;

public class Transform implements Component {
    public static class Vector3 {
        public float x, y, z;
        public static final Vector3 ZERO = new Vector3(0f, 0f, 0f);
        public static final Vector3 PLUS = new Vector3(1f, 1f, 1f);
        public static final Vector3 MINUS = new Vector3(-1f, -1f, -1f);
        public Vector3(float x, float y, float z) { this.x=x;this.y=y;this.z=z; }
        // maybe not use
        public void relative(float x, float y, float z) { this.x+=x; this.y+=y;this.z+=z; }
    }
    public Vector3 position = Vector3.ZERO;
    public Vector3 rotation = Vector3.ZERO;
    public Vector3 scale = Vector3.ZERO;
    public Vector3 velocity = Vector3.ZERO;

    @Override
    public void update(float delta) {
        if (velocity.x > 0.001f || velocity.x < -0.001f) position.x += velocity.x * delta;
        if (velocity.y > 0.001f || velocity.y < -0.001f) position.y += velocity.y * delta;
        if (velocity.z > 0.001f || velocity.z < -0.001f) position.z += velocity.z * delta;
    }
}
