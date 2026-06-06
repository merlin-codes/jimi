package dev.levia.jimi.engine.ui;

import dev.levia.jimi.engine.ai.GlbModelLoader;
import dev.levia.jimi.engine.intern.Scene;
import dev.levia.jimi.engine.lang.Transform;
import dev.levia.jimi.engine.render.Render;
import dev.levia.jimi.util.trans.GL;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * this should render 3D viewport for now
 */
public class View extends Menu {
    private List<Scene> scenes;
    private GlbModelLoader.GlbModel model;
    private Transform camera = new Transform();
    public View(Render render, int x, int y, int width, int height) {
        super(render, x, y, width, height);
        try {
            model = GlbModelLoader.load(Paths.get("assets/model.glb"));
        } catch (Exception e) {
            System.err.println("Failed to load model: " + e.getMessage());
        }
        // make possible to rotate camera based on if you press mouse and mouse position change
    }
    public void changeScene(String scenePath) {
        // TODO change world to scene path
    }

    @Override
    public void draw() {
        GL.enable(GL.GL_DEPTH_TEST);
        GL.matrixMode(GL.GL_PROJECTION);
        GL.loadIdentity();
        GL.frustum(-1.0, 1.0, -1.0, 1.0, 1.0, 100.0);
        GL.matrixMode(GL.GL_MODELVIEW);
        GL.loadIdentity();
        GL.translatef(0.0f, 0.0f, -3.0f);
        GL.rotatef(camera.rotation.x, 1.0f, 0.0f, 0.0f);
        GL.rotatef(camera.rotation.y, 0.0f, 1.0f, 0.0f);
        GL.rotatef(camera.rotation.z, 0.0f, 0.0f, 1.0f);

        model(this.model);

        GL.disable(GL.GL_DEPTH_TEST);
        camera.rotation.y += 1;
    }
    // TODO fix this, because it sucks
    public void model(GlbModelLoader.GlbModel model) {
        // GL.scalef(1f, 1f, 1f);
        // GL.translatef(-model, -model.center().y(), -model.center().z());

        // GL.begin(GL.GL_TRIANGLES);
        // GL.color3f(1.0f, 1.0f, 1.0f);
        // for (var tri : model.triangles()) {
        //     GL.vertex3f(tri.a().x(), tri.a().y(), tri.a().z());
        //     GL.vertex3f(tri.b().x(), tri.b().y(), tri.b().z());
        //     GL.vertex3f(tri.c().x(), tri.c().y(), tri.c().z());
        // }
        // GL.end();
    }
}
