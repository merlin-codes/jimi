package dev.levia.jimi.engine.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * There is always need to change something in world, by example
 * something happened with Structures and you want to apply it,
 * the problem is the concept of them is actually immutable.
 *&
 * But don't worry you can change Act, this is simply description
 * that is saving changes, or overriding actual existing entities
 */
public class Act {
    public List<Structure> structures = new ArrayList<>();
    public List<Entity> entities = new ArrayList<>();

    /**
     * this is overriding actual scene
     */
    public void use() {
    }
}
