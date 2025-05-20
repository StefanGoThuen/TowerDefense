package no.game.model;

import no.grid.CellPosition;
import no.game.model.enemy.IEnemy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class GameModelTest {

    @Test
    public void testEnemyKilledIncreasesScoreAndGold() {
        GameModel model = new GameModel();
        model.setGameState();

        int initialGold = model.getGold();
        int initialScore = model.getScore();

        model.onEnemyKilled();

        assertEquals(initialGold + 5, model.getGold());
        assertEquals(initialScore + 0, model.getScore());
    }

    @Test
    public void testGameOverWhenHpZero() {
        GameModel model = new GameModel();
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
    public void testGameStateTransitions() {
        GameModel model = new GameModel();

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

}
