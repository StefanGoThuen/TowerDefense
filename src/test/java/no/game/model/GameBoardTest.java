package no.game.model;

import no.grid.CellPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    void testPrettyString_emptyBoard() {
        GameBoard board = new GameBoard(3, 3);
        String expected = "---\n---\n---";
        assertEquals(expected, board.prettyString());
    }

    @Test
    void testPrettyString_withSymbols() {
        GameBoard board = new GameBoard(2, 3);
        board.set(new CellPosition(0, 0), 'x');
        board.set(new CellPosition(1, 2), 'y');
        String expected = "x--\n--y";
        assertEquals(expected, board.prettyString());
    }

    @Test
    void testRemoveFullRows_singleFullRow() {
        GameBoard board = new GameBoard(4, 4);

        for (int c = 0; c < 4; c++) {
            board.set(new CellPosition(2, c), 'x');
        }
        assertEquals(1, board.removeFullRows());

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals('-', board.get(new CellPosition(r, c)));
            }
        }
    }

    @Test
    void testRemoveFullRows_multipleFullRows() {
        GameBoard board = new GameBoard(5, 3);

        for (int c = 0; c < 3; c++) {
            board.set(new CellPosition(1, c), 'x');
            board.set(new CellPosition(3, c), 'x');
        }

        int removed = board.removeFullRows();
        assertEquals(2, removed);

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 3; c++) {
                assertEquals('-', board.get(new CellPosition(r, c)));
            }
        }
    }

    @Test
    void testRemoveFullRows_noFullRows() {
        GameBoard board = new GameBoard(3, 3);
        board.set(new CellPosition(0, 0), 'x'); 
        board.set(new CellPosition(1, 1), 'x');

        int removed = board.removeFullRows();
        assertEquals(0, removed);

        assertEquals('x', board.get(new CellPosition(0, 0)));
        assertEquals('x', board.get(new CellPosition(1, 1)));
        assertEquals('-', board.get(new CellPosition(2, 2)));
    }

    @Test
    void testRemoveFullRows_fullBottomRowShiftsUp() {
        GameBoard board = new GameBoard(3, 3);

        for (int c = 0; c < 3; c++) {
            board.set(new CellPosition(2, c), 'x');
        }
        board.set(new CellPosition(0, 0), 'y');

        int removed = board.removeFullRows();
        assertEquals(1, removed);

        assertEquals('-', board.get(new CellPosition(0, 0))); 
        assertEquals('y', board.get(new CellPosition(1, 0))); 
        for (int c = 1; c < 3; c++) {
            assertEquals('-', board.get(new CellPosition(1, c)));
            assertEquals('-', board.get(new CellPosition(2, c)));
        }
    }
}
