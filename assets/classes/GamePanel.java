/*
 * Rain Yeyang, Ray Hang
 * Date: June 21, 2022
 * Runs the Geometry Roll game
 * Child of JPanel
 * Implements Runnable, KeyListener, and ActionListener
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GamePanel extends JPanel implements Runnable, KeyListener, ActionListener {

	private Thread gameThread;
	private Image image;
	private Graphics graphics;
	private Color color;

	private int lvl;
	private Player player;
	private ParallaxBackground background; // for levels 1 and 2
	private MulticolorBackground colorBackground; // for level 3
	private int colorBackgroundUpdate;
	private ArrayList<Obstacle> obstacles;

	private boolean gameRunning; // flag whether game has ended
	private boolean panelRunning; // flag whether thread should terminate
	private int distanceTravelled; // track total distance player has travelled
	private int rotateTime; // time variable to control player sprites

	// results buttons
	private JButton[] buttons;
	private String[] buttonNames = { "[ RESPAWN ]", "[ MAIN MENU ]" };

	// audio
	private Audio startAudio;
	private Audio mainAudio;
	private Audio deathAudio;
	private Audio successAudio;
	private Audio greggAudio;
	private boolean audioFlag;
	private boolean isGregg;

	public GamePanel(int lvl)
			throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

		// set dimensions
		this.setPreferredSize(new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT));
		this.addKeyListener(this); // adds KeyListener for the Player
		this.setFocusable(true); // make panel visible on screen

		// set selected level
		this.lvl = lvl;

		// set theme color
		if (lvl == 1 || lvl == 3) {
			color = Constants.PURPLE;
		} else {
			color = Constants.RED;
		}

		// set audio
		startAudio = new Audio(Constants.LVL_START_AUDIO);
		startAudio.play();
		Thread.sleep(1500); // pause

		// play background music
		mainAudio = new Audio(Constants.LVL_AUDIO[lvl - 1]);
		mainAudio.setVolume(0.25f);
		mainAudio.loop();

		deathAudio = new Audio(Constants.DEATH_AUDIO);
		successAudio = new Audio(Constants.SUCCESS_AUDIO);
		greggAudio = new Audio(Constants.THE_GREGG_AUDIO);
		audioFlag = false; // use to check if death or success audio has been played
		isGregg = false;

		// create button array
		buttons = new JButton[2];

		// level 1 or 2: set parallax background
		// level 3: set color-changing background
		setBackground();

		// set obstacles
		obstacles = new ArrayList<>();
		readFile();

		// new player
		player = new Player(Constants.GAME_WIDTH / 2 - Constants.PLAYER_WIDTHS[lvl - 1] / 2,
				Constants.PLAYER_STARTS[lvl - 1],
				lvl - 1);

		// run GamePanel thread
		gameThread = new Thread(this);
		gameThread.start();
		panelRunning = true;
		gameRunning = true;
	}

	// read text file with layout of obstacles in current level
	// store obstacle data in level
	public void readFile() throws NumberFormatException, IOException {
		// read text file
		File file = new File("../../levels/lvl" + lvl + ".txt");
		// create scanner
		Scanner scan = new Scanner(file);

		// game dimensions: 25 grid units by 15 grid units (as defined in Constants)
		for (int i = 0; i < 15; i++) {
			// read current row of text file
			String row = scan.nextLine();
			for (int j = 0; j < row.length(); j++) {
				// skip spaces, where there are no obstacles
				if (row.charAt(j) == ' ') {
					continue;
				} else {
					try {
						// create/render obstacle and add it to obstacles ArrayList
						Obstacle obstacle = new Obstacle(Integer.parseInt(String.valueOf(row.charAt(j))),
								j * Constants.GRID_UNIT,
								i * Constants.GRID_UNIT);
						obstacles.add(obstacle);
					} catch (Exception e) {
						// skip previously unskipped spaces
						continue;
					}
				}
			}
		}

		// close scanner
		scan.close();
	}

	public void setBackground() throws IOException {
		if (lvl == 1) {
			// set parallax background for level 1 of Geometry Roll
			background = new ParallaxBackground(Constants.LVL1_BACKGROUND);
		} else if (lvl == 2) {
			// set parallax background for level 2 of Geometry Roll
			background = new ParallaxBackground(Constants.LVL2_BACKGROUND);
		} else {
			// set color-changing background for level 3 of Geometry Roll
			colorBackgroundUpdate = 0;
			colorBackground = new MulticolorBackground();

		}
	}

	@Override
	public void run() {
		// focus the game so that KeyListener is responsive
		this.requestFocusInWindow();

		// slow game down
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;
		rotateTime = 0;

		// game loop
		while (panelRunning) {
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			rotateTime += delta;
			rotateTime %= 360;
			lastTime = now;

			// objects move only when sufficient time has passed
			if (delta >= 1) {
				repaint();
				delta -= 1;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		// call superclass method
		super.paintComponent(g);

		// draw image off screen
		image = createImage(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
		graphics = image.getGraphics();

		// move parallax background
		if (background != null)
			background.update(graphics);

		// change colour background
		if (colorBackground != null) {
			colorBackground.update(graphics, colorBackgroundUpdate);
			colorBackgroundUpdate += Constants.GAME_SPEED;
		}

		// flag to indicate that the game is ongoing
		boolean flag = true;

		if (gameRunning) {
			// loop through every obstacle
			for (Obstacle o : obstacles) {
				// update and load position of obstacle
				try {
					o.update(graphics);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// check whether player has collided with obstacle
				player.checkCollision(o);

				// stop game if player dies
				if (player.getPlayerMode() == 3) {

					// play death sound effect
					if (!audioFlag) {
						deathAudio.play();
						audioFlag = true;
					}

					gameRunning = false;
				}

				if (Player.isGregg()) {
					if (!isGregg) {
						greggAudio.setVolume(1f);
						greggAudio.play();
					}
					isGregg = true;
				}

				// if obstacle is on screen, game hasn't finished scrolling
				if (o.isOnScreen()) {
					flag = false;
				}
			}
			// update player's distance travelled
			distanceTravelled += Constants.GAME_SPEED;
		}

		// results should be displayed if the game has finished scrolling or if the
		// player has died
		if (flag || !gameRunning || player.getPlayerMode() == 3) {

			// play success sound effect if player reaches the end
			if (!audioFlag) {
				successAudio.play();
				audioFlag = true;
			}

			// set font of displayed text
			graphics.setFont(new Font("Monospaced", Font.BOLD, 50));
			graphics.setColor(Constants.YELLOW);

			// write the amount of times the player has jumped
			graphics.drawString((lvl == 1 ? "JUMPS: " : "TURNS: ") + player.getMoves(), 150, 150);

			// calculate player score
			int score = (int) (100000.0 * (double) distanceTravelled / (double) Constants.LEVEL_LENGTHS[lvl - 1]);
			// display the player score
			graphics.drawString("SCORE: " + score, 150, 220);

			// read in high score of player and compare it to current score
			try {
				File file = new File("../../levels/lvl" + lvl + "_score.txt");
				Scanner scan = new Scanner(file);
				String highscore = scan.nextLine();

				// get current high score
				if (Integer.parseInt(highscore) >= score) {
					// display high score
					graphics.drawString("HIGHSCORE: " + highscore, 150, 290);
				} else {
					// display high score
					graphics.drawString("HIGHSCORE: " + score, 150, 290);

					// update new high score
					PrintWriter writer = new PrintWriter(file);
					// overwrite the highscore if there is a new one
					writer.println(score);
					writer.close();
				}
				scan.close();
			} catch (Exception e) {
				File file = new File("../../levels/lvl" + lvl + "_score.txt");
				// if there is no high score file, create a new one
				try {
					file.createNewFile();
					PrintWriter writer = new PrintWriter(file);
					writer.println(score);
					writer.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// display high score
				graphics.drawString("HIGHSCORE: " + score, 150, 290);
			}

			// calculate percentage of level completed
			int percentage = Math.min(100,
					(int) ((double) distanceTravelled / (double) Constants.LEVEL_LENGTHS[lvl - 1] * 100));

			// display percentage of level completed
			graphics.drawString("LVL COMPLETION: " + percentage + "%", 150, 360);

			// read in max percentage of level completed and compare it to current
			// percentage
			try {
				File file = new File("../../levels/lvl" + lvl + "_completion.txt");
				Scanner scan = new Scanner(file);
				String maxPercentage = scan.nextLine();

				// get current max percentage
				if (Integer.parseInt(maxPercentage) >= percentage) {
					// display max percentage
					graphics.drawString("MAX LVL COMPLETION: " + maxPercentage + "%", 150, 430);
				} else {
					// display max percentage
					graphics.drawString("MAX LVL COMPLETION: " + percentage + "%", 150, 430);

					// update max percentage
					PrintWriter writer = new PrintWriter(file);
					// overwrite the max percentage if there is a new one
					writer.println(percentage);
					writer.close();
				}
				scan.close();
			} catch (Exception e) {
				File file = new File("../../levels/lvl" + lvl + "_completion.txt");
				// if there is no max percentage file, create a new one
				try {
					file.createNewFile();
					PrintWriter writer = new PrintWriter(file);
					writer.println(percentage);
					writer.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// display max percentage
				graphics.drawString("MAX LVL COMPLETION: " + percentage + "%", 150, 430);
			}

			if (buttons[0] == null) {
				// set layout of panel to null
				this.setLayout(null);

				for (int i = 0; i < 2; ++i) {
					// create and name individual button
					buttons[i] = new JButton(buttonNames[i]) {
						// fix button color (on mouse click)
						@Override
						public void paint(Graphics g) {
							Color cur = null;
							// check if button is clicked
							if (getModel().isArmed() && getModel().isPressed()) {
								cur = UIManager.getColor("Button.select");
								UIManager.put("Button.select", color); // fix button color to purple
							}
							super.paint(g);
							if (cur != null) {
								UIManager.put("Button.select", cur);
							}
						}
					};

					// set fonts, foregrounds, backgrounds
					buttons[i].setFont(new Font("Monospaced", Font.BOLD, 30));
					buttons[i].setForeground(Constants.YELLOW); // set text color to custom yellow
					buttons[i].setBorderPainted(false); // remove visible border
					buttons[i].setOpaque(true);
					buttons[i].setBackground(color);
					buttons[i].setOpaque(false); // make button transparent
					buttons[i].setFocusPainted(false); // remove focus outline around text

					// coordinates (x, y), then dimensions (width, height)
					buttons[i].setBounds(Constants.GAME_WIDTH / 2 - 300 * i, 480, 300, 60);
					// add button to frame
					this.add(buttons[i]);

					// add hover effect
					final int id = i;
					buttons[i].addMouseListener(new MouseAdapter() {
						// mouse hovers: change button from transparent to custom purple
						@Override
						public void mouseEntered(MouseEvent e) {
							buttons[id].setOpaque(true);
							buttons[id].setBackground(color);
						}

						// mouse leaves: change button back to transparent
						@Override
						public void mouseExited(MouseEvent e) {
							buttons[id].setOpaque(false);
						}
					});

					// use to check whether user clicks on button
					buttons[i].addActionListener(this);
				}

				this.revalidate();
				this.setVisible(true);
			}

			// stop game
			gameRunning = false;
		}
		// update player
		player.update(graphics, rotateTime);
		// move image onto screen
		g.drawImage(image, 0, 0, this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameRunning) {
			player.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (gameRunning) {
			player.keyReleased(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// clear obstacles ArrayList
		obstacles = new ArrayList<>();
		// flag boolean to stop current thread
		panelRunning = false;
		// discard current panel
		Main.gameFrame.remove(this);
		// stop background music
		mainAudio.stop();

		// return to main menu
		if (e.getActionCommand().equals("[ MAIN MENU ]")) {
			try {
				// return to main menu
				Main.gameFrame.add(new Menu());
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
				e1.printStackTrace();
			}
		}
		// restart current level
		else if (e.getActionCommand().equals("[ RESPAWN ]")) {
			try {
				Main.gameFrame.add(new GamePanel(lvl));
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		Main.gameFrame.revalidate();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// unused but must be overriden
	}

}
