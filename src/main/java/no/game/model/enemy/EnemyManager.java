package no.game.model.enemy;

import no.grid.CellPosition;
import java.util.*;

/**
 * Manages a collection of enemies in the game.
 * EnemyManager handles updating enemy positions and
 * provides access to the list of active enemies and their path.
 */
public class EnemyManager {
    private List<IEnemy> enemies = new ArrayList<>();
    private List<CellPosition> path;

    /**
     * Creates a new EnemyManager with a specified movement path for
     * enemies.
     *
     * @param path The list of CellPosition that enemies will
     *             follow.
     *             Must not be null.
     * @throws IllegalArgumentException if path is null.
     */
    public EnemyManager(List<CellPosition> path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null.");
        }
        this.path = path;
    }

    /**
     * Updates all managed enemies by moving them along their path. 
     * Should be called once per game tick.
     */
    public void update() {
        for (IEnemy enemy : enemies) {
            enemy.move();
        }
    }

    /**
     * Returns the list of all active enemies.
     *
     * @return A modifiable List of IEnemy objects.
     */
    public List<IEnemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the movement path that enemies follow.
     *
     * @return A List of CellPosition representing the path.
     */
    public List<CellPosition> getPath() {
        return path;
    }

    /**
     * Adds a new enemy to be managed.
     *
     * @param enemy The IEnemy to add.
     *              Must not be null.
     * @throws IllegalArgumentException if enemy is null.
     */
    public void addEnemy(IEnemy enemy) {
        if (enemy == null) {
            throw new IllegalArgumentException("Enemy cannot be null.");
        }
        enemies.add(enemy);
    }
}
