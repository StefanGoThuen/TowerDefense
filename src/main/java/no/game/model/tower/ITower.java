package no.game.model.tower;

/**
 * Represents a tower in the game that can attack enemies.
 * ITower defines basic behavior such as shooting cooldowns,
 * attack range, and damage dealt.
 */
public interface ITower {

    /**
     * Determines if the tower is ready to shoot.
     *
     * @return true if the tower can shoot, false otherwise.
     */
    boolean canShoot();

    /**
     * Resets the tower's shooting cooldown after firing.
     */
    void resetCooldown();

    /**
     * Advances the cooldown timer by one tick.
     * 
     * Called once per game tick to handle cooldown progression.
     */
    void tickCooldown();

    /**
     * Returns the attack range of the tower.
     *
     * @return The range of the tower, measured in grid cells.
     */
    int getRange();

    /**
     * Returns the amount of damage the tower deals when shooting.
     *
     * @return The damage dealt per attack.
     */
    double getDamage();

    /**
     * Returns the Cost of a tower.
     *
     * @return The cost of the tower.
     */
    int getCost();

    /**
     * Returns the cooldown of a tower that decides when it can shoot again.
     *
     * @return The cooldown in seconds for a tower attack.
     */
    int getCooldown();

}
