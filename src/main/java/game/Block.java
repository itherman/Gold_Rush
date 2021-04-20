package game;

import java.awt.Color;

import javax.swing.JPanel;

public class Block extends JPanel {

	private static final long serialVersionUID = 1L;
	//Instance Variables:
	public int column;
	public int row;
	public Color color;
	public boolean isMan;
	public boolean isBorder;
	public boolean isMonster;
	public boolean isGold;


	public Block(int column, int row) {
		this.row = row;
		this.column = column;
		
		isMan = false;
		if (this.row == 0
				|| this.column == 0
				|| this.row == GoldRush.SIZE-1
				|| this.column == GoldRush.SIZE-1) {
			setBackground(Color.BLACK);
			isBorder = true;
		} else {
			setBackground(Color.GRAY);
			isBorder = false;
		}
		setOpaque(true);
		setFocusable(true);
		isMonster = false;
	}
	
	public Block(int row, int column, Color color) {
		this(row,column);
		this.color = color;
		setBackground(color);
		setOpaque(true);
		setFocusable(true);
	}
	

	public Block(Block other) {
		
		this(other.column,other.row);
	}
	
	public Block setMonster() {
		setBackground(Color.GREEN);
		isMonster = true;
		return this;
	}
	
	public Block unsetMonster() {
		setBackground(Color.GRAY);
		isMonster = false;
		return this;
	}

}


