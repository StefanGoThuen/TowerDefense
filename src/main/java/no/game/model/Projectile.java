package no.game.model;

import no.game.model.enemy.IEnemy;
import no.grid.CellPosition;

/**
 * Represents a projectile fired from a tower towards an enemy.
 * The projectile moves step-by-step towards its target and deals damage upon
 * impact.
 */
public class Projectile {
    private CellPosition position;
    private final IEnemy target;
    private final double damage;

    /**
     * Constructs a new Projectile starting from a given position towards a
     * specific target.
     *
     * @param startPos the starting position of the projectile
     * @param target   the enemy the projectile is targeting
     * @param damage   the amount of damage the projectile will inflict upon hitting
     */
    public Projectile(CellPosition startPos, IEnemy target, double damage) {
        this.position = startPos;
        this.target = target;
        this.damage = damage;
    }

    /**
     * Returns the current position of the projectile.
     *
     * @return the current CellPosition of the projectile
     */
    public CellPosition getPosition() {
        return position;
    }

    /**
     * Checks whether the projectile has reached and hit its target.
     *
     * @return true if the projectile's position matches the target's
     *         position, otherwise false
     */
    public boolean hasHitTarget() {
        return position.equals(target.getPosition());
    }

    /**
     * Returns the target enemy that this projectile is aiming for.
     *
     * @return the target IEnemy
     */
    public IEnemy getTarget() {
        return target;
    }

    /**
     * Returns the damage value of the projectile.
     *
     * @return the damage dealt by the projectile
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Moves the projectile one step closer to the target.
     * The movement is performed by adjusting the row and column by one unit
     * in the direction of the target.
     */
    public void move() {
        CellPosition targetPos = target.getPosition();

        int dx = Integer.compare(targetPos.col(), position.col());
        int dy = Integer.compare(targetPos.row(), position.row());

        position = new CellPosition(position.row() + dy, position.col() + dx);
    }
}