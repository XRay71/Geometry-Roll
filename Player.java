/*
Ray Hang, Rain Yeyang
Date: June 16, 2022

*/
import java.awt.*;
import java.awt.event.*;

public class Player extends Rectangle implements KeyListener {

	private int playerMode = 0;
    private int diameter;
    private boolean doubleMode = false;
    private double horizontalSpeed;
	private double verticalSpeed;
	private double xPosition;
	private double yPosition;

    public Player(double xPosition, double yPosition, int diameter, double horizontalSpeed, double verticalSpeed) {
        super((int) Math.round(xPosition), (int) Math.round(yPosition), diameter, diameter);
        this.horizontalSpeed = horizontalSpeed;
        this.verticalSpeed = verticalSpeed;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.diameter = diameter;
    }

    public void setMode(int playerMode) {
        this.playerMode = playerMode;
    }

    public void setdiameter(int diameter) {
        this.diameter = diameter;
    }

    public void setHorizontalSpeed(double horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    public void setSpeed(double horizontalSpeed, double verticalSpeed) {
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
        return new double[] {xPosition, x};
    }

    public double[] getVerticalPosition() {
        return new double[] {yPosition, y};
    }

    public double[][] getPosition() {
        return new double[][] {{xPosition, yPosition}, {x, y}}; 
    }

    public double getHorizontalSpeed() {
        return horizontalSpeed;
    }

    public double getVerticalSpeed() {
        return verticalSpeed;
    }

    public double[] getSpeed() {
        return new double[] {horizontalSpeed, verticalSpeed};
    }

    public void keyPressed(KeyEvent e) {

    } 

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {}
	
}
