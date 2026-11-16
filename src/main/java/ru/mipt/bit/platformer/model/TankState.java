package ru.mipt.bit.platformer.model;


public interface TankState {
    float cellTravelTimeFactor();

    float shootIntervalFactor();

    boolean canShoot();

    default TankState afterHealthChange(int health, int initialHealth) {
        return HealthStates.of(health, initialHealth);
    }
}
