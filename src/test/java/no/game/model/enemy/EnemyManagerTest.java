package no.game.model.enemy;

import org.junit.jupiter.api.Test;

import no.grid.CellPosition;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class EnemyManagerTest {

    @Test
    public void testAddEnemy() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));
        EnemyManager enemyManager = new EnemyManager(path);

        IEnemy enemy = new BasicEnemy(path, 50, 0.1);

        enemyManager.addEnemy(enemy);

        assertEquals(1, enemyManager.getEnemies().size());
    }

    @Test
    public void testUpdateEnemies() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));
        path.add(new CellPosition(1, 1));
        EnemyManager enemyManager = new EnemyManager(path);

        IEnemy enemy = new BasicEnemy(path, 50, 1.0);
        enemyManager.addEnemy(enemy);

        enemyManager.update();

        assertNotEquals(new CellPosition(0, 0), enemy.getPosition());
    }

    @Test
    public void testEnemyFollowsPath() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));
        path.add(new CellPosition(0, 1));
        path.add(new CellPosition(0, 2));
        path.add(new CellPosition(1, 2));

        EnemyManager enemyManager = new EnemyManager(path);

        IEnemy enemy = new BasicEnemy(path, 50, 1.0);
        enemyManager.addEnemy(enemy);

        enemy.move();
        assertEquals(new CellPosition(0, 1), enemy.getPosition());

        enemy.move();
        assertEquals(new CellPosition(0, 2), enemy.getPosition());

        enemy.move();
        assertEquals(new CellPosition(1, 2), enemy.getPosition());
    }

}
