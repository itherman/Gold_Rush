package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class GameMenu extends JPanel {

	public static String[][] data = new String[10][2];

	public GameMenu() {

		updateTableData();
		setBackground(Color.GREEN);
		add(new WelcomeLabel(),BorderLayout.PAGE_START);
		add(new NewGame(),BorderLayout.LINE_START);
		add(new LeaderBoardPanel(),BorderLayout.SOUTH);
	}

	public void updateTableData() {

		try {
			ArrayList<Integer> numList = new ArrayList<>();
			ArrayList<String> nameList = new ArrayList<>();

			for (int i = 0; i < data.length; i++) {
				numList.add(Integer.parseInt(data[i][0]));
				nameList.add(data[i][1]);
			}

			boolean playerScoreAdded = false;
			Integer counter = 1;

			//Sort the data:
			for (int i = 0; i < numList.size(); i++) {
				if (playerScoreAdded == false
						&& numList.get(i) < GoldRush.score) {
					numList.add(i, GoldRush.score);
					nameList.add(i, GoldRush.player);
					playerScoreAdded = true;
				}
				if (i > 9) {
					numList.remove(i);
					nameList.remove(i);
				} else {
					//Add the data to string array for populating the table
					data[i][0] = numList.get(i).toString();
					data[i][1] = nameList.get(i);
					counter++;
				}
			}

			//Add the data back to an ArrayList to re-populate the text file:
			ArrayList<String> leaderboard = new ArrayList<>();
			for (int i = 0; i < numList.size(); i++) {
				leaderboard.add(numList.get(i).toString()+":"+nameList.get(i));
			}

			//clear all text from text file:
			FileWriter fwOb = new FileWriter("Leaderboard.txt", false);
			PrintWriter pwOb = new PrintWriter(fwOb, false);
			pwOb.flush();
			pwOb.close();
			fwOb.close();


			//print updated list back to text file:
			PrintWriter writer = new PrintWriter("Leaderboard.txt", "UTF-8");
			for (String curr: leaderboard) {
				writer.println(curr);
			}
			writer.close();

		} catch (IOException e) {
			System.out.println("Data Update I/O Exception");
		}

	}

	public class WelcomeLabel extends JPanel{

		public WelcomeLabel() {
			System.out.println("welcome");
			setBackground(Color.GRAY);
			JLabel welcome = new JLabel("Gold Rush");
			welcome.setFont(new Font("Arial", Font.BOLD, 14));
			add(welcome);
		}
	}

	public class NewGame extends JButton{

		public NewGame() {
			setText("NEW GAME");
			addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					GoldRush.playGame = true;
				}

			});
		}
	}

	public class LeaderBoardPanel extends JPanel {


		JTable leaderboard;

		public LeaderBoardPanel() {

			String[] col = {"Score","Name"};

			leaderboard = new JTable(data, col);
			leaderboard.setPreferredScrollableViewportSize(new Dimension(250,50));
			leaderboard.setFillsViewportHeight(true);
			
			add(leaderboard);
		}
	}

}