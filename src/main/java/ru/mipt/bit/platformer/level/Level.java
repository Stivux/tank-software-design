package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Level {
    private final int width;
    private final int height;
    private final GridPoint2 playerStart;
    private final List<GameObject> obstacles;

    public Level(int width, int height, GridPoint2 playerStart, List<? extends GameObject> obstacles) {
        this.width = width;
        this.height = height;
        this.playerStart = new GridPoint2(playerStart);
        this.obstacles = Collections.unmodifiableList(new ArrayList<GameObject>(obstacles));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GridPoint2 getPlayerStart() {
        return new GridPoint2(playerStart);
    }

    public List<GameObject> getObstacles() {
        return obstacles;
    }
}
