package no.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Grid implements IGrid {

    private int rows;
    private int cols;

    private List<List<Character>> cells;

    public Grid(int rows, int cols) {
        this(rows, cols, null);
    }

    public Grid(int rows, int cols, Character defaulValue) {
        this.rows = rows;
        this.cols = cols;

        cells = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Character> innerList = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                innerList.add(defaulValue);
            }
            cells.add(innerList);
        }
    }

    @Override
    public int rows() {
        return this.rows;
    }

    @Override
    public int cols() {
        return this.cols;
    }

    @Override
    public Iterator<GridCell> iterator() {
        List<GridCell> gridCells = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CellPosition pos = new CellPosition(i, j);
                GridCell cell = new GridCell(pos, get(pos));
                gridCells.add(cell);
            }
        }
        return gridCells.iterator();
    }

    @Override
    public void set(CellPosition pos, Character symbol) {
        if (!positionIsOnGrid(pos))
            throw new IndexOutOfBoundsException("Given position is not within grid: (" + pos.row() + ", " + pos.col() + ")");

        int row = pos.row();
        int col = pos.col();
        cells.get(row).set(col, symbol);
    }

    @Override
    public Character get(CellPosition pos) {
        if (!positionIsOnGrid(pos))
            throw new IndexOutOfBoundsException("Given position is not within grid: (" + pos.row() + ", " + pos.col() + ")");

        int row = pos.row();
        int col = pos.col();
        return cells.get(row).get(col);
    }

    @Override
    public boolean positionIsOnGrid(CellPosition pos) {
        boolean isWithinRowBound = pos.row() >= 0 && pos.row() < rows;
        boolean isWithinColBound = pos.col() >= 0 && pos.col() < cols;

        return isWithinRowBound && isWithinColBound;
    }
    
}
