package no.game.model.enemy;

import no.grid.CellPosition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {

    @Test
    public void testEnemyHealthNotNegative() {
        Enemy enemy = new BasicEnemy(List.of(new CellPosition(0, 0)), 50, 0.1);

        enemy.takeDamage(30);
        enemy.takeDamage(30);

        assertEquals(0, enemy.getHealth(), 0.01);
    }

    @Test
    public void testEnemyTakesDamageAndDies() {
        Enemy enemy = new BasicEnemy(List.of(new CellPosition(0, 0)), 50, 0.1);
        enemy.takeDamage(20);
        assertFalse(enemy.isDead());
        enemy.takeDamage(30);
        assertTrue(enemy.isDead());
    }

    @Test
    public void testEnemyMovement() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));
        path.add(new CellPosition(1, 0)); 

        IEnemy enemy = new BasicEnemy(path, 100, 1.0);

        assertEquals(new CellPosition(0, 0), enemy.getPosition());

        enemy.move();

        assertEquals(new CellPosition(1, 0), enemy.getPosition());
    }

    @Test
    public void testEnemySlowing() {
        Enemy enemy = new BasicEnemy(List.of(new CellPosition(0, 0)), 50, 1.0);
        enemy.applySlow(1.0, 3);
        assertEquals(1.0, enemy.getSpeed(), 0.01);
        enemy.move();
    }
}
