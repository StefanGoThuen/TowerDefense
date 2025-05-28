package no.game.model.tower;

import no.game.model.GameBoard;
import no.game.model.GameModel;
import no.game.model.Map;

import no.grid.CellPosition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TowerTest {

    @Test
    public void testTowerPlacementValidPosition() {
        GameModel model = new GameModel();
        model.setGameState();

        CellPosition validPosition = new CellPosition(10, 10);
        TowerType towerType = TowerType.SLOW;
        boolean placed = model.placeTower(validPosition, towerType);

        assertTrue(placed);
    }

    @Test
    public void testTowerPlacementOnPath() {
        GameModel model = new GameModel();
        CellPosition path = new CellPosition(5, 5);

        model.getBoard().set(path, 'w');

        TowerType towerType = TowerType.SNIPER;

        boolean result = model.placeTower(path, towerType);

        assertFalse(result);
    }

    @Test
    public void testTowerPlacementNotEnoughGold() {
        GameModel model = new GameModel();
        model.setGameState();
        model.setGold(0);

        GameBoard board = model.getBoard();
        Map.map1(board);

        CellPosition validPosition = new CellPosition(10, 10);
        TowerType towerType = TowerType.SNIPER;
        boolean placed = model.placeTower(validPosition, towerType);

        assertFalse(placed);
    }

    @Test
    public void testTowerPlacementCannotOverlapAnotherTower() {
        GameModel model = new GameModel();
        model.setGameState();

        CellPosition firstTowerPosition = new CellPosition(5, 5);
        TowerType towerType = TowerType.BASIC;
        boolean firstSuccess = model.placeTower(firstTowerPosition, towerType);
        assertTrue(firstSuccess);

        boolean secondSuccess = model.placeTower(firstTowerPosition, towerType);

        assertFalse(secondSuccess);
    }

    @Test
    public void testGetTowerClassReturnsCorrectClasses() {
        assertEquals(BasicTower.class, TowerType.BASIC.getTowerClass());
        assertEquals(SniperTower.class, TowerType.SNIPER.getTowerClass());
        assertEquals(SlowTower.class, TowerType.SLOW.getTowerClass());
    }

    @Test
    public void testGetTowerClassNotNull() {
        for (TowerType type : TowerType.values()) {
            assertNotNull(type.getTowerClass(), "getTowerClass should not return null for " + type);
        }
    }

}
