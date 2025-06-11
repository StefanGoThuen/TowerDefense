package no.game.model.tower;

import no.grid.CellPosition;

/**
 * A basic tower with standard range, damage, and cooldown.
 */
public class BasicTower extends Tower {

    /**
     * Constructs a BasicTower.
     *
     * @param position The position of the tower.
     */
    public BasicTower(CellPosition position) {
        super(position, 4, 3, 3);
        this.cost = 50;
    }
}
