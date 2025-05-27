package no.game.model;

import org.junit.jupiter.api.Test;

import no.game.model.enemy.BasicEnemy;
import no.game.model.enemy.CircleEnemy;
import no.game.model.enemy.EnemyManager;
import no.game.model.enemy.IEnemy;
import no.game.model.enemy.TriangleEnemy;
import no.grid.CellPosition;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class WaveManagerTest {

    @Test
    public void testStartNewWave() {
        EnemyManager enemyManager = new EnemyManager(new ArrayList<>());
        WaveManager waveManager = new WaveManager(enemyManager);

        waveManager.startNewWave();

        assertEquals(1, waveManager.getCurrentWave());
        assertTrue(waveManager.isWaveActive());
    }

    @Test
    public void testAutoWaveProgression() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));
        path.add(new CellPosition(1, 0));
        EnemyManager enemyManager = new EnemyManager(path);

        WaveManager waveManager = new WaveManager(enemyManager);
        waveManager.startNewWave();

        double deltaTime = 2.0;

        while (waveManager.isWaveActive()) {
            waveManager.update(deltaTime);
        }

        enemyManager.getEnemies().clear();

        waveManager.handleAutoWaveProgression(deltaTime);

        assertEquals(2, waveManager.getCurrentWave());
    }

    @Test
    public void testCanStartNewWave() {
        List<CellPosition> path = new ArrayList<>();
        path.add(new CellPosition(0, 0));
        path.add(new CellPosition(1, 0));
        EnemyManager enemyManager = new EnemyManager(path);

        WaveManager waveManager = new WaveManager(enemyManager);
        waveManager.startNewWave();

        double deltaTime = 2.0;

        while (waveManager.isWaveActive()) {
            waveManager.update(deltaTime);
        }

        enemyManager.getEnemies().clear();

        assertTrue(waveManager.canStartNewWave());
    }

    @Test
    public void testStartNewWaveIncrementsValues() {
        EnemyManager enemyManager = new EnemyManager(new ArrayList<>());
        WaveManager waveManager = new WaveManager(enemyManager);

        waveManager.startNewWave();
        assertEquals(1, waveManager.getCurrentWave());

        waveManager.startNewWave();
        assertEquals(2, waveManager.getCurrentWave());
        waveManager.update(2.0);
    }

    @Test
    public void testUpdateWithNoPathSkipsSpawning() {
        EnemyManager enemyManager = new EnemyManager(new ArrayList<>());
        WaveManager waveManager = new WaveManager(enemyManager);
        waveManager.startNewWave();

        waveManager.update(5.0);

        assertEquals(0, enemyManager.getEnemies().size());
    }

    @Test
    public void testSpawnEnemyCoversAllTypes() {
        List<CellPosition> path = List.of(new CellPosition(0, 0), new CellPosition(1, 0));
        EnemyManager enemyManager = new EnemyManager(path);
        WaveManager waveManager = new WaveManager(enemyManager);

        waveManager.startNewWave();

        boolean triangleSpawned = false;
        boolean circleSpawned = false;
        boolean basicSpawned = false;

        for (int i = 0; i < 100; i++) {
            waveManager.update(2.0);
            for (IEnemy enemy : enemyManager.getEnemies()) {
                if (enemy instanceof TriangleEnemy)
                    triangleSpawned = true;
                if (enemy instanceof CircleEnemy)
                    circleSpawned = true;
                if (enemy instanceof BasicEnemy)
                    basicSpawned = true;
            }
            if (triangleSpawned && circleSpawned && basicSpawned)
                break;
        }

        assertTrue(triangleSpawned);
        assertTrue(circleSpawned);
        assertTrue(basicSpawned);
    }

    @Test
    public void testWaveDeactivatesAfterSpawningAll() {
        List<CellPosition> path = List.of(new CellPosition(0, 0), new CellPosition(1, 0));
        EnemyManager enemyManager = new EnemyManager(path);
        WaveManager waveManager = new WaveManager(enemyManager);

        waveManager.startNewWave();
        while (waveManager.isWaveActive()) {
            waveManager.update(2.0);
        }

        assertFalse(waveManager.isWaveActive());
    }

    @Test
    public void testCanStartNewWaveFalseWhenEnemiesExist() {
        List<CellPosition> path = List.of(new CellPosition(0, 0), new CellPosition(1, 0));
        EnemyManager enemyManager = new EnemyManager(path);
        enemyManager.addEnemy(new BasicEnemy(path, 50, 0.05));

        WaveManager waveManager = new WaveManager(enemyManager);

        assertFalse(waveManager.canStartNewWave());
    }

}
