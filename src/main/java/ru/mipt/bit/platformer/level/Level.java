package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Updatable;
import ru.mipt.bit.platformer.model.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Level implements World, Updatable {
    private final int width;
    private final int height;
    private final GridPoint2 playerStart;
    private final List<GameObject> obstacles;
    private final List<GameObject> objects = new ArrayList<GameObject>();
    private final List<LevelListener> listeners = new ArrayList<LevelListener>();

    public Level(int width, int height, GridPoint2 playerStart, List<? extends GameObject> obstacles) {
        this.width = width;
        this.height = height;
        this.playerStart = new GridPoint2(playerStart);
        this.obstacles = Collections.unmodifiableList(new ArrayList<GameObject>(obstacles));
    }

    public void addListener(LevelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void add(GameObject object) {
        objects.add(object);
        for (LevelListener listener : new ArrayList<LevelListener>(listeners)) {
            listener.onAdded(object);
        }
    }

    @Override
    public void remove(GameObject object) {
        if (!objects.remove(object)) {
            return;
        }
        for (LevelListener listener : new ArrayList<LevelListener>(listeners)) {
            listener.onRemoved(object);
        }
    }

    @Override
    public boolean inBounds(GridPoint2 cell) {
        return cell.x >= 0 && cell.y >= 0 && cell.x < width && cell.y < height;
    }

    @Override
    public List<GameObject> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    @Override
    public void update(float deltaTime) {
        for (GameObject object : new ArrayList<GameObject>(objects)) {
            if (objects.contains(object)) {
                object.update(deltaTime);
            }
        }
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
