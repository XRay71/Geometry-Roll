
/*
Rain Yeyang
Date: June 16, 2022
Main menu bar of Geometry Roll
Child of JFrame
Implements ActionListener, MouseListener
*/
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.File;
import java.io.IOException;

public class Menu extends JPanel implements ActionListener, MouseListener {

	private final int BUTTON_HEIGHT = 60;
	private final int BUTTON_WIDTH = 300;
	private final int SPACING = 15;

	private Image background;
	private Font font;
	private Color yellow;
	private Color purple;

	// menu options: LVL 1, LVL 2, LVL 3, RULES
	private JButton[] buttons;

	public Menu() throws IOException {

		this.setPreferredSize(new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT)); // dimensions of Geometry
																							// Roll
		this.setFocusable(true);

		// set standard backgrounds and colors
		background = ImageIO.read(new File(Constants.MENU_BACKGROUND));
		yellow = new Color(241, 194, 50); // RGB code for gold-esque yellow color
		purple = new Color(64, 5, 111); // RGB code for deep purple color
		font = new Font("Monospaced", Font.BOLD, 30);

		this.setFont(font);

		// create buttons
		buttons = new JButton[4];
		buttons[0] = new JButton("[ LVL 1 ]");
		buttons[1] = new JButton("[ LVL 2 ]");
		buttons[2] = new JButton("[ LVL 3 ]");
		buttons[3] = new JButton("[ RULES ]");

		// set fonts, foregrounds, backgrounds
		for (int i = 0; i < 4; ++i) {
			buttons[i].setFont(font);
			buttons[i].setForeground(yellow); // set text color to custom yellow
			buttons[i].setBorderPainted(false); // remove visible border
			buttons[i].setOpaque(true);
			buttons[i].setBackground(purple);
			buttons[i].setOpaque(false); // make background transparent
		}

		// set background image

		// set layout of frame to null
		this.setLayout(null);

		// add buttons in succession
		for (int i = 0; i < 4; ++i) {
			// coordinates (x, y), then dimensions (width, height)
			int x = Constants.GAME_WIDTH / 2 - BUTTON_WIDTH / 2;
			int y = 200 + SPACING * (i + 1) + BUTTON_HEIGHT * i;
			buttons[i].setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);

			// add button to frame
			this.add(buttons[i]);

			// add hover effect
			final int id = i;
			buttons[i].addMouseListener(new MouseAdapter() {
				// mouse hovers: change background from transparent to custom purple
				@Override
				public void mouseEntered(MouseEvent e) {
					buttons[id].setOpaque(true);
					buttons[id].setBackground(purple);
				}

				// mouse leaves: change background back to transparent
				@Override
				public void mouseExited(MouseEvent e) {
					buttons[id].setOpaque(false);
				}
			});

			// check whether user clicks on button
			buttons[i].addActionListener(this);
		}

		this.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null); // draw background
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("[ LVL 1 ]")) {
			new GamePanel(1); // Level 1 of Geometry Roll is selected
			Main.gameFrame.remove(this); // discard main menu
		} else if (e.getActionCommand().equals("[ LVL 2 ]")) {
			new GamePanel(2); // Level 2 of Geometry Roll is selected
			Main.gameFrame.remove(this); // discard main menu
		} else if (e.getActionCommand().equals("[ LVL 3 ]")) {
			new GamePanel(3); // Level 3 of Geometry Roll is selected
			Main.gameFrame.remove(this); // discard main menu
		} else if (e.getActionCommand().equals("[ RULES ]")) {
			Main.gameFrame.remove(this); // discard main menu
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// unused but must be overriden
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// unused but must be overriden
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// unused but must be overriden
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// overriden
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// overriden
	}

}
