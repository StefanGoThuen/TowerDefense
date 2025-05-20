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

    /**
     * Iterates through the board and removes the full rows, moves the rows left
     * downards and adds new empty rows at the top
     * 
     * @return number of rows removed
     */
    public int removeFullRows() {
        int rowsRemoved = 0;
        int r = rows() - 1;
        while (r > 0) {
            if (isFullRow(r)) {
                removeRow(r);
                rowsRemoved++;
            } else {
                r--;
            }
        }
        return rowsRemoved;
    }

    private void removeRow(int row) {
        for (int i = row; i > 0; i--) {
            copyRowTo(i - 1, i);
        }
        fillTopRowWithEmpty();
    }

    private boolean isFullRow(int row) {
        return (!existsInRow(row, '-'));
    }

    private boolean existsInRow(int row, Character symbol) {
        for (int i = 0; i < cols(); i++) {
            if (get(new CellPosition(row, i)).equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    private void copyRowTo(int originalRow, int targetRow) {
        for (int i = 0; i < cols(); i++) {
            set(new CellPosition(targetRow, i), get(new CellPosition(originalRow, i)));
        }
    }

    private void fillTopRowWithEmpty() {
        for (int i = 0; i < cols(); i++) {
            set(new CellPosition(0, i), '-');
        }
    }

}
