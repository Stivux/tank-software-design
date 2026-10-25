package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Movable;

public final class AiTankController implements Controller {
    private final Movable tank;
    private final MoveStrategy strategy;

    public AiTankController(Movable tank, MoveStrategy strategy) {
        this.tank = tank;
        this.strategy = strategy;
    }

    @Override
    public void update() {
        new MoveTankAction(tank, strategy.chooseDirection()).execute();
    }
}
