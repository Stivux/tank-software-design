package ru.mipt.bit.platformer.model;

public final class HealthStates {
    private HealthStates() {
    }

    public static TankState of(int health, int initialHealth) {
        if (initialHealth <= 0) {
            return HeavyTankState.INSTANCE;
        }
        long percent = (long) health * 100L;
        if (percent > (long) initialHealth * 70L) {
            return LightTankState.INSTANCE;
        }
        if (percent >= (long) initialHealth * 15L) {
            return MediumTankState.INSTANCE;
        }
        return HeavyTankState.INSTANCE;
    }
}
