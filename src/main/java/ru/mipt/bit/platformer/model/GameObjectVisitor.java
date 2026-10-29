package ru.mipt.bit.platformer.model;

public interface GameObjectVisitor<R> {
    R visitGameObject(GameObject object);

    default R visitMovable(Movable movable) {
        return visitGameObject(movable);
    }

    default R visitBullet(Bullet bullet) {
        return visitMovable(bullet);
    }
}
