package no.game.model;

import no.grid.CellPosition;
import no.grid.Grid;

public class GameBoard extends Grid {

    public GameBoard(int rows, int cols) {
        super(rows, cols, '-');
    }

    /**
     * A string representation of the board in a readable format.
     * For testing purposes.
     *
     * @return a string representation of the board
     */
    public String prettyString() {
        StringBuilder board = new StringBuilder();
        for (int r = 0; r < this.rows(); r++) {
            StringBuilder row = new StringBuilder();
            for (int c = 0; c < this.cols(); c++) {
                CellPosition pos = new CellPosition(r, c);
                Character symbol = this.get(pos);
                row.append(symbol);
            }
            board.append(row + "\n");
        }
        return board.toString().strip();
    }

}
