package ru.mipt.bit.platformer.model;

public final class MediumTankState implements TankState {
    public static final MediumTankState INSTANCE = new MediumTankState();

    private MediumTankState() {
    }

    @Override
    public float cellTravelTimeFactor() {
        return 2f;
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
