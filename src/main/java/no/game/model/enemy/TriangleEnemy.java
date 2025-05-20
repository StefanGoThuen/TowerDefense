package no.game.model.enemy;

import java.util.List;
import no.grid.CellPosition;

/**
 * A TriangleEnemy with higher speed but lower health.
 * Moves along a path with given health and speed.
 */
public class TriangleEnemy extends Enemy {

    /**
     * Constructs a TriangleEnemy with the specified path, health, and speed.
     *
     * @param path   The path for the enemy to follow.
     * @param health The initial and maximum health of the enemy.
     * @param speed  The movement speed of the enemy.
     */
    public TriangleEnemy(List<CellPosition> path, double health, double speed) {
        super(path, health, speed);
    }
}
