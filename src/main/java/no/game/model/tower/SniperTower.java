package no.game.model.tower;

import no.grid.CellPosition;

/**
 * A sniper tower with very long range and high damage.
 */
public class SniperTower extends Tower {

    /**
     * Constructs a SniperTower.
     *
     * @param position The position of the tower.
     */
    public SniperTower(CellPosition position) {
        super(position, 20, 100, 150); // range 20, damage 100, cooldown 150 ticks
    }
}
