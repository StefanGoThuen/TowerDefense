package no.game.view;

import no.grid.CellPosition;
import no.grid.GridDimension;

import java.awt.geom.Rectangle2D;

public class CellPositionToPixelConverter {

    private final Rectangle2D box;
    private final GridDimension gd;
    private final double margin;

    public CellPositionToPixelConverter(Rectangle2D box, GridDimension gd, double margin) {
        this.box = box;
        this.gd = gd;
        this.margin = margin;
    }

    public Rectangle2D getBoundsForCell(CellPosition cellPosition) {
        double cellW = (box.getWidth() - margin * gd.cols() - margin) / gd.cols();
        double cellH = (box.getHeight() - margin * gd.rows() - margin) / gd.rows();
        double cellX = box.getX() + margin + (cellW + margin) * cellPosition.col();
        double cellY = box.getY() + margin + (cellH + margin) * cellPosition.row();
        return new Rectangle2D.Double(cellX, cellY, cellW, cellH);
    }

    public CellPosition getCellFromPixel(int x, int y) {
        double cellW = (box.getWidth() - margin * gd.cols() - margin) / gd.cols();
        double cellH = (box.getHeight() - margin * gd.rows() - margin) / gd.rows();

        int col = (int) ((x - box.getX() - margin) / (cellW + margin));
        int row = (int) ((y - box.getY() - margin) / (cellH + margin));

        if (col >= 0 && col < gd.cols() && row >= 0 && row < gd.rows()) {
            return new CellPosition(row, col);
        }
        return null;
    }
}
