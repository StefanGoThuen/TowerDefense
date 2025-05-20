package no.game.model;

import org.junit.jupiter.api.Test;

import no.game.model.enemy.EnemyManager;
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

}
