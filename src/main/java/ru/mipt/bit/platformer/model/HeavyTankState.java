package ru.mipt.bit.platformer.model;

public final class HeavyTankState implements TankState {
    public static final HeavyTankState INSTANCE = new HeavyTankState();

    private HeavyTankState() {
    }

    @Override
    public float cellTravelTimeFactor() {
        return 3f;
    }

    @Override
    public float shootIntervalFactor() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return false;
    }
}
