package dev.levia.jimi.engine.shaders;

public class ShaderProgram {
    protected static String makeShader(String global, String main) { return "#Version 330 core\n"+global+"\n\nvoid main() {\n\t"+main+"\n}"; }

    public static final String vertexShader = makeShader( "layout (location = 0) in vec3 aPos;", "gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);" );
    public static final String fragmentShader = makeShader( "out vec4 FragColor;", "FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f); // Orange!" );
}
