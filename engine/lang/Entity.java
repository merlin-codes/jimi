package dev.levia.jimi.engine.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * This is renderable object, that needs to be managed by code,
 * so changes must be possible to control from Java
 */
public class Entity {
    public List<Component> components = new ArrayList<>();

    public Entity() {
        components.add(new Transform());
    }
    public void update(float delta) {
        components.forEach(component -> component.update(delta));
    }
}
