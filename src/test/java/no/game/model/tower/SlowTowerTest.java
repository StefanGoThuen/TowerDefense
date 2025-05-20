package no.game.model.tower;

import no.grid.CellPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SlowTowerTest {

    @Test
    public void testCooldownMechanics() {
        SlowTower tower = new SlowTower(new CellPosition(0, 0));
        assertTrue(tower.canShoot());
        tower.resetCooldown();
        assertFalse(tower.canShoot());

        tower.tickCooldown();
        tower.tickCooldown();
        tower.tickCooldown();
        tower.tickCooldown();
        assertTrue(tower.canShoot());
    }

    @Test
    public void testSlowValues() {
        SlowTower tower = new SlowTower(new CellPosition(0, 0));
        assertEquals(0.7, tower.getSlowAmount(), 0.01);
        assertEquals(600, tower.getSlowDuration());
    }
}
