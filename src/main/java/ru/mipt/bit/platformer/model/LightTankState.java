package ru.mipt.bit.platformer.model;

public final class LightTankState implements TankState {
    public static final LightTankState INSTANCE = new LightTankState();

    private LightTankState() {
    }

    @Override
    public float cellTravelTimeFactor() {
        return 1f;
    }

    @Override
    public float shootIntervalFactor() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
