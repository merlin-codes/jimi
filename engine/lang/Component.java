package dev.levia.jimi.engine.lang;

/**
 * Some information needs to exist but not need to send it GPU,
 * simply put its just something that actually doesn't exist and
 * is made by programmer for saving something like gravity
 * - Gravity Concept: is simply applying force to specific entities
 *&
 * they are interfaces are limited to be simply used by update method
 */
public interface Component {
    void update(float delta);
}
