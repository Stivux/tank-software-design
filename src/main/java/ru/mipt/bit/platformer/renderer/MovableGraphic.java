package ru.mipt.bit.platformer.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.util.TileMovement;

public class MovableGraphic implements EntityGraphic {
    private final Movable movable;
    private final GraphicObject graphics;
    private final TileMovement tileMovement;

    public MovableGraphic(Movable movable, GraphicObject graphics, TileMovement tileMovement) {
        this.movable = movable;
        this.graphics = graphics;
        this.tileMovement = tileMovement;
    }

    @Override
    public void draw(Batch batch) {
        graphics.interpolate(
                tileMovement,
                movable.getCoordinates(),
                movable.getDestinationCoordinates(),
                movable.getMovementProgress()
        );
        graphics.draw(batch, movable.getRotation());
    }
}
