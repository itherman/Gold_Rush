package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class View extends JPanel{

	public boolean buttonPressed = false;

	public View(Model model, int size) {

		// Add the main panel to the center:
		add(new CenterPanel(model,size),BorderLayout.CENTER);

		// Add the information panel to the bottom:
		add(new BottomPanel(),BorderLayout.SOUTH);
	}
	
	private class BottomPanel extends JPanel {

		JLabel score = new JLabel("Score: 0   ");
		public JButton autoRun = new JButton("AUTO-RUN");

		public BottomPanel() {

			setBackground(Color.WHITE);
			add(score);
			autoRun.setBorder(BorderFactory.createRaisedBevelBorder());
			add(autoRun);
			
			autoRun.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					if (GoldRush.autoRun == false) {
						GoldRush.autoRun = true;
						autoRun.setText("AUTO-RUN");
						autoRun.setBorder(BorderFactory.createLoweredBevelBorder());
					} else {
						GoldRush.autoRun = false;
						autoRun.setText("AUTO-RUN");
						autoRun.setBorder(BorderFactory.createRaisedBevelBorder());
					}
					autoRun.transferFocus();
				}
				
			});

			Timer timer = new Timer(500, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					score.setText("Score: " + GoldRush.score);
				}
			});
			timer.start();
		}
	}


	private class CenterPanel extends JPanel {

		public CenterPanel(Model model, int size) {
			
			setLayout(new GridLayout(size,size));

			for (ArrayList<Block> currArray: model.getGameboard()) {

				for (Block currBlock: currArray) {

					add(currBlock);
				}
			}
		}
	}
	
	

}
