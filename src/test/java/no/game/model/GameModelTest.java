package no.game.model;

import no.grid.CellPosition;
import no.game.model.enemy.IEnemy;
import no.game.model.tower.TowerType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class GameModelTest {

    private GameModel model;

    @BeforeEach
    public void setup() {
        model = new GameModel();
    }

    @Test
    public void testEnemyKilledIncreasesScoreAndGold() {
        int initialGold = model.getGold();
        int initialScore = model.getScore();

        model.onEnemyKilled();

        assertEquals(initialGold + 5, model.getGold());
        assertEquals(initialScore + 0, model.getScore());
    }

    @Test
    public void testGameOverWhenHpZero() {
        model.setGameState();

        List<CellPosition> path = Map.getPath();
        IEnemy enemy = new IEnemy() {
            @Override
            public void move() {
            }

            @Override
            public boolean isDead() {
                return false;
            }

            @Override
            public boolean isAtEnd() {
                return true;
            }

            @Override
            public void takeDamage(double dmg) {
            }

            @Override
            public double getHealth() {
                return 100;
            }

            @Override
            public double getMaxHealth() {
                return 100;
            }

            @Override
            public CellPosition getPosition() {
                return path.get(0);
            }

            @Override
            public double getSpeed() {
                return 0.0;
            }

            @Override
            public void applySlow(double amount, int durationTicks) {
                // Not testing applySlow, but since IEnemy has the method i must dummy implement
                // it
                throw new UnsupportedOperationException("Unimplemented method 'applySlow'");
            }
        };

        for (int i = 0; i < 10; i++) {
            model.getEnemies().add(enemy);
            model.update(1.0);
        }

        assertEquals(GameState.GAME_OVER, model.getGameState());
    }

    @Test
    public void testInitialGameStateIsChoose() {
        assertEquals(GameState.CHOOSE, model.getGameState());
    }

    @Test
    public void testGameStateTransitions() {

        assertEquals(GameState.CHOOSE, model.getGameState());

        model.GameStart();
        assertEquals(GameState.WELCOME, model.getGameState());

        model.setGameState();
        assertEquals(GameState.ACTIVE_GAME, model.getGameState());

        model.pauseGameState();
        assertEquals(GameState.PAUSED, model.getGameState());

        model.setGameState();
        assertEquals(GameState.ACTIVE_GAME, model.getGameState());

    }

    @Test
    public void testSellTowerSuccessfully() {
        CellPosition pos = new CellPosition(5, 5);
        int startingGold = model.getGold();

        boolean placed = model.placeTower(pos, TowerType.BASIC);
        assertTrue(placed);

        assertEquals(startingGold - 50, model.getGold());

        boolean sold = model.sellTower(pos);
        assertTrue(sold);

        assertEquals(startingGold, model.getGold());

        assertNull(model.getTowerAt(pos));
    }

    @Test
    public void testSellTowerFailsIfNoTower() {
        CellPosition pos = new CellPosition(7, 7);
        int goldBefore = model.getGold();

        boolean sold = model.sellTower(pos);
        assertFalse(sold);

        assertEquals(goldBefore, model.getGold());
    }

}
