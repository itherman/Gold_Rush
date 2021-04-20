package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.swing.*;

import SheetsQuickstart.SheetsQuickstart;

/**
 * @author Ian Etherton
 */

public class GoldRush {

	final static int SIZE = 50;
	public static String player;
	static Model model;
	static View currView;
	static GameMenu menu;
	public static boolean gameover = true;
	public static boolean returnToMenu = true;
	static int score;
	static boolean autoRun;
	static boolean playGame;


	public static void createAndShowGUI() {

		fixForMacMisbehaving();
		JFrame window = new JFrame("Gold Rush");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(525, 575);
		window.setResizable(false);
		window.setVisible(true);

		Timer timer = new Timer(500, new ActionListener() {
			@Override
			public synchronized void actionPerformed(ActionEvent e) {


				if (gameover && returnToMenu) {

					menu = new GameMenu();
					menu.setFocusable(true);
					window.setContentPane(menu);
					window.setVisible(true);
					returnToMenu = false;
					try {
						SheetsQuickstart.updateData();
					} catch (IOException | GeneralSecurityException e1) {
						e1.printStackTrace();
					}
				}

				if (playGame) {
					launchNewGame(window);
				}
			}
		});
		timer.start();
	}

	private static void launchNewGame(JFrame window) {

		returnToMenu = true;
		window.getContentPane().removeAll();
		playGame = false;
		gameover = false;
		score = 0;
		autoRun = false;
		model = new Model(SIZE);
		currView = new View(model,SIZE);
		currView.addKeyListener(customKeyListener);
		currView.setFocusable(true);
		window.setContentPane(currView);
	}

	private static void fixForMacMisbehaving() {
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
			//do nothing
		}
	}

	public enum Pressed {UP,DOWN,RIGHT,LEFT,NONE}

	public static Pressed pressed;

	static KeyListener customKeyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent f) {}
		@Override
		public void keyReleased(KeyEvent f) {}
		@Override
		public void keyPressed(KeyEvent f) {

			if (f.getKeyCode() == KeyEvent.VK_UP) {
				pressed = Pressed.UP;

			} else if (f.getKeyCode() == KeyEvent.VK_DOWN) {
				pressed = Pressed.DOWN;

			} else if (f.getKeyCode() == KeyEvent.VK_RIGHT) {
				pressed = Pressed.RIGHT;

			} else if (f.getKeyCode() == KeyEvent.VK_LEFT) {
				pressed = Pressed.LEFT;

			}
		}
	};

}


