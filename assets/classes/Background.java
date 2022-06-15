/*
Rain Yeyang
Date: June 15, 2022
Background image class
*/

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Background {

	private BufferedImage image;
	private int x, y;

	public Background(int x, int y, String imageFile) throws IOException {
		this.x = x; // set x-coordinate
		this.y = y; // set y-coordinate;
		image = ImageIO.read(new File(imageFile)); // open background image
	}

	public void draw(Graphics g) {
		// draw image
		g.drawImage(image, this.x, this.y, image.getWidth(), image.getHeight(), null);

		// move image
		this.x -= 3;

		// reposition image along the right edge if it goes off screen
		if (this.x <= -1 * image.getWidth()) {
			this.x += image.getWidth() * 2; // realign
		}
	}

	public void setXPosition(int x) {
		this.x = x; // reset x-coordinate
	}

	// getter functions

	public int getXPosition() {
		return this.x;
	}

	public int getYPosition() {
		return this.y;
	}

	public int getImageWidth() {
		return image.getWidth();
	}

}
