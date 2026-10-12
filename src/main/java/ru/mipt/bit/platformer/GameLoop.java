package ru.mipt.bit.platformer;

import ru.mipt.bit.platformer.controller.Controller;
import ru.mipt.bit.platformer.model.Updatable;
import ru.mipt.bit.platformer.renderer.Renderer;

import java.util.List;

public final class GameLoop {
    private final Renderer renderer;
    private final List<Controller> controllers;
    private final List<Updatable> updatables;

    public GameLoop(Renderer renderer, List<Controller> controllers, List<Updatable> updatables) {
        this.renderer = renderer;
        this.controllers = controllers;
        this.updatables = updatables;
    }

    public void tick(float deltaTime) {
        for (Controller controller : controllers) {
            controller.update();
        }
        renderer.render();
        for (Updatable updatable : updatables) {
            updatable.update(deltaTime);
        }
    }

    public void dispose() {
        renderer.dispose();
    }
}
