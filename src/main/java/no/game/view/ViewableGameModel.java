package no.game.view;

import no.game.model.GameState;
import no.grid.GridCell;
import no.grid.GridDimension;

public interface ViewableGameModel {

    /**
     * The dimensions of the board, i.e. number of rows and columns
     *
     * @return an object of type GridDimension
     */
    GridDimension getDimension();

    /**
     * An object that when iterated over returns all positions
     * and corresponding values
     *
     * @return an iterable object
     */
    Iterable<GridCell> getTilesOnBoard();

    /**
     * 
     * @return current state of the game
     */
    GameState getGameState();

    /**
     * The current score of the game
     *
     * @return the score as an integer
     */
    int getScore();

}
