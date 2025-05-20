package no.game.model.enemy;

import java.util.List;
import no.grid.CellPosition;

/**
 * A enemy with higher health but lower speed.
 */
public class CircleEnemy extends Enemy {

    /**
     * Constructs a CircleEnemy with the specified path, health, and speed.
     *
     * @param path   The path for the enemy to follow.
     * @param health The initial and maximum health of the enemy.
     * @param speed  The movement speed of the enemy.
     */
    public CircleEnemy(List<CellPosition> path, double health, double speed) {
        super(path, health, speed);
    }

}
