package no.game.model;

import no.grid.GridCell;
import no.grid.GridDimension;
import no.game.model.enemy.EnemyManager;
import no.game.model.enemy.IEnemy;
import no.game.model.tower.BasicTower;
import no.game.model.tower.SlowTower;
import no.game.model.tower.SniperTower;
import no.game.model.tower.Tower;
import no.game.model.tower.TowerType;
import no.game.view.ViewableGameModel;
import no.grid.CellPosition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The GameModel class is the core class responsible for managing the game
 * state,
 * enemy waves, tower placement, and interactions between game objects such as
 * enemies, towers, and projectiles.
 * It implements the ViewableGameModel interface to provide game information for
 * the view layer.
 */
public class GameModel implements ViewableGameModel {

    private GameBoard board;
    private GameState state;
    private EnemyManager enemyManager;
    private WaveManager waveManager;
    private List<Tower> towers;
    private List<Projectile> projectiles;
    private int score;
    private int playerHP;
    private int gold;

    /**
     * Constructs a new GameModel with a default 20x20 game board.
     */
    public GameModel() {
        this(new GameBoard(20, 20));
    }

    /**
     * Constructs a new GameModel with the specified game board.
     *
     * @param board The game board to use for the game model.
     */
    public GameModel(GameBoard board) {
        this.board = board;
        this.state = GameState.CHOOSE;
        this.score = 0;
        this.playerHP = 10;
        this.gold = 500;
        this.towers = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        List<CellPosition> enemyPath = Map.getPath();
        this.enemyManager = new EnemyManager(enemyPath);
        this.waveManager = new WaveManager(this.enemyManager);
        waveManager.startNewWave();

    }

    /**
     * Sets the map for the game model based on the specified map type.
     *
     * @param mapType is a Enum that decides which of map to set for the game model.
     */
    public void setMap(MapType mapType) {
        switch (mapType) {
            case MAP1 -> Map.map1(board);
            case MAP2 -> Map.map2(board);
            case MAP3 -> Map.map3(board);
        }
        List<CellPosition> enemyPath = Map.getPath();
        this.enemyManager = new EnemyManager(enemyPath);
        this.waveManager = new WaveManager(this.enemyManager);
        waveManager.startNewWave();
    }

    /**
     * Updates the game model by processing tower cooldowns, projectiles, enemy
     * movement, enemy death, wave progression, and checks for win/loss conditions.
     *
     * @param deltaTime The time difference (in seconds) since the last update.
     */
    public void update(double deltaTime) {
        if (state != GameState.ACTIVE_GAME)
            return;

        for (Tower tower : towers) {
            tower.tickCooldown();
            if (!tower.canShoot())
                continue;

            IEnemy target = findEnemyInRange(tower);
            if (target != null) {
                if (tower instanceof SlowTower slowTower) {
                    projectiles.add(new Projectile(tower.getPosition(), target, tower.getDamage()));
                    target.applySlow(slowTower.getSlowAmount(), slowTower.getSlowDuration());
                } else {
                    projectiles.add(new Projectile(tower.getPosition(), target, tower.getDamage()));
                }
                tower.resetCooldown();
            }

        }

        Iterator<Projectile> projIt = projectiles.iterator();
        while (projIt.hasNext()) {
            Projectile projectile = projIt.next();
            projectile.move();

            if (projectile.hasHitTarget()) {
                projectile.getTarget().takeDamage(projectile.getDamage());
                projIt.remove();
            }
        }

        enemyManager.update();

        Iterator<IEnemy> enemyIt = enemyManager.getEnemies().iterator();
        while (enemyIt.hasNext()) {
            IEnemy enemy = enemyIt.next();
            if (enemy.isDead()) {
                enemyIt.remove();
                score += 10;
                onEnemyKilled();
                continue;
            }

            if (enemy.isAtEnd()) {
                playerHP--;
                enemyIt.remove();
                if (playerHP <= 0) {
                    state = GameState.GAME_OVER;
                }
            }
        }

        waveManager.handleAutoWaveProgression(deltaTime);

        if (!waveManager.isWaveActive() && enemyManager.getEnemies().isEmpty()) {
            pauseGameState();
        }
    }

    /**
     * Finds the first enemy in range of the specified tower.
     *
     * @param tower The tower to check for enemies in range.
     * @return The first enemy in range, or null if no enemy is in range.
     */
    private IEnemy findEnemyInRange(Tower tower) {
        CellPosition towerPos = tower.getPosition();
        int range = tower.getRange();

        for (IEnemy enemy : enemyManager.getEnemies()) {
            if (distance(towerPos, enemy.getPosition()) <= range) {
                return enemy;
            }
        }
        return null;
    }

    private double distance(CellPosition a, CellPosition b) {
        int dx = a.col() - b.col();
        int dy = a.row() - b.row();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Retrieves the list of enemies in the game.
     *
     * @return The list of enemies in the game.
     */
    public List<IEnemy> getEnemies() {
        return enemyManager.getEnemies();
    }

    /**
     * Retrieves the list of towers in the game.
     * 
     * @return The list of towers in the game.
     */
    public List<Tower> getTowers() {
        return towers;
    }

    /**
     * Retrieves the list of projectiles in the game.
     * 
     * @return The list of projectiles in the game.
     */
    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Places a tower on the game board at the specified position, if the player has
     * enough gold,
     * the position is not occupied, and the tile is not a wall.
     *
     * @param pos  The position on the board where the tower is to be placed.
     * @param type The type of tower to place.
     * @return True if the tower was successfully placed, false otherwise.
     */
    public boolean placeTower(CellPosition pos, TowerType type) {
        int towerCost = switch (type) {
            case BASIC -> 50;
            case SNIPER -> 75;
            case SLOW -> 60;
        };

        if (gold >= towerCost && board.get(pos) != 'w' && !isOccupied(pos)) {
            Tower tower = switch (type) {
                case BASIC -> new BasicTower(pos);
                case SNIPER -> new SniperTower(pos);
                case SLOW -> new SlowTower(pos);
            };

            towers.add(tower);
            gold -= towerCost;
            return true;
        }

        return false;
    }

    private boolean isOccupied(CellPosition position) {
        for (Tower tower : towers) {
            if (tower.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public GridDimension getDimension() {
        return board;
    }

    @Override
    public Iterable<GridCell> getTilesOnBoard() {
        return board;
    }

    @Override
    public GameState getGameState() {
        return state;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    /**
     * Retrieves the current game board.
     *
     * @return The current game board.
     */
    public GameBoard getBoard() {
        return this.board;
    }

    /**
     * Retrieves the current player's health points.
     *
     * @return The current player's health points.
     */
    public int getPlayerHP() {
        return this.playerHP;
    }

    /**
     * Retrieves the current amount of gold the player has.
     *
     * @return The current amount of gold.
     */
    public int getGold() {
        return this.gold;
    }

    /**
     * Retrieves the current wave number.
     *
     * @return The current wave number.
     */
    public int getWaveNumber() {
        return waveManager.getCurrentWave();
    }

    /**
     * Increases the player's gold after an enemy is killed.
     */
    public void onEnemyKilled() {
        gold += 5;
    }

    /**
     * Retrieves the tower at a specified position on the game board.
     *
     * @param position The position on the board to check.
     * @return The tower at the specified position, or null if no tower exists at
     *         that position.
     */
    public Tower getTowerAt(CellPosition position) {
        for (Tower tower : towers) {
            if (tower.getPosition().equals(position)) {
                return tower;
            }
        }
        return null;
    }

    /**
     * Sets the game state to active (the game is in progress).
     *
     * @return The updated game state.
     */
    public GameState setGameState() {
        return state = GameState.ACTIVE_GAME;
    }

    /**
     * Pauses the game and sets the game state to paused.
     *
     * @return The updated game state.
     */
    public GameState pauseGameState() {
        return state = GameState.PAUSED;
    }

    /**
     * Sets a new game and sets the game state to welcome.
     *
     * @return The updated game state.
     */
    public GameState GameStart() {
        return state = GameState.WELCOME;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

}
