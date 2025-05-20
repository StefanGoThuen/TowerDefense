package no.game.model.enemy;

import java.util.List;
import no.grid.CellPosition;

/**
 * A basicEnemy with no special behavior.
 * Moves along a path with given health and speed.
 */
public class BasicEnemy extends Enemy {
    /**
     * Constructs a BasicEnemy with the specified path, health, and speed.
     *
     * @param path   The path for the enemy to follow.
     * @param health The initial and maximum health of the enemy.
     * @param speed  The movement speed of the enemy.
     */
    public BasicEnemy(List<CellPosition> path, double health, double speed) {
        super(path, health, speed);
    }
}
