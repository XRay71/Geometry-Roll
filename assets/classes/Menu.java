/*
 * Rain Yeyang, Ray Hang
 * Date: June 21, 2022
 * Contains the main menu of Geometry Roll
 * Child of JPanel
 * Implements ActionListener, MouseListener
 */

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Menu extends JPanel implements ActionListener, MouseListener {

	private final int BUTTON_HEIGHT = 60;
	private final int BUTTON_WIDTH = 300;
	private final int SPACING = 15;

	private Image background;
	private Font font;

	// menu options
	private JButton[] buttons;
	private String[] buttonNames = {"[ LVL 1 ]", "[ LVL 2 ]", "[ LVL 3 ]", "[ RULES ]"};

	public Menu() throws IOException, UnsupportedAudioFileException, LineUnavailableException {

		// dimensions of Geometry Roll
		this.setPreferredSize(new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT));
		this.setFocusable(true);

		// set background and font
		background = ImageIO.read(new File(Constants.MENU_BACKGROUND));
		font = new Font("Monospaced", Font.BOLD, 30);
		this.setFont(font); 

		// set audio if previously terminated / not started
		if (GameFrame.audio == null) {
			GameFrame.audio = new Audio(Constants.DEFAULT_AUDIO);
			GameFrame.audio.loop();
		}

		// create buttons
		buttons = new JButton[4];
		for (int i = 0; i < 4; ++i) {
			// name individual button
			buttons[i] = new JButton(buttonNames[i]) {
				// fix button color (on mouse click)
				@Override
				public void paint(Graphics g) {
					Color cur = null;
					// check if button is clicked
					if (getModel().isArmed() && getModel().isPressed()) {
						cur = UIManager.getColor("Button.select");
						UIManager.put("Button.select", Constants.PURPLE); // fix button color to purple
					}
					super.paint(g);
					if (cur != null) {
						UIManager.put("Button.select", cur);
					}
				}
			};

			// set fonts, foregrounds, backgrounds
			buttons[i].setFont(font);
			buttons[i].setForeground(Constants.YELLOW); // set text color to custom yellow
			buttons[i].setBorderPainted(false); // remove visible border
			buttons[i].setOpaque(true);
			buttons[i].setBackground(Constants.PURPLE);
			buttons[i].setOpaque(false); // make button transparent
			buttons[i].setFocusPainted(false); // remove focus outline around text
		}

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
				// mouse hovers: change button from transparent to custom purple
				@Override
				public void mouseEntered(MouseEvent e) {
					buttons[id].setOpaque(true);
					buttons[id].setBackground(Constants.PURPLE);
				}

				// mouse leaves: change button back to transparent
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
		// level 1 of Geometry Roll is selected
		if (e.getActionCommand().equals("[ LVL 1 ]")) {
			// stop audio
			GameFrame.audio.stop();
			GameFrame.audio = null;
			// discard main menu
			Main.gameFrame.remove(this);
			try {
				// start level 1
				Main.gameFrame.add(new GamePanel(1));
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e1) {
				e1.printStackTrace();
			}
			Main.gameFrame.revalidate();
		} 
		// level 2 of Geometry Roll is selected
		else if (e.getActionCommand().equals("[ LVL 2 ]")) {
			// stop audio
			GameFrame.audio.stop();
			GameFrame.audio = null;
			// discard main menu
			Main.gameFrame.remove(this);
			try {
				// start level 2
				Main.gameFrame.add(new GamePanel(2));
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e1) {
				e1.printStackTrace();
			}
			Main.gameFrame.revalidate();
		} 
		// level 3 of Geometry Roll is selected
		else if (e.getActionCommand().equals("[ LVL 3 ]")) {
			// stop audio
			GameFrame.audio.stop();
			GameFrame.audio = null;
			// discard main menu
			Main.gameFrame.remove(this);
			try {
				// start level 3
				Main.gameFrame.add(new GamePanel(3));
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e1) {
				e1.printStackTrace();
			}
			Main.gameFrame.revalidate();
		}
		// display rules of Geometry Roll
		else if (e.getActionCommand().equals("[ RULES ]")) {
			// discard main menu
			Main.gameFrame.remove(this);
			try {
				// display rules panel
				Main.gameFrame.add(new Rules());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Main.gameFrame.revalidate();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// unused but must be overriden
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// unused but must be overriden
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// unused but must be overriden
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// overriden
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// overriden
	}

}
