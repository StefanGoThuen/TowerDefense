package no.game.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import no.game.model.GameModel;
import no.game.model.GameState;
import no.game.model.Projectile;
import no.game.model.enemy.CircleEnemy;
import no.game.model.enemy.BasicEnemy;
import no.game.model.enemy.IEnemy;
import no.game.model.enemy.TriangleEnemy;
import no.game.model.tower.BasicTower;
import no.game.model.tower.SlowTower;
import no.game.model.tower.SniperTower;
import no.game.model.tower.Tower;
import no.game.model.tower.TowerType;
import no.grid.CellPosition;
import no.grid.GridCell;
import no.grid.GridDimension;

public class GameView extends JPanel {

    public static final int OUTERMARGIN = 0;
    public static final int CELLMARGIN = 0;
    public static final int PREFERREDSIDESIZE = 40;

    private ViewableGameModel viewableTetrisModel;
    private ColorTheme colorTheme;
    private CellPosition hoveredCell = null;
    private TowerType hoveredTowerType = null;
    private boolean isPlacingTower = false;
    private Rectangle2D shopBounds;
    private CellPosition towerMenuPosition = null;

    private Rectangle2D baseTowerBox;
    private Rectangle2D sniperBox;
    private Rectangle2D slowBox;
    private Rectangle2D aoeBox;
    private JButton startWaveButton;
    private JButton pauseButton;
    private Class<? extends Tower> placingTowerType;

    private final Image basicEnemyImage = loadImage("basic_enemy.png");
    private final Image circleEnemyImage = loadImage("circle_enemy.png");
    private final Image triangleEnemyImage = loadImage("triangle_enemy.png");

    public GameView(ViewableGameModel viewableTetrisModel) {
        this.viewableTetrisModel = viewableTetrisModel;
        this.colorTheme = new ColorTheme();
        this.setBackground(colorTheme.getBackgroundColor());
        this.setFocusable(true);
        this.setPreferredSize(getDefaultSize(viewableTetrisModel.getDimension()));

        startWaveButton = new JButton("Start Wave");
        startWaveButton.setFocusable(false);
        startWaveButton.setBounds(getWidth() - 150, 50, 130, 30);
        startWaveButton.addActionListener(e -> {
            if (viewableTetrisModel instanceof GameModel model) {
                model.setGameState();
                pauseButton.setVisible(true);
                repaint();
            }
        });
        add(startWaveButton);

        pauseButton = new JButton("Pause");
        pauseButton.setFocusable(false);
        pauseButton.setBounds(getWidth() - 150, 100, 130, 30);
        pauseButton.addActionListener(e -> {
            if (viewableTetrisModel instanceof GameModel model) {
                model.pauseGameState();
                repaint();
            }
        });
        add(pauseButton);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent hover) {
                if (isPlacingTower) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }

                if (!isPlacingTower) {
                    hoveredCell = null;
                    repaint();
                    return;
                }

                if (shopBounds.contains(hover.getPoint())) {
                    return;
                }

                Rectangle2D box = new Rectangle2D.Double(
                        OUTERMARGIN,
                        OUTERMARGIN,
                        getWidth() - OUTERMARGIN * 2,
                        getHeight() - OUTERMARGIN * 2);
                CellPositionToPixelConverter converter = new CellPositionToPixelConverter(
                        box,
                        viewableTetrisModel.getDimension(),
                        CELLMARGIN);
                hoveredCell = converter.getCellFromPixel(hover.getX(), hover.getY());
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawGame(g2);

        int y = OUTERMARGIN + 40;
        startWaveButton.setBounds(getWidth() - 150, y, 130, 30);
        int y2 = OUTERMARGIN + 80;
        pauseButton.setBounds(getWidth() - 150, y2, 130, 30);

        if (hoveredTowerType != null) {
            drawTowerInfo(g2, hoveredTowerType);
        }
        if (towerMenuPosition != null) {
            Rectangle2D sellButton = getSellButtonBoxFor(towerMenuPosition);
            g.setColor(Color.RED);
            g.fillRect((int) sellButton.getX(), (int) sellButton.getY(),
                    (int) sellButton.getWidth(), (int) sellButton.getHeight());
            g.setColor(Color.WHITE);
            g.drawString("Sell", (int) sellButton.getX() + 10, (int) sellButton.getY() + 17);
        }

    }

    private void drawGame(Graphics2D g2) {
        Rectangle2D box = new Rectangle2D.Double(
                OUTERMARGIN,
                OUTERMARGIN,
                this.getWidth() - OUTERMARGIN * 2,
                this.getHeight() - OUTERMARGIN * 2);
        g2.setColor(colorTheme.getFrameColor());
        g2.fill(box);

        CellPositionToPixelConverter converter = new CellPositionToPixelConverter(box,
                viewableTetrisModel.getDimension(), CELLMARGIN);

        drawCells(g2, viewableTetrisModel.getTilesOnBoard(), converter, colorTheme);
        drawHUD(g2);

        drawTowers(g2, ((GameModel) viewableTetrisModel).getTowers(), converter);

        if (viewableTetrisModel instanceof GameModel) {
            GameModel gameModel = (GameModel) viewableTetrisModel;
            drawEnemies(g2, gameModel.getEnemies(), converter);
        }
        drawHoverCell(g2, converter);

        if (viewableTetrisModel instanceof GameModel gm) {
            drawProjectiles(g2, gm.getProjectiles(), converter);
        }
        drawShop(g2);

        if (viewableTetrisModel.getGameState() == GameState.PAUSED) {
            drawPauseScreen(g2);
        }

        if (viewableTetrisModel.getGameState() == GameState.GAME_OVER) {
            startWaveButton.setVisible(false);
            pauseButton.setVisible(false);
            drawGameOver(g2);
        }

        if (viewableTetrisModel instanceof GameModel model) {
            if (model.getGameState() == GameState.CHOOSE) {
                startWaveButton.setVisible(false);
                pauseButton.setVisible(false);
                drawMapSelectView(g2);
            } else if (model.getGameState() == GameState.WELCOME) {
                startWaveButton.setVisible(true);
                pauseButton.setVisible(true);
            } else if (model.getGameState() == GameState.PAUSED) {
                startWaveButton.setVisible(true);
                pauseButton.setVisible(false);
            }
        }

    }

    private void drawMapSelectView(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 150, 255));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 24));
        g2.drawString("Velg Kart", getWidth() / 2 - 60, getHeight() / 2 - 10);

        Image[] mapImages = {
                loadImage("Map1.png"),
                loadImage("Map2.png"),
                loadImage("Map3.png")
        };

        int imageWidth = 100;
        int imageHeight = 100;

        g2.setFont(new Font("Monospaced", Font.PLAIN, 20));

        for (int i = 1; i <= 3; i++) {
            g2.setColor(Color.DARK_GRAY);
            g2.fill(getMapButton(i));

            g2.setColor(Color.WHITE);
            g2.drawString("Kart " + i, getWidth() / 2 - 30, getHeight() / 2 + 60 + (i - 1) * 70);

            int imageX = getWidth() / 2 - 50 + (i - 2) * 150;
            int imageY = getHeight() / 4 - 50;
            g2.drawImage(mapImages[i - 1], imageX, imageY, imageWidth, imageHeight, null);
            g2.drawString("Kart " + i, imageX + 15, imageY - 10);
        }
    }

    private static void drawCells(Graphics2D g2, Iterable<GridCell> iterable,
            CellPositionToPixelConverter converter, ColorTheme colorTheme) {
        for (GridCell cell : iterable) {
            CellPosition pos = cell.pos();
            Character c = cell.symbol();
            Rectangle2D tile = converter.getBoundsForCell(pos);

            g2.setColor(colorTheme.getCellColor(c));
            g2.fill(tile);
        }
    }

    private static Dimension getDefaultSize(GridDimension gd) {
        int width = (int) (PREFERREDSIDESIZE * gd.cols() + CELLMARGIN * (gd.cols() + 1) + 2 * OUTERMARGIN);
        int height = (int) (PREFERREDSIDESIZE * gd.rows() + CELLMARGIN * (gd.cols() + 1) + 2 * OUTERMARGIN);
        return new Dimension(width, height);
    }

    private void drawHUD(Graphics2D g2) {
        if (!(viewableTetrisModel instanceof GameModel model))
            return;

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 18));

        String goldStr = "💰 Gull: " + model.getGold();
        String hpStr = "❤️ Liv: " + model.getPlayerHP();
        String waveStr = "🌊 Wave: " + model.getWaveNumber();
        String scoreStr = "⭐ Poeng: " + model.getScore();

        int y = OUTERMARGIN + 25;
        int spacing = 180;

        g2.drawString(goldStr, OUTERMARGIN + 10, y);
        g2.drawString(hpStr, OUTERMARGIN + 10 + spacing, y);
        g2.drawString(waveStr, OUTERMARGIN + 10 + spacing * 2, y);
        g2.drawString(scoreStr, OUTERMARGIN + 10 + spacing * 3, y);
    }

    private void drawGameOver(Graphics2D g2) {
        Rectangle2D box = new Rectangle2D.Double(
                0,
                0,
                this.getWidth(),
                this.getHeight());
        g2.setColor(colorTheme.getBackgroundColor());
        g2.fill(box);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 50));
        SwingGraphics.drawCenteredString(g2, "GAME OVER", getBounds());
    }

    private void drawEnemies(Graphics2D g2, List<IEnemy> enemies, CellPositionToPixelConverter converter) {
        for (IEnemy enemy : enemies) {
            Rectangle2D rect = converter.getBoundsForCell(enemy.getPosition());

            Image img;
            if (enemy instanceof TriangleEnemy) {
                img = triangleEnemyImage;
            } else if (enemy instanceof CircleEnemy) {
                img = circleEnemyImage;
            } else if (enemy instanceof BasicEnemy) {
                img = basicEnemyImage;
            } else {
                img = basicEnemyImage;
            }

            g2.drawImage(img,
                    (int) rect.getX(),
                    (int) rect.getY(),
                    (int) rect.getWidth(),
                    (int) rect.getHeight(),
                    null);

            // Draw health bar
            double barHeight = 4;
            double padding = 1;
            double maxHealth = enemy.getMaxHealth();
            double healthRatio = (double) enemy.getHealth() / maxHealth;

            double barWidth = Math.min(rect.getWidth(), rect.getWidth() * healthRatio);

            g2.setColor(Color.GREEN);
            g2.fill(new Rectangle2D.Double(
                    rect.getX(),
                    rect.getY() - barHeight - padding,
                    barWidth,
                    barHeight));
        }
    }

    private void drawTowers(Graphics2D g2, List<Tower> towers, CellPositionToPixelConverter converter) {
        for (Tower tower : towers) {
            Rectangle2D rect = converter.getBoundsForCell(tower.getPosition());
            Color color = switch (tower.getClass().getSimpleName()) {
                case "SniperTower" -> Color.GREEN;
                case "SlowTower" -> Color.CYAN;
                case "BasicTower" -> Color.BLUE;
                case "AoeTower" -> Color.ORANGE;
                default -> Color.BLUE;
            };
            drawTowerShape(g2, rect, tower.getClass(), color);
        }
    }

    private void drawHoverCell(Graphics2D g2, CellPositionToPixelConverter converter) {
        if (!isPlacingTower || hoveredCell == null || !(viewableTetrisModel instanceof GameModel model)) {
            return;
        }

        char tileSymbol = model.getBoard().get(hoveredCell);
        boolean isPath = tileSymbol == 'w';
        boolean hasTower = model.getTowers().stream().anyMatch(t -> t.getPosition().equals(hoveredCell));

        if (!isPath && !hasTower) {
            Rectangle2D rect = converter.getBoundsForCell(hoveredCell);
            Color color = switch (placingTowerType.getSimpleName()) {
                case "SniperTower" -> Color.GREEN;
                case "SlowTower" -> Color.CYAN;
                case "AoeTower" -> Color.ORANGE;
                default -> Color.BLUE;
            };
            drawTowerShape(g2, rect, placingTowerType, color);
        }
    }

    private void drawProjectiles(Graphics2D g2, List<Projectile> projectiles, CellPositionToPixelConverter converter) {
        g2.setColor(Color.WHITE);

        for (Projectile projectile : projectiles) {
            Rectangle2D rect = converter.getBoundsForCell(projectile.getPosition());

            int x = (int) rect.getX() + (int) (rect.getWidth() / 2);
            int y = (int) rect.getY() + (int) (rect.getHeight() / 2);
            int radius = 5;

            g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    private void drawShop(Graphics2D g2) {
        double shopHeight = 80;
        shopBounds = new Rectangle2D.Double(
                OUTERMARGIN,
                getHeight() - shopHeight,
                getWidth() - OUTERMARGIN * 2,
                shopHeight);

        g2.setColor(new Color(30, 30, 30));
        g2.fill(shopBounds);

        g2.setColor(Color.WHITE);
        g2.draw(shopBounds);

        int padding = 15;
        baseTowerBox = new Rectangle2D.Double(
                shopBounds.getX() + padding,
                shopBounds.getY() + padding,
                40,
                40);

        sniperBox = new Rectangle2D.Double(
                baseTowerBox.getMaxX() + padding,
                shopBounds.getY() + padding,
                40,
                40);

        slowBox = new Rectangle2D.Double(
                sniperBox.getMaxX() + padding,
                shopBounds.getY() + padding,
                40,
                40);

        aoeBox = new Rectangle2D.Double(
                slowBox.getMaxX() + padding,
                shopBounds.getY() + padding,
                40,
                40);

        // Basic Tower
        drawTowerShape(g2, baseTowerBox, TowerType.BASIC.getTowerClass(), Color.BLUE);
        g2.setColor(Color.WHITE);
        g2.draw(baseTowerBox);

        // Sniper Tower
        drawTowerShape(g2, sniperBox, TowerType.SNIPER.getTowerClass(), Color.GREEN);
        g2.setColor(Color.WHITE);
        g2.draw(sniperBox);

        // Slow Tower
        drawTowerShape(g2, slowBox, TowerType.SLOW.getTowerClass(), Color.CYAN);
        g2.setColor(Color.WHITE);
        g2.draw(slowBox);

        // AOE Tower
        drawTowerShape(g2, aoeBox, TowerType.AOE.getTowerClass(), Color.ORANGE);
        g2.setColor(Color.WHITE);
        g2.draw(aoeBox);

        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2.drawString("Klikk for å kjøpe tårn", (int) (aoeBox.getMaxX() + 10), (int) (aoeBox.getY() + 25));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point point = e.getPoint();

                if (baseTowerBox.contains(point)) {
                    hoveredTowerType = TowerType.BASIC;
                } else if (sniperBox.contains(point)) {
                    hoveredTowerType = TowerType.SNIPER;
                } else if (slowBox.contains(point)) {
                    hoveredTowerType = TowerType.SLOW;
                } else if (aoeBox.contains(point)) {
                    hoveredTowerType = TowerType.AOE;
                } else {
                    hoveredTowerType = null;
                }

                repaint();
            }
        });

    }

    private void drawTowerInfo(Graphics2D g2, TowerType towerType) {
        String towerInfo = getTowerInfo(towerType);

        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        Point mousePos = getMousePosition();
        if (mousePos == null)
            return;

        int infoBoxX = mousePos.x + 10;
        int infoBoxY = mousePos.y + -50;
        int boxWidth = 220;
        int lineHeight = 18;
        String[] lines = towerInfo.split("\n");
        int boxHeight = lineHeight * lines.length + 10;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(infoBoxX, infoBoxY, boxWidth, boxHeight, 10, 10);

        g2.setColor(Color.WHITE);
        for (int i = 0; i < lines.length; i++) {
            g2.drawString(lines[i], infoBoxX + 10, infoBoxY + 20 + i * lineHeight);
        }
    }

    private String getTowerInfo(TowerType towerType) {
        switch (towerType) {
            case BASIC:
                return "Basic Tower:\nDamage: 3\nRange: 4\nCost: 50";
            case SNIPER:
                return "Sniper Tower:\nDamage: 100\nRange: 20\nCost: 75";
            case SLOW:
                return "Slow Tower:\nSlow: 20% speed\nRange: 5\nCost: 60";
            case AOE:
                return "Aoe Tower:\nDamage: 4\nRange 2\nCost: 80";
            default:
                return "Unknown Tower";
        }
    }

    private void drawPauseScreen(Graphics2D g2) {

        g2.setFont(new Font("Monospaced", Font.BOLD, 50));
        g2.drawString("Paused", getWidth() / 2 - 60, getHeight() / 8 - 30);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 15));
        g2.drawString("Trykk Start Wave for å fortsette å spille, du kan også plassere tårn i Pause modus",
                getWidth() / 2 - 350,
                getHeight() / 8);

    }

    private void drawTowerShape(Graphics2D g2, Rectangle2D rect, Class<?> towerClass, Color color) {
        g2.setColor(color);
        if (towerClass == SniperTower.class) {
            int x1 = (int) rect.getCenterX();
            int y1 = (int) rect.getY();
            int x2 = (int) rect.getX();
            int y2 = (int) rect.getMaxY();
            int x3 = (int) rect.getMaxX();
            int y3 = (int) rect.getMaxY();
            g2.fillPolygon(new int[] { x1, x2, x3 }, new int[] { y1, y2, y3 }, 3);
        } else if (towerClass == SlowTower.class) {
            g2.fillOval((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
        } else if (towerClass == BasicTower.class) {
            g2.fill(rect);
        } else {
            g2.fill(rect);
        }
    }

    /**
     * Returns whether the player is currently placing a tower.
     *
     * @return true if a tower is being placed, false otherwise
     */
    public boolean isPlacingTower() {
        return isPlacingTower;
    }

    /**
     * Sets whether the player is currently placing a tower.
     *
     * @param placing true if placing a tower, false otherwise
     */
    public void setPlacingTower(boolean placing) {
        this.isPlacingTower = placing;
    }

    /**
     * Clears the currently hovered cell (sets it to null).
     */
    public void clearHoveredCell() {
        this.hoveredCell = null;
    }

    /**
     * Returns the bounds of the shop area on the screen.
     *
     * @return a Rectangle2D representing the shop bounds
     */
    public Rectangle2D getShopBounds() {
        return shopBounds;
    }

    /**
     * Returns the bounding box for the base tower shop button.
     *
     * @return a Rectangle2D representing the base tower box
     */
    public Rectangle2D getBaseTowerBox() {
        return baseTowerBox;
    }

    /**
     * Returns the bounding box for the sniper tower shop button.
     *
     * @return a Rectangle2D representing the sniper tower box
     */
    public Rectangle2D getSniperTowerBox() {
        return sniperBox;
    }

    /**
     * Returns the bounding box for the slow tower shop button.
     *
     * @return a Rectangle2D representing the slow tower box
     */
    public Rectangle2D getSlowTowerBox() {
        return slowBox;
    }

    public Rectangle2D getAOETowerBox() {
        return aoeBox;
    }

    /**
     * Sets the type of tower that is currently selected for placement.
     *
     * @param towerType the Class of the tower being placed
     */
    public void setPlacingTowerType(Class<? extends Tower> towerType) {
        this.placingTowerType = towerType;
    }

    /**
     * Returns a Rectangle representing the position and size of a Map selection
     * button.
     *
     * @param index the index of the map button (1 for Map1, 2 for Map2, 3 for Map3,
     *              etc.)
     * @return a Rectangle for the map button
     */
    public Rectangle getMapButton(int index) {
        int buttonWidth = 150;
        int buttonHeight = 50;
        int x = getWidth() / 2 - buttonWidth / 2;
        int baseY = getHeight() / 2 + 30;
        int gap = 70;

        int y = baseY + (index - 1) * gap;
        return new Rectangle(x, y, buttonWidth, buttonHeight);
    }

    private Image loadImage(String filename) {
        try {
            return new ImageIcon(getClass().getResource("/no/game/resources/" + filename)).getImage();
        } catch (Exception e) {
            System.err.println("Image not found: " + filename);
            return null;
        }
    }

    public void showTowerMenu(CellPosition cell) {
        towerMenuPosition = cell;
        repaint();
    }

    public void hideTowerMenu() {
        towerMenuPosition = null;
        repaint();
    }

    public boolean isTowerMenuVisible() {
        return towerMenuPosition != null;
    }

    public CellPosition getTowerMenuPosition() {
        return towerMenuPosition;
    }

    public Rectangle2D getSellButtonBoxFor(CellPosition cell) {
        Rectangle2D boardBox = new Rectangle2D.Double(
                OUTERMARGIN, OUTERMARGIN,
                getWidth() - OUTERMARGIN * 2,
                getHeight() - OUTERMARGIN * 2);

        CellPositionToPixelConverter converter = new CellPositionToPixelConverter(
                boardBox, viewableTetrisModel.getDimension(), CELLMARGIN);

        Point towerCenter = converter.getCellCenter(cell);

        int width = 60;
        int height = 25;
        return new Rectangle2D.Double(towerCenter.x - width / 2, towerCenter.y - 50, width, height);
    }

}