package dev.levia.jimi.engine.intern;

public interface IGameObject {

    /**
     * this function is only run for renderer
     * so only when object is visible by player
     */
    public void render();

    /**
     * this is first run after GL is ready to render
     * so after GLFW.init() not when object is created
     */
    public void setup();

    /**
     * this is run every frame of the game (based lock on 60 FPS)
     */
    public void update(float delta);
}
