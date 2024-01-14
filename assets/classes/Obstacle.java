
/*
 * Rain Yeyang, Ray Hang
 * Date: June 21, 2022
 * Processes and moves obstacles, including blocks and spikes
 * Child of Rectangle
 * Numbered as follows:
 * 0: block []
 * 1: upright spike /\
 * 2: upside down spike \/
 * 3: upright left triangle |\
 * 4: upside down left triangle |/
 * 5: upright right triangle /|
 * 6: upside down right triangle \|
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Obstacle extends Rectangle {

    private int obstacleType;
    private double xPosition;
    private double yPosition;
    private boolean onScreen;
    private boolean isGregg;
    private BufferedImage image;

    public Obstacle(int obstacleType, double xPosition, double yPosition) throws IOException {
        // call superclass constructor
        super((int) Math.round(xPosition), (int) Math.round(yPosition), Constants.GRID_UNIT, Constants.GRID_UNIT);

        // set variables
        this.obstacleType = obstacleType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        onScreen = false;
        isGregg = false;

        // read in image file
        image = ImageIO.read(new File(Constants.OBSTACLES[obstacleType]));
    }

    // setter functions of obstacle:

    public void setXPosition(double xPosition) {
        // double value of x-coordinate
        this.xPosition = xPosition;
        // integer value of x-coordinate
        this.x = (int) Math.round(xPosition);
    }

    public void setYPosition(double yPosition) {
        // double value of y-coordinate
        this.yPosition = yPosition;
        // integer value of x-coordinate
        this.y = (int) Math.round(yPosition);
    }

    public void setPosition(double xPosition, double yPosition) {
        setXPosition(xPosition);
        setYPosition(yPosition);
    }

    // getter functions of obstacle:

    public double[] getXPosition() {
        return new double[] { xPosition, x };
    }

    public double[] getYPosition() {
        return new double[] { yPosition, y };
    }

    public double[][] getPosition() {
        return new double[][] { { xPosition, yPosition }, { x, y } };
    }

    public double getHorizontalSpeed() {
        return Constants.GAME_SPEED;
    }

    public int getObstacleType() {
        return obstacleType;
    }

    public boolean isOnScreen() {
        // whether obstacle has loaded
        return onScreen;
    }

    // called continuously
    public void update(Graphics g) throws IOException {

        if (Player.isGregg()) {
            if (!isGregg && obstacleType != 7) {
                image = ImageIO.read(new File(Constants.GREGGED[obstacleType]));
            }
            isGregg = true;
        }
        // move obstacle
        setXPosition(xPosition - Constants.GAME_SPEED);

        // obstacle should appear on screen
        if (xPosition <= Constants.GAME_WIDTH) {
            onScreen = true;
        }

        // flag whether obstacle is still on screen
        if (xPosition <= -Constants.GRID_UNIT) {
            onScreen = false;
        }

        // if the obstacle is on the screen, draw it
        if (onScreen) {
            draw(g);
        }
    }

    // same idea as in Player class
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

}
