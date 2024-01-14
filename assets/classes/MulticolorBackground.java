/*
Ray Hang, Rain Yeyang
Date: June 21, 2022
Handles color-changing background, as used in level 3 of Geometry Roll
Child of Rectangle
*/
import java.awt.*;

public class MulticolorBackground extends Rectangle {

	// the current color displayed
	private Color currentColor;

	public MulticolorBackground() {
		super(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
		currentColor = Color.BLACK;
	}

	// called continuously
	public void update(Graphics g, int update) {
		// creates a new RGBA color based on the update variable
		currentColor = new Color((int) (update * 2.0 / 6.0) % 256, (int) (update * 3.0 / 6.0) % 256,
				(int) (update * 4.0 / 6.0) % 256, 200);
		draw(g);
	}

	// same idea as that of Player class
	public void draw(Graphics g) {
		g.setColor(currentColor);
		g.fillRect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
	}
}
