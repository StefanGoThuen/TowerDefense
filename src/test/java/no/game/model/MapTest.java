package no.game.model;

import no.grid.CellPosition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {

    private static final int ROWS = 20;
    private static final int COLS = 20;

    @Test
    void testMap1PathIsMarked() {
        GameBoard board = new GameBoard(ROWS, COLS);
        Map.map1(board);

        List<CellPosition> path = Map.getPath();
        assertFalse(path.isEmpty());

        for (CellPosition pos : path) {
            assertEquals('w', board.get(pos));
        }
    }

    @Test
    void testMap2PathIsMarked() {
        GameBoard board = new GameBoard(ROWS, COLS);
        Map.map2(board);

        List<CellPosition> path = Map.getPath();
        assertFalse(path.isEmpty());

        for (CellPosition pos : path) {
            assertEquals('w', board.get(pos));
        }
    }

    @Test
    void testMap3PathIsMarked() {
        GameBoard board = new GameBoard(ROWS, COLS);
        Map.map3(board);

        List<CellPosition> path = Map.getPath();
        assertFalse(path.isEmpty());

        for (CellPosition pos : path) {
            assertEquals('w', board.get(pos));
        }
    }

    @Test
    void testMapPathClearedBetweenCalls() {
        GameBoard board = new GameBoard(ROWS, COLS);
        Map.map1(board);
        int map1Size = Map.getPath().size();

        Map.map2(board);
        int map2Size = Map.getPath().size();

        assertNotEquals(map1Size, map2Size);
    }

    @Test
    void testMap1PathContinuityAndEdges() {
        GameBoard board = new GameBoard(ROWS, COLS);
        Map.map1(board);
        List<CellPosition> path = Map.getPath();

        assertFalse(path.isEmpty());
        assertTrue(path.size() > 1);

        for (int i = 1; i < path.size(); i++) {
            CellPosition prev = path.get(i - 1);
            CellPosition curr = path.get(i);
            int rowDiff = Math.abs(prev.row() - curr.row());
            int colDiff = Math.abs(prev.col() - curr.col());
            assertTrue((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1));
        }

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                CellPosition pos = new CellPosition(r, c);
                char val = board.get(pos);
                if (!path.contains(pos)) {
                    assertNotEquals('w', val);
                }
            }
        }
    }

    @Test
    void testEachMapHasDistinctPath() {
        GameBoard board1 = new GameBoard(ROWS, COLS);
        Map.map1(board1);
        List<CellPosition> path1 = List.copyOf(Map.getPath());

        GameBoard board2 = new GameBoard(ROWS, COLS);
        Map.map2(board2);
        List<CellPosition> path2 = List.copyOf(Map.getPath());

        assertNotEquals(path1, path2);
    }

    @Test
    void testPrettyStringHasPathCharacters() {
        GameBoard board = new GameBoard(ROWS, COLS);
        Map.map3(board);
        String pretty = board.prettyString();

        long wCount = pretty.chars().filter(c -> c == 'w').count();
        assertEquals(Map.getPath().size(), wCount);
    }
}
