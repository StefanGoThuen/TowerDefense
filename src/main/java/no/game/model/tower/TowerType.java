package no.game.model.tower;

/**
 * TowerType is an enumeration representing the different types of
 * towers
 * available in the game. Each tower type corresponds to a specific tower class
 * that has different attributes and behaviors.
 * 
 * The tower types in this enum include:
 * 
 * BASIC - A standard tower with basic attack capabilities.
 * SNIPER - A long-range tower with powerful shots.
 * SLOW - A tower that slows down enemies in its range.
 * 
 */
public enum TowerType {
    /**
     * Represents a basic tower with standard attack capabilities.
     */
    BASIC,

    /**
     * Represents a sniper tower that has long-range attacks.
     */
    SNIPER,

    /**
     * Represents a slow tower that slows down enemies.
     */
    SLOW;

    /**
     * Returns the corresponding Class object for the tower type.
     * 
     * This method returns the Class of the specific tower implementation
     * that corresponds to the enum value. For example, BASIC returns
     * BasicTower, SNIPER returns SniperTower, and
     * SLOW returns SlowTower.
     *
     * @return the Class of the corresponding tower
     */
    public Class<? extends Tower> getTowerClass() {
        return switch (this) {
            case BASIC -> BasicTower.class;
            case SNIPER -> SniperTower.class;
            case SLOW -> SlowTower.class;
        };
    }
}
