package no.game.controller;

import no.game.model.GameModel;
import no.game.model.GameState;
import no.game.model.MapType;
import no.game.model.tower.TowerType;
import no.game.view.CellPositionToPixelConverter;
import no.game.view.GameView;
import no.grid.CellPosition;

import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.Point;

/**
 * The GameController class handles user interactions and game updates
 * for a tower defense game. It connects the GameModel with the GameView,
 * processing mouse clicks for map selection and tower placement, and updating
 * the game state periodically.
 */
public class GameController {

    private final GameModel model;
    private final GameView view;
    private final Timer gameTimer;
    private TowerType selectedTowerType;

    /**
     * Constructs a new GameController with the specified model and view.
     * Starts a timer to periodically update and repaint the game.
     *
     * @param model the game model
     * @param view  the game view
     */
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();

                if (model.getGameState() == GameState.CHOOSE) {
                    for (int i = 1; i <= 3; i++) {
                        if (view.getMapButton(i).contains(point)) {
                            model.setMap(MapType.values()[i - 1]);
                            model.GameStart();
                            return;
                        }
                    }
                }

                if (view.isTowerMenuVisible()) {
                    Rectangle2D sellButton = view.getSellButtonBoxFor(view.getTowerMenuPosition());
                    if (sellButton.contains(point)) {
                        model.sellTower(view.getTowerMenuPosition());
                        view.hideTowerMenu();
                        return;
                    } else {
                        view.hideTowerMenu();
                    }
                }

                if (handleTowerSelection(point)) {
                    return;
                }

                // --- Check for tower clicks (to open menu) ---
                Rectangle2D boardBox = new Rectangle2D.Double(
                        GameView.OUTERMARGIN,
                        GameView.OUTERMARGIN,
                        view.getWidth() - GameView.OUTERMARGIN * 2,
                        view.getHeight() - GameView.OUTERMARGIN * 2);

                CellPositionToPixelConverter converter = new CellPositionToPixelConverter(
                        boardBox,
                        model.getDimension(),
                        GameView.CELLMARGIN);

                CellPosition clickedCell = converter.getCellFromPixel(point.x, point.y);

                if (clickedCell != null && model.hasTowerAt(clickedCell)) {
                    view.showTowerMenu(clickedCell);
                }
            }
        });

        gameTimer = new Timer(8, e -> {
            model.update(0.2);
            view.repaint();
        });
        gameTimer.start();
    }

    /**
     * Attempts to place a tower at the specified screen position, if valid.
     *
     * @param point the screen coordinates where the player clicked
     */
    private void placeTowerAt(Point point) {
        Rectangle2D box = new Rectangle2D.Double(
                GameView.OUTERMARGIN,
                GameView.OUTERMARGIN,
                view.getWidth() - GameView.OUTERMARGIN * 2,
                view.getHeight() - GameView.OUTERMARGIN * 2);

        CellPositionToPixelConverter converter = new CellPositionToPixelConverter(
                box,
                model.getDimension(),
                GameView.CELLMARGIN);

        CellPosition cell = converter.getCellFromPixel(point.x, point.y);

        if (cell != null && !isCellInShopBounds(cell, converter)) {
            if (model.placeTower(cell, selectedTowerType)) {
                selectedTowerType = null;
                view.setPlacingTower(false);
                view.clearHoveredCell();
                view.repaint();
            }
        }
    }

    /**
     * Checks if the given cell position overlaps with the shop area, where towers
     * cannot be placed.
     *
     * @param cell      the cell to check
     * @param converter the converter used to map cell positions to pixel bounds
     * @return true if the cell is inside the shop bounds, false
     *         otherwise
     */
    private boolean isCellInShopBounds(CellPosition cell, CellPositionToPixelConverter converter) {
        Rectangle2D shopBounds = view.getShopBounds();
        Rectangle2D cellBounds = converter.getBoundsForCell(cell);
        return shopBounds.intersects(cellBounds);
    }

    /**
     * Handles tower selection from the shop area or attempts to place a tower
     * if a tower is currently selected for placement.
     *
     * @param point the screen coordinates where the player clicked
     * @return true if handled, false otherwise
     */
    private boolean handleTowerSelection(Point point) {
        if (view.getBaseTowerBox().contains(point)) {
            selectedTowerType = TowerType.BASIC;
            view.setPlacingTowerType(getTowerClass(selectedTowerType));
            view.setPlacingTower(true);
            return true;
        }
        if (view.getSniperTowerBox().contains(point)) {
            selectedTowerType = TowerType.SNIPER;
            view.setPlacingTowerType(getTowerClass(selectedTowerType));
            view.setPlacingTower(true);
            return true;
        }
        if (view.getSlowTowerBox().contains(point)) {
            selectedTowerType = TowerType.SLOW;
            view.setPlacingTowerType(getTowerClass(selectedTowerType));
            view.setPlacingTower(true);
            return true;
        }
        if (view.getAOETowerBox().contains(point)) {
            selectedTowerType = TowerType.AOE;
            view.setPlacingTowerType(getTowerClass(selectedTowerType));
            view.setPlacingTower(true);
            return true;
        }

        if (view.isPlacingTower()) {
            placeTowerAt(point);
            return true;
        }

        // No action handled
        return false;
    }

    /**
     * Returns the class type corresponding to the given TowerType.
     *
     * @param type the tower type
     * @return the corresponding tower class
     */
    private Class<? extends no.game.model.tower.Tower> getTowerClass(TowerType type) {
        return switch (type) {
            case BASIC -> no.game.model.tower.Tower.class;
            case SNIPER -> no.game.model.tower.SniperTower.class;
            case SLOW -> no.game.model.tower.SlowTower.class;
            case AOE -> no.game.model.tower.AoeTower.class;
        };
    }
}
