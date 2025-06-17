package no.game.model.tower;

import no.grid.CellPosition;

/**
 * Abstract class representing a generic tower.
 * Handles cooldown logic and provides basic tower properties.
 */
public abstract class Tower implements ITower {
    protected CellPosition position;
    protected int range;
    protected double damage;
    protected int cooldownTicks;
    protected int cooldownRemaining;
    protected int cost;

    /**
     * Constructs a new {@code Tower}.
     *
     * @param position      The position of the tower.
     * @param range         The attack range of the tower.
     * @param damage        The damage dealt per attack.
     * @param cooldownTicks The number of ticks between shots.
     */
    public Tower(CellPosition position, int range, double damage, int cooldownTicks) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }
        this.position = position;
        this.range = range;
        this.damage = damage;
        this.cooldownTicks = cooldownTicks;
        this.cooldownRemaining = 0;
    }

    /**
     * Returns the position of the tower.
     *
     * @return The CellPosition of the tower.
     */
    public CellPosition getPosition() {
        return position;
    }

    @Override
    public boolean canShoot() {
        return cooldownRemaining <= 0;
    }

    @Override
    public void resetCooldown() {
        this.cooldownRemaining = cooldownTicks;
    }

    @Override
    public void tickCooldown() {
        if (cooldownRemaining > 0) {
            cooldownRemaining--;
        }
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getCooldown() {
        return cooldownTicks;
    }

}
