package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Random;

/** Adapter from random AI moves to {@link Controller}. */
public final class AiTankController implements Controller {
    private final Tank tank;
    private final Random random;

    public AiTankController(Tank tank, Random random) {
        this.tank = tank;
        this.random = random;
    }

    @Override
    public void update() {
        Direction[] directions = Direction.values();
        int choice = random.nextInt(directions.length + 1);
        if (choice < directions.length) {
            new MoveTankAction(tank, directions[choice]).execute();
        } else {
            new ShootTankAction(tank).execute();
        }
    }
}
