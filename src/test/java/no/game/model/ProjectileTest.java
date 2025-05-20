package no.game.model;

import no.game.model.enemy.BasicEnemy;
import no.grid.CellPosition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {

    @Test
    void testInitialPosition() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));
        path.add(new CellPosition(5, 5));

        BasicEnemy enemy = new BasicEnemy(path, 100, 1.0);
        Projectile projectile = new Projectile(new CellPosition(0, 0), enemy, 10.0);

        assertEquals(new CellPosition(0, 0), projectile.getPosition());
    }

    @Test
    void testTargetIsSetCorrectly() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));
        path.add(new CellPosition(2, 2));

        BasicEnemy enemy = new BasicEnemy(path, 100, 1.0);
        Projectile projectile = new Projectile(new CellPosition(0, 0), enemy, 15.0);

        assertEquals(enemy, projectile.getTarget());
    }

    @Test
    void testDamageValue() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));
        path.add(new CellPosition(2, 2));

        BasicEnemy enemy = new BasicEnemy(path, 100, 1.0);
        Projectile projectile = new Projectile(new CellPosition(0, 0), enemy, 20.0);

        assertEquals(20.0, projectile.getDamage());
    }

    @Test
    void testProjectileMovesTowardsEnemy() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(5, 5));

        BasicEnemy enemy = new BasicEnemy(path, 100, 1.0);
        Projectile projectile = new Projectile(new CellPosition(0, 0), enemy, 10.0);

        projectile.move();
        assertEquals(new CellPosition(1, 1), projectile.getPosition());

        projectile.move();
        assertEquals(new CellPosition(2, 2), projectile.getPosition());

        projectile.move();
        assertEquals(new CellPosition(3, 3), projectile.getPosition());
    }

    @Test
    void testProjectileHitsTarget() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(2, 2));

        BasicEnemy enemy = new BasicEnemy(path, 100, 1.0);
        Projectile projectile = new Projectile(new CellPosition(0, 0), enemy, 10.0);

        assertFalse(projectile.hasHitTarget());

        projectile.move();
        assertFalse(projectile.hasHitTarget());

        projectile.move();
        assertTrue(projectile.hasHitTarget());
    }

    @Test
    void testProjectileyAtTarget() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));

        BasicEnemy enemy = new BasicEnemy(path, 100, 1.0);
        Projectile projectile = new Projectile(new CellPosition(0, 0), enemy, 10.0);

        assertTrue(projectile.hasHitTarget());
    }
}
