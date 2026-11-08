package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Tank;

public final class ShootTankAction implements Action {
    private final Tank tank;

    public ShootTankAction(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.shoot();
    }
}
