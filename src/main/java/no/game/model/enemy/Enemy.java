package no.game.model.enemy;

import no.grid.CellPosition;

import java.util.List;

/**
 * Abstract base class for enemy entities moving along a predefined path in the
 * game.
 * An Enemy has health, a speed, and can be affected by slowing effects.
 * It moves along a list of CellPosition waypoints and handles slow
 * effects internally.
 */
public abstract class Enemy implements IEnemy {
    private List<CellPosition> path;
    private CellPosition position;
    private int pathIndex = 0;
    private double health;
    private double maxHealth;
    private double speed;
    private double movementProgress = 0.0;
    private double currentSpeed;
    private int slowTicksRemaining = 0;
    private double slowAmount = 0.0;

    /**
     * Constructs a new Enemy following a specified path with given health
     * and speed.
     *
     * @param path   The list of CellPosition waypoints representing the
     *               movement path.
     *               The path must have at least one position.
     * @param health The initial and maximum health of the enemy. Must be positive.
     * @param speed  The base speed of the enemy. Must be non-negative.
     * @throws IllegalArgumentException if path is empty, or health
     *                                  <= 0, or speed < 0.
     */
    public Enemy(List<CellPosition> path, double health, double speed) {

        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty.");
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive.");
        }
        if (speed < 0) {
            throw new IllegalArgumentException("Speed cannot be negative.");
        }
        this.path = path;
        this.position = path.get(0);
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.currentSpeed = speed;

    }

    /**
     * Applies a slowing effect to the enemy, reducing its current speed.
     * 
     * If a new slow effect has a greater slow amount than the active one,
     * or if no slow is currently active, it replaces the existing slow effect.
     *
     * @param amount        Fraction (0.0 to 1.0) representing the speed reduction.
     *                      For example, 0.2 slows the enemy by 20%.
     * @param durationTicks Number of ticks the slow effect should last.
     */
    public void applySlow(double amount, int durationTicks) {
        if (amount > slowAmount || slowTicksRemaining <= 0) {
            this.slowAmount = amount;
            this.currentSpeed = speed * (1 - amount);
            this.slowTicksRemaining = durationTicks;
        }
    }

    @Override
    public double getMaxHealth() {
        return maxHealth;
    }

    @Override
    public CellPosition getPosition() {
        return position;
    }

    @Override
    public double getHealth() {
        if (health >= 0) {
            return health;
        } else {
            return health = 0;
        }
    }

    @Override
    public void takeDamage(double dmg) {
        if (getHealth() >= 0) {
            this.health -= dmg;
        } else if (getHealth() <= 0) {
            this.health = 0;
        }

    }

    @Override
    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public boolean isAtEnd() {
        return pathIndex >= path.size() - 1;
    }

    @Override
    public void move() {
        if (pathIndex + 1 < path.size()) {
            movementProgress += currentSpeed;
            while (movementProgress >= 1.0) {
                movementProgress -= 1.0;
                pathIndex++;
                if (pathIndex >= path.size())
                    break;
            }
            if (pathIndex < path.size()) {
                position = path.get(pathIndex);
            }
        }

        if (slowTicksRemaining > 0) {
            slowTicksRemaining--;
            if (slowTicksRemaining <= 0) {
                slowAmount = 0;
                currentSpeed = speed;
            }
        }
    }

}
