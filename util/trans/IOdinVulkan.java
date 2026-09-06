package dev.levia.jimi.util.trans;

import dev.levia.jimi.util.anot.Library;
import java.lang.foreign.MemorySegment;

@Library(name = "../libs/render_odin.so", prefix = "")
public interface IOdinVulkan {
    boolean init_window(int width, int height, MemorySegment title);
    long load_gltf_model(MemorySegment path);
    void set_entity_position(long id, float x, float y, float z);
    void set_entity_rotation(long id, float pitch, float yaw, float roll);
    void set_entity_scale(long id, float sx, float sy, float sz);
    boolean poll_events_and_render(float deltaTime);
    void close_window();
}
