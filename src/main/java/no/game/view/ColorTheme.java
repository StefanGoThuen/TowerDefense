package no.game.view;

import java.awt.Color;

public class ColorTheme {

    public Color getCellColor(Character c) {
        Color color = switch (c) {
            case 'r' -> Color.RED;
            case 'g' -> Color.GREEN;
            case 'y' -> Color.YELLOW;
            case 'b' -> Color.BLUE;
            case 'w' -> Color.GRAY;
            case '-' -> new Color(150, 75, 0);

            default -> throw new IllegalArgumentException(
                    "No available color for '" + c + "'");
        };
        return color;
    }

    public Color getFrameColor() {
        return Color.WHITE;
    }

    public Color getBackgroundColor() {
        return Color.BLACK;
    }

}
