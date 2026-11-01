package ru.mipt.bit.platformer.model;

public interface GameObjectVisitor<R> {
    R visitGameObject(GameObject object);

    default R visitMovable(Movable movable) {
        return visitGameObject(movable);
    }
}
