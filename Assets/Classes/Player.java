package Assets.Classes;

/*
Ray Hang, Rain Yeyang
Date: June 16, 2022
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends Rectangle implements KeyListener {

    private int playerMode = 0;
    private int diameter;
    private boolean doubleMode = false, isMoving = false, hasJumped = false;
    private double verticalSpeed;
    private double xPosition;
    private double yPosition;
    private static BufferedImage spriteOne, spriteTwo, spriteThree, death;

    public Player(double xPosition, double yPosition, int diameter, double verticalSpeed)
            throws IOException {
        super((int) Math.round(xPosition), (int) Math.round(yPosition), diameter, diameter);
        this.verticalSpeed = verticalSpeed;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.diameter = diameter;
        spriteOne = ImageIO.read(new File(Constants.PLAYER_MODE_ZERO_SPRITE));
        spriteTwo = ImageIO.read(new File(Constants.PLAYER_MODE_ONE_SPRITE));
        spriteThree = ImageIO.read(new File(Constants.PLAYER_MODE_TWO_SPRITE));
        death = ImageIO.read(new File(Constants.PLAYER_MODE_THREE_SPRITE));
    }

    public void setMode(int playerMode) {
        if (playerMode == 2) {
            verticalSpeed = verticalSpeed > 0 ? Constants.GAME_SPEED : verticalSpeed == 0 ? 0 : -Constants.GAME_SPEED;
        }
        this.playerMode = playerMode;
    }

    public void setdiameter(int diameter) {
        this.diameter = diameter;
    }

    public void setHorizontalSpeed(int horizontalSpeed) {
        Constants.GAME_SPEED = horizontalSpeed;
    }

    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    public void setSpeed(int horizontalSpeed, double verticalSpeed) {
        this.setHorizontalSpeed(horizontalSpeed);
        this.setVerticalSpeed(verticalSpeed);
    }

    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
        this.x = (int) Math.round(xPosition);
    }

    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
        this.y = (int) Math.round(yPosition);
    }

    public void setPosition(double xPosition, double yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.x = (int) Math.round(xPosition);
        this.y = (int) Math.round(yPosition);
    }

    public int getPlayerMode() {
        return playerMode;
    }

    public int getdiameter() {
        return diameter;
    }

    public boolean isDoubleMode() {
        return doubleMode;
    }

    public double[] getHorizontalPosition() {
        return new double[] { xPosition, x };
    }

    public double[] getVerticalPosition() {
        return new double[] { yPosition, y };
    }

    public double[][] getPosition() {
        return new double[][] { { xPosition, yPosition }, { x, y } };
    }

    public double getHorizontalSpeed() {
        return Constants.GAME_SPEED;
    }

    public double getVerticalSpeed() {
        return verticalSpeed;
    }

    public double[] getSpeed() {
        return new double[] { Constants.GAME_SPEED, verticalSpeed };
    }

    public void update(Graphics g, int time) {
        if (playerMode != 3) {
            yPosition += verticalSpeed;

            if (yPosition >= Constants.GAME_HEIGHT - diameter) {
                yPosition = Constants.GAME_HEIGHT - diameter;
                setVerticalSpeed(0);
                hasJumped = false;
            } else if (yPosition <= 0) {
                if (playerMode == 0) {
                    playerMode = 3;
                    hasJumped = false;
                } else {
                    yPosition = 0;
                }
                setVerticalSpeed(0);
            } else if (playerMode == 0 || (playerMode == 1 && !isMoving)) {
                verticalSpeed -= Constants.GRAVITY;
            } else if (playerMode == 1 && isMoving) {
                verticalSpeed += Constants.GRAVITY;
            }

            setYPosition(yPosition);
            checkCollision();
        }

        draw(g, time);
    }

    public void draw(Graphics g, int time) {

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        double theta;
        AffineTransform newTransform;

        if (playerMode == 0 || playerMode == 3) {
            theta = normalizeAngle(-time / 360 * 2 * Math.PI);
        } else if (playerMode == 1) {
            theta = normalizeAngle(Math.atan2(verticalSpeed, Constants.GAME_SPEED));
        } else {
            theta = normalizeAngle(verticalSpeed > 0 ? Math.PI / 4 : verticalSpeed == 0 ? 0 : -Math.PI / 4);
        }

        newTransform = AffineTransform.getRotateInstance(theta, xPosition + diameter / 2.0, yPosition + diameter / 2.0);
        g2d.setTransform(newTransform);

        if (playerMode == 3) {
            g2d.drawImage(death, x, y, diameter, diameter, null);
        } else {
            g2d.drawImage(playerMode == 1 ? spriteOne : playerMode == 2 ? spriteTwo : spriteThree, x, y, null);
            g2d.setTransform(originalTransform);
        }
    }

    public void checkCollision() {

    }

    private double normalizeAngle(double angle) {
        return (angle + 2 * Math.PI) % (2 * Math.PI);
    }

    public void keyPressed(KeyEvent e) {
        isMoving = true;
        if (playerMode == 0) {
            if (!hasJumped) {
                setVerticalSpeed(10);
                hasJumped = true;
            }
        } else if (playerMode == 2) {
            if (!hasJumped) {
                setVerticalSpeed(-Constants.GAME_SPEED);
                hasJumped = true;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        isMoving = false;
        if (playerMode == 2) {
            setVerticalSpeed(Constants.GAME_SPEED);
            hasJumped = false;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

}
