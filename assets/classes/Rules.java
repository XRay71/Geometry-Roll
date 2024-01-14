/*
 * Ray Hang, Rain Yeyang
 * Date: June 21, 2022
 * Contains the rules and instructions of Geometry Roll
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

public class Rules extends JPanel implements ActionListener, MouseListener {

	private final int BUTTON_HEIGHT = 60;
	private final int BUTTON_WIDTH = 300;

	private Image background;
	private Font font;

	// button to return to main menu
	private JButton back;

	public Rules() throws IOException {

		// dimensions of Geometry Roll
		this.setPreferredSize(new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT));
		this.setFocusable(true);

		// set background and font
		background = ImageIO.read(new File(Constants.RULES));
		font = new Font("Monospaced", Font.BOLD, 30);
		this.setFont(font);

		// create button
		back = new JButton("[ BACK ]") {
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

		// style button
		back.setFont(font);
		back.setForeground(Constants.YELLOW); // set text color to custom yellow
		back.setBorderPainted(false); // remove visible border
		back.setOpaque(true);
		back.setBackground(Constants.PURPLE);
		back.setOpaque(false); // make button transparent
		back.setFocusPainted(false); // remove focus outline around text

		// set layout of panel to null
		this.setLayout(null);

		// coordinates (x, y), then dimensions (width, height)
		back.setBounds(Constants.GAME_WIDTH / 2 - BUTTON_WIDTH / 2, 480, BUTTON_WIDTH, BUTTON_HEIGHT);

		// add button to panel
		this.add(back);

		// add hover effect
		back.addMouseListener(new MouseAdapter() {
			// mouse hovers: change button from transparent to custom purple
			@Override
			public void mouseEntered(MouseEvent e) {
				back.setOpaque(true);
				back.setBackground(Constants.PURPLE);
			}

			// mouse leaves: change button back to transparent
			@Override
			public void mouseExited(MouseEvent e) {
				back.setOpaque(false);
			}
		});

		// check whether user clicks on button
		back.addActionListener(this);

		// make panel visible
		this.setVisible(true);
		
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null); // draw background
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("[ BACK ]")) {
			Main.gameFrame.remove(this); // discard panel
			try {
				Main.gameFrame.add(new Menu()); // return to main menu
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
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
