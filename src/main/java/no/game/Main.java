package no.game;

import javax.swing.JFrame;

import no.game.controller.GameController;
import no.game.model.GameModel;
import no.game.view.GameView;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {

	public static final String WINDOW_TITLE = "INF101 Tower Defense";

	public static void main(String[] args) {
		GameModel model = new GameModel();
		GameView view = new GameView(model);
		new GameController(model, view);

		JFrame frame = new JFrame(WINDOW_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(view);
		frame.pack();

		frame.setVisible(true);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
				dim.height / 2 - frame.getSize().height / 2);
	}

}
