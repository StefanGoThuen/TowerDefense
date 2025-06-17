package no.game.model.tower;

import no.game.model.Projectile;
import no.game.model.enemy.BasicEnemy;
import no.game.model.enemy.IEnemy;
import no.grid.CellPosition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SniperTowerTest {

    @Test
    public void testSniperTowerStats() {
        SniperTower tower = new SniperTower(new CellPosition(0, 0));

        assertEquals(20, tower.getRange());
        assertEquals(100, tower.getDamage(), 0.01);
        assertEquals(150, tower.getCooldown());
        assertEquals(75, tower.getCost());
    }

    @Test
    public void testSniperTowerCooldownMechanics() {
        SniperTower tower = new SniperTower(new CellPosition(0, 0));
        assertTrue(tower.canShoot());

        tower.resetCooldown();
        assertFalse(tower.canShoot());

        for (int i = 0; i < 149; i++) {
            tower.tickCooldown();
            assertFalse(tower.canShoot());
        }

        tower.tickCooldown(); // 150
        assertTrue(tower.canShoot());
    }

    @Test
    public void testSniperShootsEnemyInLongRange() {
        List<CellPosition> path = new ArrayList<>();
        SniperTower tower = new SniperTower(new CellPosition(0, 0));

        path.add(new CellPosition(0, 0));

        IEnemy enemy = new BasicEnemy(path, 100, 1.0);

        assertTrue(tower.canShoot());
        boolean inRange = Math.abs(enemy.getPosition().col() - tower.getPosition().col())
                + Math.abs(enemy.getPosition().row() - tower.getPosition().row()) <= tower.getRange();
        assertTrue(inRange);

        Projectile projectile = new Projectile(tower.getPosition(), enemy, tower.getDamage());
        assertEquals(enemy, projectile.getTarget());
        assertEquals(100, projectile.getDamage(), 0.01);
    }

    @Test
    public void testSniperCannotShootTwiceInARow() {
        SniperTower tower = new SniperTower(new CellPosition(0, 0));
        assertTrue(tower.canShoot());
        tower.resetCooldown();
        assertFalse(tower.canShoot());
    }
}
