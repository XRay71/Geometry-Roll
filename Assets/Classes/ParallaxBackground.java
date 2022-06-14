package Assets.Classes;

/*
Rain Yeyang
Date: June 15, 2022
Background image scroll class
*/

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.lang.Thread;

public class ParallaxBackground extends Canvas implements Runnable {

	// use two copies to continuously swap the background
	private BufferedImage image;
	private Background image1, image2;

	public ParallaxBackground() throws IOException {
		image1 = new Background(0, 0);

		// position second image to the right of the first (off screen)
		image2 = new Background(image1.getImageWidth(), 0);

		// create individual thread
		new Thread(this).start();
		setVisible(true);
	}

	@Override
	public void run() {
		try {
			while (true) {
				// pause momentarily before continuing to scroll
				Thread.sleep(5);
				repaint();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		// check if background image does not exist
		if (image == null) {
			// create placeholder image
			image = (BufferedImage) (createImage(getWidth(), getHeight()));
		}

		// buffer to draw to
		Graphics buffer = image.createGraphics();

		// draw both copies of the background image to the buffer
		image1.draw(buffer);
		image2.draw(buffer);

		// draw image onto screen
		g2D.drawImage(image, null, 0, 0);
	}

}
