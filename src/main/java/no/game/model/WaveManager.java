package no.game.model;

import no.game.model.enemy.*;
import no.grid.CellPosition;

import java.util.List;

/**
 * Manages the progression and spawning of enemy waves in the game.
 * 
 * The WaveManager is responsible for handling the current wave,
 * spawning new enemies, and progressing to the next wave.
 * Each wave has a set number of enemies, with a time interval between each
 * enemy spawn. As the waves progress,
 * enemies become stronger with increased health and speed.
 */
public class WaveManager {
    private int currentWave = 0;
    private int enemiesPerWave = 5;
    private int enemiesSpawned = 0;
    private double waveTimer = 0.0;
    private final double timeBetweenEnemies = 1.8;

    private final EnemyManager enemyManager;
    private boolean isWaveActive = false;

    /**
     * Constructs a WaveManager to control enemy waves.
     *
     * @param enemyManager The EnemyManager responsible for managing the
     *                     list of enemies.
     *                     Must not be null.
     * @throws IllegalArgumentException if enemyManager is null.
     */
    public WaveManager(EnemyManager enemyManager) {
        if (enemyManager == null) {
            throw new IllegalArgumentException("EnemyManager cannot be null.");
        }
        this.enemyManager = enemyManager;
    }

    /**
     * Starts a new wave by incrementing the wave count, adjusting the number of
     * enemies,
     * and resetting wave progress.
     */
    public void startNewWave() {
        currentWave++;
        enemiesPerWave += 10;
        enemiesSpawned = 0;
        waveTimer = 0.0;
        isWaveActive = true;
    }

    /**
     * Updates the wave, checking if new enemies should spawn based on the time
     * elapsed.
     * 
     * @param deltaTime The time difference (in seconds) since the last update.
     */
    public void update(double deltaTime) {
        if (!isWaveActive)
            return;

        waveTimer += deltaTime;

        if (enemiesSpawned < enemiesPerWave && waveTimer >= timeBetweenEnemies) {
            spawnEnemy();
            waveTimer = 0.0;
        }

        if (enemiesSpawned >= enemiesPerWave) {
            isWaveActive = false;
        }
    }

    /**
     * Spawns a new enemy, randomly selecting from different enemy types
     * based on the current wave number, which affects their health and speed.
     * Also scales the enemies stats per wave
     */
    private void spawnEnemy() {
        List<CellPosition> path = enemyManager.getPath();
        if (path == null || path.isEmpty())
            return;

        double spawnChance = Math.random();
        IEnemy enemy;

        // Base stats for different enemy types
        double triangleBaseHP = 35;
        double circleBaseHP = 100;
        double basicBaseHP = 50;

        double triangleBaseSpeed = 0.09;
        double circleBaseSpeed = 0.04;
        double basicBaseSpeed = 0.065;

        double hpScale = 1 + (currentWave * 0.2);
        double speedScale = 1 + (currentWave * 0.05);

        if (spawnChance < 0.33) {
            enemy = new TriangleEnemy(path, triangleBaseHP * hpScale, triangleBaseSpeed * speedScale);
        } else if (spawnChance < 0.66) {
            enemy = new CircleEnemy(path, circleBaseHP * hpScale, circleBaseSpeed * speedScale);
        } else {
            enemy = new BasicEnemy(path, basicBaseHP * hpScale, basicBaseSpeed * speedScale);
        }

        enemyManager.addEnemy(enemy);
        enemiesSpawned++;
    }

    /**
     * Handles automatic wave progression. Calls the update(double) method
     * and starts a new wave if all enemies from the current wave have been spawned.
     *
     * @param deltaTime The time difference (in seconds) since the last update.
     */
    public void handleAutoWaveProgression(double deltaTime) {
        update(deltaTime);
        if (canStartNewWave()) {
            startNewWave();
        }
    }

    /**
     * Returns whether a wave is currently active.
     *
     * @return true if the current wave is active, false otherwise.
     */
    public boolean isWaveActive() {
        return isWaveActive;
    }

    /**
     * Returns the current wave number.
     *
     * @return The number of the current wave.
     */
    public int getCurrentWave() {
        return currentWave;
    }

    /**
     * Determines if a new wave can be started.
     * A new wave can start if the current wave is not active and all enemies from
     * the previous wave have been spawned.
     *
     * @return true if a new wave can start, false otherwise.
     */
    public boolean canStartNewWave() {
        return !isWaveActive && enemyManager.getEnemies().isEmpty();
    }
}
