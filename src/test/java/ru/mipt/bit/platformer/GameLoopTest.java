package ru.mipt.bit.platformer;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.controller.Controller;
import ru.mipt.bit.platformer.model.Updatable;
import ru.mipt.bit.platformer.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameLoopTest {

    @Test
    void tickRunsControllersThenRenderThenWorld() {
        List<String> order = new ArrayList<String>();
        GameLoop loop = new GameLoop(renderer(order), List.of(controller(order)), List.of(updatable(order)));

        loop.tick(0.25f);

        assertEquals(List.of("controller", "render", "updatable"), order);
    }

    @Test
    void disposeClosesTheRenderer() {
        List<String> order = new ArrayList<String>();
        GameLoop loop = new GameLoop(renderer(order), List.of(), List.of());

        loop.dispose();

        assertEquals(List.of("dispose"), order);
    }

    private static Controller controller(List<String> order) {
        return new Controller() {
            @Override
            public void update() {
                order.add("controller");
            }
        };
    }

    private static Renderer renderer(List<String> order) {
        return new Renderer() {
            @Override
            public void render() {
                order.add("render");
            }

            @Override
            public void dispose() {
                order.add("dispose");
            }
        };
    }

    private static Updatable updatable(List<String> order) {
        return new Updatable() {
            @Override
            public void update(float deltaTime) {
                order.add("updatable");
            }
        };
    }
}
