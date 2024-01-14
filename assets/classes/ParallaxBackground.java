/*
 * Ray Hang, Rain Yeyang
 * Date: June 21, 2022
 * Makes background(s) parallax, as used in levels 1 and 2 of Geometry Roll
 * Child of Rectangle
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ParallaxBackground extends Rectangle {
	
	private double xPosition;
    private BufferedImage image;

    public ParallaxBackground(String imageFile) throws IOException {
        super(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

		// create image file
		image = ImageIO.read(new File(imageFile));
    }

    // setter function, sets the current x position of the current Obstacle
    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
        this.x = (int) Math.round(xPosition);
    }

    // called continuously
    public void update(Graphics g) {
		
        xPosition -= Constants.GAME_SPEED;
		if (xPosition <= -image.getWidth()) {
			xPosition = 0;
		}
        setXPosition(xPosition);
		draw(g);
    }

    // same idea as that of Player class
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
		g.drawImage(image, x + image.getWidth(), y, null);
    }

}
