package no.game.model.tower;

import no.grid.CellPosition;

/**
 * A tower that slows down enemies instead of dealing damage.
 */
public class SlowTower extends Tower {

    /**
     * Constructs a SlowTower.
     *
     * @param position The position of the tower.
     */
    public SlowTower(CellPosition position) {
        super(position, 5, 0, 4); // range 5, no damage, cooldown 4 ticks
    }

    /**
     * Returns the slow amount applied to enemies.
     *
     * @return The slow factor (e.g., 0.7 means 30% speed reduction).
     */
    public double getSlowAmount() {
        return 0.7;
    }

    /**
     * Returns the duration (in ticks) the slow effect lasts.
     *
     * @return The number of ticks the slow lasts.
     */
    public int getSlowDuration() {
        return 600;
    }
}
