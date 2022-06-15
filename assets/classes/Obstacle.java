/*
 * Ray Hang, Rain Yeyang
 * Date: June 16, 2022
 * Processing and moving obstacles, including blocks and spikes
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
    private boolean onScreen = false;
    private BufferedImage image;

    public Obstacle(int obstacleType, double xPosition, double yPosition) throws IOException {
        super((int) Math.round(xPosition), (int) Math.round(yPosition), Constants.GRID_UNIT, Constants.GRID_UNIT);
        this.obstacleType = obstacleType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.image = ImageIO.read(new File(
                obstacleType == 0 ? Constants.SQUARE
                        : obstacleType == 1 ? Constants.UPRIGHT_SPIKE
                                : obstacleType == 2 ? Constants.UPSIDE_DOWN_SPIKE
                                        : obstacleType == 3 ? Constants.LEFT_UPRIGHT_DIAGONAL
                                                : obstacleType == 4 ? Constants.LEFT_UPSIDE_DOWN_DIAGONAL
                                                        : obstacleType == 5 ? Constants.RIGHT_UPRIGHT_DIAGONAL
                                                                : Constants.RIGHT_UPSIDE_DOWN_DIAGONAL));
    }

    // setter function, sets the current x position of the current Obstacle
    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
        this.x = (int) Math.round(xPosition);
    }

    // setter function, sets the current y position of the current Obstacle
    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
        this.y = (int) Math.round(yPosition);
    }

    // setter function, sets both coordinate positions of the current Obstacle
    public void setPosition(double xPosition, double yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.x = (int) Math.round(xPosition);
        this.y = (int) Math.round(yPosition);
    }

    // getter function, returns the Obstacle's horizontal position
    public double[] getHorizontalPosition() {
        return new double[] { xPosition, x };
    }

    // getter function, returns the Obstacle's vertical position
    public double[] getVerticalPosition() {
        return new double[] { yPosition, y };
    }

    // getter function, returns the Obstacle's coordinate position
    public double[][] getPosition() {
        return new double[][] { { xPosition, yPosition }, { x, y } };
    }

    // getter function, returns the Obstacle's horizontal speed
    public double getHorizontalSpeed() {
        return Constants.GAME_SPEED;
    }

    // getter function, returns the obstacle type
    public int getObstacleType() {
        return obstacleType;
    }

    // called continuously
    public void update(Graphics g) {
        xPosition -= Constants.GAME_SPEED;
        setXPosition(xPosition);
        if (xPosition <= Constants.GAME_WIDTH) {
            onScreen = true;
        } else if (xPosition <= -Constants.GRID_UNIT) {
            onScreen = false;
        }
        if (onScreen)
            draw(g);
    }

    // same idea as in Player class
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

}
