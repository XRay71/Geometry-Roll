package Assets.Classes;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
Ray Hang, Rain Yeyang
Date: June 15, 2022
Player class represents the controllable Player in the game.
4 modes: 
0->Rolling
1->Spaceship
2->Wave
3->Dead
*/

public class Player extends Rectangle implements KeyListener {

    private int playerMode = 0;
    private int diameter;
    private boolean doubleMode = false, isMoving = false, hasJumped = false;
    private double verticalSpeed;
    private double xPosition;
    private double yPosition;
    private static BufferedImage spriteOne, spriteTwo, spriteThree, death;

    // Constructor
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

    // Setter function, sets the mode of the current Player
    public void setMode(int playerMode) {

        // Wave mode has special movement
        if (playerMode == 2) {
            verticalSpeed = verticalSpeed > 0 ? Constants.GAME_SPEED : verticalSpeed == 0 ? 0 : -Constants.GAME_SPEED;
        }

        this.playerMode = playerMode;
    }

    // Setter function, sets the diameter of the current Player (always a square)
    public void setdiameter(int diameter) {
        this.diameter = diameter;
    }

    // Setter function, sets the horizontal speed of the current Player
    // In fact, because the horizontal speed of the Player is the speed at which the
    // game moves,
    // that's what it changes lol
    public void setHorizontalSpeed(int horizontalSpeed) {
        Constants.GAME_SPEED = horizontalSpeed;
    }

    // Setter function, sets the vertical speed of the current Player
    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    // Setter function, sets both coordinate speeds
    public void setSpeed(int horizontalSpeed, double verticalSpeed) {
        this.setHorizontalSpeed(horizontalSpeed);
        this.setVerticalSpeed(verticalSpeed);
    }

    // Setter function, sets the current x position of the current Player
    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
        this.x = (int) Math.round(xPosition);
    }

    // Setter function, sets the current y position of the current Player
    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
        this.y = (int) Math.round(yPosition);
    }

    // Setter function, sets both coordinate positions of the current Player
    public void setPosition(double xPosition, double yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.x = (int) Math.round(xPosition);
        this.y = (int) Math.round(yPosition);
    }

    // Getter function, returns the current Player mode;
    public int getPlayerMode() {
        return playerMode;
    }

    // Getter function, returns the current diameter of the Player
    public int getdiameter() {
        return diameter;
    }

    // Getter function, returns if the Player is in double mode (MIGHT REMOVE)
    public boolean isDoubleMode() {
        return doubleMode;
    }

    // Getter function, returns the Player's horizontal position
    public double[] getHorizontalPosition() {
        return new double[] { xPosition, x };
    }

    // Getter function, returns the Player's vertical position
    public double[] getVerticalPosition() {
        return new double[] { yPosition, y };
    }

    // Getter function, returns the Player's coordinate position
    public double[][] getPosition() {
        return new double[][] { { xPosition, yPosition }, { x, y } };
    }

    // Getter function, returns the Player's horizontal speed
    public double getHorizontalSpeed() {
        return Constants.GAME_SPEED;
    }

    // Getter function, returns the Player's vertical speed
    public double getVerticalSpeed() {
        return verticalSpeed;
    }

    // Getter function, returns the Player's coordinate speeds
    public double[] getSpeed() {
        return new double[] { Constants.GAME_SPEED, verticalSpeed };
    }

    // Called continuously to update the position of the Player
    // Also draws and checks collisions
    public void update(Graphics g, int time) {

        // The Player only moves if it's not dead
        if (playerMode != 3) {
            yPosition += verticalSpeed;

            // Hitting the bottom
            if (yPosition >= Constants.GAME_HEIGHT - diameter) {
                yPosition = Constants.GAME_HEIGHT - diameter;
                setVerticalSpeed(0);
                hasJumped = false;
            }
            // Hitting the top
            else if (yPosition <= 0) {

                // The Player dies if it's in roll mode
                if (playerMode == 0) {
                    playerMode = 3;
                    hasJumped = false;
                } else {
                    yPosition = 0;
                }

                setVerticalSpeed(0);
            }
            // Gravity
            else if (playerMode == 0 || (playerMode == 1 && !isMoving)) {
                verticalSpeed -= Constants.GRAVITY;
            }
            // Upwards movement
            else if (playerMode == 1 && isMoving) {
                verticalSpeed += Constants.GRAVITY;
            }

            // Sets the position to be the updated one
            setYPosition(yPosition);

            // Checks for collisions
            checkCollision();
        }

        // draws the new positions
        draw(g, time);
    }

    // Called continually from update, draws the Player
    public void draw(Graphics g, int time) {

        // Can't do rotations with Graphics lol, need Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        double theta;
        AffineTransform newTransform;

        // Different angles depending on player mode
        if (playerMode == 0 || playerMode == 3) {
            theta = normalizeAngle(-time / 360 * 2 * Math.PI);
        } else if (playerMode == 1) {
            theta = normalizeAngle(Math.atan2(verticalSpeed, Constants.GAME_SPEED));
        } else {
            theta = normalizeAngle(verticalSpeed > 0 ? Math.PI / 4 : verticalSpeed == 0 ? 0 : -Math.PI / 4);
        }

        // Creates a new transformation to do this
        newTransform = AffineTransform.getRotateInstance(theta, xPosition + diameter / 2.0, yPosition + diameter / 2.0);
        g2d.setTransform(newTransform);

        // If it's dead, do the death thing
        if (playerMode == 3) {
            g2d.drawImage(death, x, y, diameter, diameter, null);
        }
        // Otherwise, draw it normally
        else {
            g2d.drawImage(playerMode == 1 ? spriteOne : playerMode == 2 ? spriteTwo : spriteThree, x, y, null);
        }

        // Resets transformation state
        g2d.setTransform(originalTransform);
    }

    // Called continuously from update, checks for collisions
    public void checkCollision() {

    }

    // Helper function, returns angles in 0-2pi format
    private double normalizeAngle(double angle) {
        return (angle + 2 * Math.PI) % (2 * Math.PI);
    }

    // Called when a key is pressed
    public void keyPressed(KeyEvent e) {
        isMoving = true;

        // Makes Player jump if rolling mode
        if (playerMode == 0) {
            if (!hasJumped) {
                setVerticalSpeed(10);
                hasJumped = true;
            }
        }
        // Makes Player move up if wave mode
        else if (playerMode == 2) {
            if (!hasJumped) {
                setVerticalSpeed(-Constants.GAME_SPEED);
                hasJumped = true;
            }
        }
    }

    // Called when a key is released
    public void keyReleased(KeyEvent e) {
        isMoving = false;

        // Makes Player move down if wave mode
        if (playerMode == 2) {
            setVerticalSpeed(Constants.GAME_SPEED);
            hasJumped = false;
        }
    }

    // Need this because interface lol
    public void keyTyped(KeyEvent e) {
    }

}
