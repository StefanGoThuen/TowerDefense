package no.game.model;

import no.grid.CellPosition;
import java.util.*;

/**
 * The Map class defines different predefined maps for the game.
 * Each map consists of a sequence of CellPosition objects forming a
 * path that enemies will follow.
 */
public class Map {
    private static List<CellPosition> path = new ArrayList<>();

    /**
     * Configures the board with the layout of Map 1.
     * Sets up the path by marking specific tiles and stores the path positions.
     *
     * @param board the game board to update with the path layout
     */
    public static void map1(GameBoard board) {
        path.clear();

        for (int col = 19; col >= 16; col--)
            pathAdd(board, 10, col);
        for (int row = 10; row >= 6; row--)
            pathAdd(board, row, 15);
        for (int col = 14; col >= 13; col--)
            pathAdd(board, 6, col);
        for (int row = 7; row <= 14; row++)
            pathAdd(board, row, 13);
        pathAdd(board, 14, 12);
        for (int row = 14; row >= 6; row--)
            pathAdd(board, row, 11);
        for (int col = 10; col >= 5; col--)
            pathAdd(board, 6, col);
        pathAdd(board, 7, 5);

        for (int col = 5; col <= 9; col++)
            pathAdd(board, 8, col);
        pathAdd(board, 9, 9);
        for (int col = 9; col >= 5; col--)
            pathAdd(board, 10, col);
        pathAdd(board, 11, 5);
        for (int col = 5; col <= 9; col++)
            pathAdd(board, 12, col);
        pathAdd(board, 13, 9);
        for (int col = 9; col >= 0; col--)
            pathAdd(board, 14, col);
    }

    /**
     * Configures the board with the layout of Map 2.
     *
     * @param board the game board to update with the path layout
     */
    public static void map2(GameBoard board) {
        path.clear();

        for (int col = 19; col >= 2; col--)
            pathAdd(board, 4, col);
        pathAdd(board, 5, 2);
        pathAdd(board, 6, 2);
        for (int col = 2; col <= 17; col++)
            pathAdd(board, 7, col);
        pathAdd(board, 8, 17);
        pathAdd(board, 9, 17);
        for (int col = 17; col >= 2; col--)
            pathAdd(board, 10, col);
        pathAdd(board, 11, 2);
        pathAdd(board, 12, 2);
        for (int col = 2; col <= 17; col++)
            pathAdd(board, 13, col);
        pathAdd(board, 14, 17);
        pathAdd(board, 15, 17);
        for (int col = 17; col >= 0; col--)
            pathAdd(board, 16, col);

    }

    /**
     * Configures the board with the layout of Map 3.
     *
     * @param board the game board to update with the path layout
     */
    public static void map3(GameBoard board) {
        path.clear();

        for (int col = 19; col >= 9; col--)
            pathAdd(board, 4, col);
        for (int row = 5; row <= 15; row++)
            pathAdd(board, row, 9);
        for (int col = 9; col >= 0; col--)
            pathAdd(board, 16, col);

    }

    /**
     * Adds a single cell to the path and marks it on the board.
     *
     * @param board the board where the cell is marked as part of the path
     * @param row   the row index of the cell
     * @param col   the column index of the cell
     */

    private static void pathAdd(GameBoard board, int row, int col) {
        CellPosition pos = new CellPosition(row, col);
        board.set(pos, 'w');
        path.add(pos);
    }

    /**
     * Returns the current enemy path.
     *
     * @return a list of CellPosition objects representing the path
     */
    public static List<CellPosition> getPath() {
        return path;
    }
}