package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import java.util.Random;

public final class AiTankController implements Controller {
    private final Movable tank;
    private final Random random;

    public AiTankController(Movable tank, Random random) {
        this.tank = tank;
        this.random = random;
    }

    @Override
    public void update() {
        Direction[] directions = Direction.values();
        new MoveTankAction(tank, directions[random.nextInt(directions.length)]).execute();
    }
}
