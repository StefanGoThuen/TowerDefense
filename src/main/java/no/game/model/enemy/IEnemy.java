package no.game.model.enemy;

import no.grid.CellPosition;

/**
 * Interface representing a generic enemy in the game.
 * Provides methods for movement, health management, and status effects like
 * slowing.
 */
public interface IEnemy {

    /**
     * Updates the enemy's position based on its speed and current effects.
     */
    void move();

    /**
     * Checks if the enemy is dead.
     *
     * @return true if the enemy's health is zero or less, otherwise
     *         false.
     */
    boolean isDead();

    /**
     * Checks if the enemy has reached the end of its path.
     *
     * @return true if the enemy is at the end, otherwise
     *         false.
     */
    boolean isAtEnd();

    /**
     * Reduces the enemy's health by a given amount of damage.
     *
     * @param dmg The amount of damage to apply.
     */
    void takeDamage(double dmg);

    /**
     * Returns the enemy's current health.
     *
     * @return The current health value.
     */
    double getHealth();

    /**
     * Returns the enemy's maximum health.
     *
     * @return The maximum health value.
     */
    double getMaxHealth();

    /**
     * Gets the enemy's current position on the grid.
     *
     * @return The CellPosition representing the enemy's location.
     */
    CellPosition getPosition();

    /**
     * Returns the enemy's current speed.
     *
     * @return The speed value.
     */
    double getSpeed();

    /**
     * Applies a slowing effect to the enemy, reducing its speed.
     *
     * @param amount        The fraction to reduce the speed by (e.g., 0.2 for 20%
     *                      slower).
     * @param durationTicks How many ticks the slow effect lasts.
     */
    void applySlow(double amount, int durationTicks);
}
