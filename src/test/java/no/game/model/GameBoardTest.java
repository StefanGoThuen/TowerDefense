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

}
