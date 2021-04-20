package game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

public class Model{

	//Instance Variables:
	private ArrayList<ArrayList<Block>> gameboard;
	private Block man;
	private Block gold;
	private ArrayList<Block> monsters;
	private boolean monsterAdded;
	private boolean everyOther;
	private boolean localGameOver;

	Random rando = new Random();


	//Constructor:
	public Model(int size) {

		gameboard = new ArrayList<>();

		for (int column = 0; column < size; column++) {

			ArrayList<Block> currArrayOfBlocks = new ArrayList<Block>();

			for (int row = 0; row < size; row++) {

				currArrayOfBlocks.add(new Block(column,row));
			}
			gameboard.add(currArrayOfBlocks);
		}

		monsters = new ArrayList<>();	
		GoldRush.autoRun = false;
		GoldRush.score = 0;
		monsterAdded = false;
		everyOther = false;
		localGameOver = false;

		gold = gameboard.get(25).get(25);
		gold.isGold = true;
		gold.setBackground(Color.YELLOW);

		man = gameboard.get(35).get(35);
		man.isMan = true;
		man.setBackground(Color.BLUE);

		monsters.add(gameboard.get(10).get(10).setMonster());

		//Timer with action method to perform the main game actions
		Timer timer = new Timer(90, new ActionListener() {

			public synchronized void actionPerformed(ActionEvent event) {

				if (GoldRush.gameover == false) {

					if (GoldRush.pressed == GoldRush.Pressed.UP) {
						if (gameboard.get(man.column-1).get(man.row).isBorder == false) {
							gameboard.get(man.column--).get(man.row).setBackground(Color.GRAY);
							gameboard.get(man.column).get(man.row).setBackground(Color.BLUE);
						}
					} else if (GoldRush.pressed == GoldRush.Pressed.DOWN) {
						if (gameboard.get(man.column+1).get(man.row).isBorder == false) {
							gameboard.get(man.column++).get(man.row).setBackground(Color.GRAY);
							gameboard.get(man.column).get(man.row).setBackground(Color.BLUE);
						}
					} else if (GoldRush.pressed == GoldRush.Pressed.RIGHT) {
						if (gameboard.get(man.column).get(man.row+1).isBorder == false) {
							gameboard.get(man.column).get(man.row++).setBackground(Color.GRAY);
							gameboard.get(man.column).get(man.row).setBackground(Color.BLUE);
						}
					} else if (GoldRush.pressed == GoldRush.Pressed.LEFT) {
						if (gameboard.get(man.column).get(man.row-1).isBorder == false) {
							gameboard.get(man.column).get(man.row--).setBackground(Color.GRAY);
							gameboard.get(man.column).get(man.row).setBackground(Color.BLUE);
						}
					}
					if (GoldRush.autoRun == false) {
						GoldRush.pressed = GoldRush.Pressed.NONE;
					}
					checkGotGold();
					if (GoldRush.score % 3 == 1 && monsterAdded == false) {
						int randCol = rando.nextInt(49);
						int randRow = rando.nextInt(49);
						Block nextMonster = gameboard.get(randCol).get(randRow);
						if (nextMonster.isMonster == false
								&& nextMonster.isBorder == false
								&& nextMonster.isGold == false
								&& manNearby(nextMonster) == false) {
							monsters.add(nextMonster.setMonster());
							monsterAdded = true;
						}
					}
					if ((everyOther = everyOther == false)? true: false) {
						monsterMove();
					}
					checkGameover();
				}
			}
		});
		timer.start();
	}

	private void monsterMove() {

		for (Block monster: monsters) {

			if (manNearby() == false) {

				int nextRowMove = rando.nextInt(2);
				int nextColMove = rando.nextInt(2);

				if (nextRowMove == 1 && nextColMove == 0) {
					if (gameboard.get(monster.column-1).get(monster.row).isBorder == false
							&& gameboard.get(monster.column-1).get(monster.row).isGold == false) {
						gameboard.get(monster.column--).get(monster.row).unsetMonster();
						gameboard.get(monster.column).get(monster.row).setMonster();
					}
				} else if (nextRowMove == 1 && nextColMove == 1) {
					if (gameboard.get(monster.column+1).get(monster.row).isBorder == false
							&& gameboard.get(monster.column+1).get(monster.row).isGold == false) {
						gameboard.get(monster.column++).get(monster.row).unsetMonster();
						gameboard.get(monster.column).get(monster.row).setMonster();
					}
				} else if (nextRowMove == 0 && nextColMove == 0) {
					if (gameboard.get(monster.column).get(monster.row+1).isBorder == false
							&& gameboard.get(monster.column).get(monster.row+1).isGold == false) {
						gameboard.get(monster.column).get(monster.row++).unsetMonster();
						gameboard.get(monster.column).get(monster.row).setMonster();
					}
				} else if (nextRowMove == 0 && nextColMove == 1) {
					if (gameboard.get(monster.column).get(monster.row-1).isBorder == false
							&& gameboard.get(monster.column).get(monster.row-1).isGold == false) {
						gameboard.get(monster.column).get(monster.row--).unsetMonster();
						gameboard.get(monster.column).get(monster.row).setMonster();
					}
				}
			} else {
				Block nextMonster = gameboard.get((monster.column<=man.column)?monster.column+1:monster.column-1).get((monster.row<=man.row)?monster.row+1:monster.row-1);
				if (nextMonster.isBorder == false && nextMonster.isGold == false) {
					gameboard.get((monster.column<=man.column)?monster.column++:monster.column--).get((monster.row<=man.row)?monster.row++:monster.row--).unsetMonster();
					gameboard.get(monster.column).get(monster.row).setMonster();
				}
			}
		}
	}

	private void checkGameover() {
		if (localGameOver == false) {
			for (Block monster: monsters) {

				if (monster.column == man.column && monster.row == man.row) {
					GoldRush.gameover = true;
					localGameOver = true;
				}
			}
		}
	}

	private boolean manNearby() {

		for (Block monster: monsters) {
			if ((man.column >= monster.column-5 && man.column <= monster.column+5)
					&& (man.row >= monster.row-5 && man.row <= monster.row+5)) {
				return true;
			}
		}
		return false;
	}

	private boolean manNearby(Block monster) {

		if ((man.column >= monster.column-5 && man.column <= monster.column+5)
				&& (man.row >= monster.row-5 && man.row <= monster.row+5)) {
			return true;
		}
		return false;
	}

	private void checkGotGold() {

		if (man.column == gold.column
				&& man.row == gold.row) {
			gameboard.get(gold.column).get(gold.row).isGold = false;
			GoldRush.score++;
			monsterAdded = false;
			int rowInt = rando.nextInt(GoldRush.SIZE-1);
			int colInt = rando.nextInt(GoldRush.SIZE-1);

			while (rowInt == 0 || colInt == 0) {
				rowInt = rando.nextInt(GoldRush.SIZE-1);
				colInt = rando.nextInt(GoldRush.SIZE-1);
			}
			gold.column = rowInt;
			gold.row = colInt;
			gameboard.get(gold.column).get(gold.row).isGold = true;
			gameboard.get(gold.column).get(gold.row).setBackground(Color.YELLOW);
		}
	}

 ArrayList<ArrayList<Block>> getGameboard() {
		return gameboard;
	}

}
