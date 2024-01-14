/* 
 * Rain Yeyang, Ray Hang
 * Date: June 21, 2022
 * Controls the player sprites
 * Child of Rectangle
 * Implements KeyListener
 * Has 4 modes, numbered as follows: 
 * 0 -> Rolling
 * 1 -> Spaceship
 * 2 -> Wave
 * 3 -> Dead
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Rectangle implements KeyListener {

    private int playerMode; // [ball, ship, wave, dead]
    private int moves; // number of times player has jumped in current round
    private boolean isMoving;
    private boolean hasJumped;
    private static boolean greggFlag;

    private double verticalSpeed; // the speed the player is moving up and down (negative is up)
    private double xPosition; // middle of screen
    private double yPosition; // position of the player

    // sprites
    private static BufferedImage spriteOne;
    private static BufferedImage spriteTwo;
    private static BufferedImage spriteThree;
    private static BufferedImage death;

    public Player(double xPosition, double yPosition, int playerMode) throws IOException {
        // superclass constructor
        super((int) Math.round(xPosition), (int) Math.round(yPosition),
                Constants.PLAYER_WIDTHS[playerMode], Constants.PLAYER_HEIGHTS[playerMode]);

        // set player mode
        this.playerMode = playerMode;

        // make Player move down if in wave mode
        if (playerMode == 2) {
            setVerticalSpeed(Constants.GAME_SPEED);
        }

        // set coordinates
        this.xPosition = xPosition;
        this.yPosition = yPosition;

        // set other variabes
        moves = 0;
        isMoving = false;
        hasJumped = false;
        greggFlag = false;

        // read in sprite image files
        spriteOne = ImageIO.read(new File(Constants.PLAYER_MODE_ZERO_SPRITE));
        spriteTwo = ImageIO.read(new File(Constants.PLAYER_MODE_ONE_SPRITE));
        spriteThree = ImageIO.read(new File(Constants.PLAYER_MODE_TWO_SPRITE));
        death = ImageIO.read(new File(Constants.PLAYER_MODE_THREE_SPRITE));
    }

    // setter functions:

    public void setDeadPlayer() {
        // kill player
        playerMode = 3;

        // lay player onto ground
        setYPosition(Constants.GAME_HEIGHT - Constants.PLAYER_HEIGHTS[playerMode]);
        setVerticalSpeed(0);
        this.height = Constants.PLAYER_HEIGHTS[3];
        this.width = Constants.PLAYER_WIDTHS[3];
    }

    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    public void setXPosition(double xPosition) {
        // double value of x-coordinate
        this.xPosition = xPosition;
        // integer value of x-coordinate
        this.x = (int) Math.round(xPosition);
    }

    public void setYPosition(double yPosition) {
        // double value of y-coordinate
        this.yPosition = yPosition;
        // integer value of y-coordinate
        this.y = (int) Math.round(yPosition);
    }

    public void setPosition(double xPosition, double yPosition) {
        setXPosition(xPosition);
        setYPosition(yPosition);
    }

    // getter functions:

    public int getMoves() {
        return moves;
    }

    public int getPlayerMode() {
        return playerMode;
    }

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

    public double getVerticalSpeed() {
        return verticalSpeed;
    }

    public static boolean isGregg() {
        return greggFlag;
    }

    // player's coordinate speeds
    public double[] getSpeed() {
        return new double[] { Constants.GAME_SPEED, verticalSpeed };
    }

    // called continuously to update and draw position of player
    public void update(Graphics g, int time) {

        // player only moves if not dead
        if (playerMode != 3) {

            setYPosition(yPosition + verticalSpeed);

            // check whether player hits bottom of frame
            if (yPosition >= Constants.GAME_HEIGHT - Constants.PLAYER_HEIGHTS[playerMode]) {
                yPosition = Constants.GAME_HEIGHT - Constants.PLAYER_HEIGHTS[playerMode] - 1;
                setVerticalSpeed(0);
                hasJumped = false;
            }
            // check whether player hits top of frame
            else if (yPosition <= 0) {

                // player dies if it's in rolling mode (level 1)
                if (playerMode == 0) {
                    setDeadPlayer();
                } else {
                    // stay at upper bound
                    setYPosition(1);
                    setVerticalSpeed(0);
                }
            }
            // gravity for mode 0
            else if (playerMode == 0) {
                verticalSpeed -= Constants.GRAVITY;
            }
            // gravity for mode 1
            else if (playerMode == 1 && !isMoving) {
                verticalSpeed -= Constants.GRAVITY / 2.0;
            }
            // upwards movement
            else if (playerMode == 1 && isMoving) {
                verticalSpeed += Constants.GRAVITY / 2.0;
            }

            // set position to be updated
            setYPosition(yPosition);
        }

        // draw new positions
        draw(g, time);
    }

    // called continually from update, draw player
    public void draw(Graphics g, int time) {

        // Graphics2D required for rotations
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        double theta;
        AffineTransform newTransform;

        // angles depending on player mode
        if (playerMode == 0 || playerMode == 3) {
            theta = normalizeAngle(time / 90.0 * 2.0 * Math.PI);
        } else if (playerMode == 1) {
            theta = normalizeAngle(Math.atan2(verticalSpeed, Constants.GAME_SPEED));
        } else {
            theta = normalizeAngle(verticalSpeed > 0 ? Math.PI / 4.0 : verticalSpeed == 0 ? 0 : -Math.PI / 4.0);
        }

        // create new transformation to do this
        newTransform = AffineTransform.getRotateInstance(theta, xPosition + Constants.PLAYER_WIDTHS[playerMode] / 2.0,
                yPosition + Constants.PLAYER_HEIGHTS[playerMode] / 2.0);
        g2d.setTransform(newTransform);

        // if it's dead, do the death thing
        if (playerMode == 3) {
            g2d.drawImage(death, x, y, Constants.PLAYER_WIDTHS[playerMode], Constants.PLAYER_HEIGHTS[playerMode], null);
        }
        // otherwise, draw it normally
        else {
            g2d.drawImage(playerMode == 0 ? spriteOne : playerMode == 1 ? spriteTwo : spriteThree, x, y, null);
        }

        // resets transformation state
        g2d.setTransform(originalTransform);
    }

    // called continuously from update, check for collision
    public void checkCollision(Obstacle o) {

        // check if obstacle is touching player or if player is dead
        if (playerMode == 3 || xPosition + Constants.PLAYER_WIDTHS[playerMode] < o.getXPosition()[0]
                || yPosition + Constants.PLAYER_HEIGHTS[playerMode] < o.getYPosition()[0]
                || xPosition > o.getXPosition()[0] + Constants.GRID_UNIT
                || yPosition > o.getYPosition()[0] + Constants.GRID_UNIT) {
            return;
        }

        int type = o.getObstacleType();

        if (type == 7) {
            greggFlag = true;
            return;
        }

        // check obstacle 1: player > /\
        if (type == 1) {
            // spike always kills
            // check left side of spike
            // use linear inequality and Pythagoras' theorem to calculate if the ball hit
            // the spike or not
            if (xPosition + Constants.PLAYER_WIDTHS[playerMode] > o.getXPosition()[0]
                    && xPosition + Constants.PLAYER_WIDTHS[playerMode] < o.getXPosition()[0] + o.getWidth() / 2.0) {
                if ((Constants.GAME_HEIGHT - yPosition - Constants.PLAYER_HEIGHTS[playerMode]
                        - (Constants.GAME_HEIGHT - o.getYPosition()[0] - o.getHeight()))
                        * (Constants.GAME_HEIGHT - yPosition - Constants.PLAYER_HEIGHTS[playerMode]
                                - (Constants.GAME_HEIGHT - o.getYPosition()[0] - o.getHeight())) <= 3.0
                                        * (xPosition + Constants.PLAYER_WIDTHS[playerMode] - o.getXPosition()[0])
                                        * (xPosition + Constants.PLAYER_WIDTHS[playerMode] - o.getXPosition()[0])) {
                    setDeadPlayer();
                    return;
                }
            } else {
                if (-(Constants.GAME_HEIGHT - yPosition - Constants.PLAYER_HEIGHTS[playerMode]
                        - (Constants.GAME_HEIGHT - o.getYPosition()[0]))
                        * (Constants.GAME_HEIGHT - yPosition - Constants.PLAYER_HEIGHTS[playerMode]
                                - (Constants.GAME_HEIGHT - o.getYPosition()[0])) <= -3.0
                                        * (xPosition - o.getXPosition()[0] - o.getWidth() / 2.0)
                                        * (xPosition - o.getXPosition()[0] - o.getWidth() / 2.0)) {
                    setDeadPlayer();
                    return;
                }
            }
        }

        // check obstacle 2: player > \/
        if (type == 2) {
            // spike always kills
            // check left side of spike
            // use linear inequality and Pythagoras' theorem to calculate if the ball hit
            // the spike or not
            if (xPosition + Constants.PLAYER_WIDTHS[playerMode] > o.getXPosition()[0]
                    && xPosition + Constants.PLAYER_WIDTHS[playerMode] < o.getXPosition()[0] + o.getWidth() / 2.0) {
                if (-(Constants.GAME_HEIGHT - yPosition - (Constants.GAME_HEIGHT - o.getYPosition()[0]))
                        * (Constants.GAME_HEIGHT - yPosition - (Constants.GAME_HEIGHT - o.getYPosition()[0])) >= -3.0
                                * (xPosition + Constants.PLAYER_WIDTHS[playerMode] - o.getXPosition()[0])
                                * (xPosition + Constants.PLAYER_WIDTHS[playerMode] - o.getXPosition()[0])) {
                    setDeadPlayer();
                    return;
                }
            } else {
                if ((Constants.GAME_HEIGHT - yPosition - (Constants.GAME_HEIGHT - o.getYPosition()[0] - o.getHeight()))
                        * (Constants.GAME_HEIGHT - yPosition
                                - (Constants.GAME_HEIGHT - o.getYPosition()[0] - o.getHeight())) >= 3.0
                                        * (xPosition - o.getXPosition()[0] - o.getWidth() / 2.0)
                                        * (xPosition - o.getXPosition()[0] - o.getWidth() / 2.0)) {
                    setDeadPlayer();
                    return;
                }
            }
        }

        // check obstacle 0: player > []

        if (type == 0) {

            // check player mode 0
            if (playerMode == 0) {
                // check if the player is in the left dead buffer zone
                if (yPosition + Constants.PLAYER_HEIGHTS[playerMode] >= o.getYPosition()[0]
                        + Constants.PLAYER_HEIGHTS[playerMode] / 4.0
                        && xPosition + Constants.PLAYER_WIDTHS[playerMode] / 2 <= o.getXPosition()[0]
                                + 1.5 * Constants.GAME_SPEED) {
                    setDeadPlayer();
                    // check previous position of the player to see if it is approaching from above
                } else if (yPosition - verticalSpeed <= o.getYPosition()[0] && verticalSpeed >= -9) {
                    setYPosition(o.getYPosition()[0] - Constants.PLAYER_HEIGHTS[playerMode] - 1);
                    setVerticalSpeed(-Constants.GRAVITY);
                    hasJumped = false;

                } else if (yPosition - verticalSpeed >= o.getYPosition()[0]) {
                    setDeadPlayer();
                }
                return;
            }

            // check player mode 1
            if (playerMode == 1) {
                // check if the player is in the left dead buffer zone
                if (yPosition + Constants.PLAYER_HEIGHTS[playerMode] >= o.getYPosition()[0]
                        + Constants.PLAYER_HEIGHTS[playerMode] / 4.0
                        && xPosition + Constants.PLAYER_WIDTHS[playerMode] / 2 <= o.getXPosition()[0]
                                + 1.5 * Constants.GAME_SPEED) {
                    setDeadPlayer();
                    // check previous position of the player to see if it is approaching from above
                } else if (yPosition - verticalSpeed <= o.getYPosition()[0] && verticalSpeed >= -9) {
                    setYPosition(o.getYPosition()[0] - Constants.PLAYER_HEIGHTS[playerMode] - 1);
                    setVerticalSpeed(0);
                    hasJumped = false;

                } else if (yPosition - verticalSpeed >= o.getYPosition()[0]) {
                    setYPosition(o.getYPosition()[0] + o.getHeight() + 1);
                    setVerticalSpeed(0);
                }
                return;
            }

            // check player mode 2
            if (playerMode == 2) {
                // check if the player is in the left dead buffer zone
                if (yPosition + Constants.PLAYER_HEIGHTS[playerMode] >= o.getYPosition()[0]
                        + Constants.PLAYER_HEIGHTS[playerMode] / 4.0
                        && xPosition + Constants.PLAYER_WIDTHS[playerMode] / 2 <= o.getXPosition()[0]
                                + Constants.GAME_SPEED) {
                    setDeadPlayer();
                    // check previous position of the player to see if it is approaching from above
                } else if (yPosition - verticalSpeed <= o.getYPosition()[0]) {
                    setYPosition(o.getYPosition()[0] - Constants.PLAYER_HEIGHTS[playerMode] - 1);
                    setVerticalSpeed(0);
                    hasJumped = false;

                } else if (yPosition - verticalSpeed >= o.getYPosition()[0]) {
                    setYPosition(o.getYPosition()[0] + o.getHeight() + 1);
                    setVerticalSpeed(0);
                }
                return;
            }

        }

        if (playerMode == 2) {

            // check obstacle 3: player > |\
            if (type == 3) {
                if (-(Constants.GAME_HEIGHT - yPosition - Constants.PLAYER_HEIGHTS[playerMode]
                        - (Constants.GAME_HEIGHT - o.getYPosition()[0]))
                        * (Constants.GAME_HEIGHT - yPosition - Constants.PLAYER_HEIGHTS[playerMode]
                                - (Constants.GAME_HEIGHT - o.getYPosition()[0])) <= -(xPosition - o.getXPosition()[0]
                                        - o.getWidth() / 2.0)
                                        * (xPosition - o.getXPosition()[0] - o.getWidth() / 2.0)) {
                    setDeadPlayer();
                    return;
                }
            }

            // check obstacle 4: player > |/
            if (type == 4) {
                if ((Constants.GAME_HEIGHT - yPosition - (Constants.GAME_HEIGHT - o.getYPosition()[0] - o.getHeight()))
                        * (Constants.GAME_HEIGHT - yPosition
                                - (Constants.GAME_HEIGHT - o.getYPosition()[0] - o.getHeight())) >= (xPosition
                                        - o.getXPosition()[0] - o.getWidth() / 2.0)
                                        * (xPosition - o.getXPosition()[0] - o.getWidth() / 2.0)) {
                    setDeadPlayer();
                    return;
                }
            }

            // check obstacle 5: player > /|
            if (type == 5) {
                if ((Constants.GAME_HEIGHT - yPosition - Constants.PLAYER_HEIGHTS[playerMode]
                        - (Constants.GAME_HEIGHT - o.getYPosition()[0] - o.getHeight()))
                        * (Constants.GAME_HEIGHT - yPosition - Constants.PLAYER_HEIGHTS[playerMode]
                                - (Constants.GAME_HEIGHT - o.getYPosition()[0] - o.getHeight())) <= (xPosition
                                        + Constants.PLAYER_WIDTHS[playerMode] - o.getXPosition()[0])
                                        * (xPosition + Constants.PLAYER_WIDTHS[playerMode] - o.getXPosition()[0])) {
                    setDeadPlayer();
                    return;
                }
            }

            // check obstacle 6: player > \|
            if (type == 5) {
                if (-(Constants.GAME_HEIGHT - yPosition - (Constants.GAME_HEIGHT - o.getYPosition()[0]))
                        * (Constants.GAME_HEIGHT - yPosition
                                - (Constants.GAME_HEIGHT - o.getYPosition()[0])) >= -(xPosition
                                        + Constants.PLAYER_WIDTHS[playerMode] - o.getXPosition()[0])
                                        * (xPosition + Constants.PLAYER_WIDTHS[playerMode] - o.getXPosition()[0])) {
                    setDeadPlayer();
                    return;
                }
            }

        }

    }

    // helper function, return angles in 0-2pi format
    private double normalizeAngle(double angle) {
        return (angle + 2 * Math.PI) % (2 * Math.PI);
    }

    // called when key is pressed
    public void keyPressed(KeyEvent e) {

        if (!isMoving && playerMode == 1) {
            moves++;
        }
        isMoving = true;

        // make player jump if in rolling mode (0)
        if (playerMode == 0) {
            if (!hasJumped) {
                setVerticalSpeed(-10);
                hasJumped = true;
                moves++;
            }
        }
        // make player move up if in wave mode (2)
        else if (playerMode == 2) {
            if (!hasJumped) {
                setVerticalSpeed(-Constants.GAME_SPEED);
                hasJumped = true;
                moves++;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isMoving = false;

        // make Player move down if in wave mode
        if (playerMode == 2) {
            setVerticalSpeed(Constants.GAME_SPEED);
            hasJumped = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // unused but must be overriden
    }

}
