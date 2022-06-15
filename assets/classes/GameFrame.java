/*
Rain Yeyang, Ray Hang
Date: June 16, 2022
Creates window frame for Geometry Roll
Child of JFrame
*/
import java.io.IOException;
import javax.swing.*;

public class GameFrame extends JFrame {

	Menu menu;
	
	public GameFrame() throws IOException {
		menu = new Menu(); // create main menu
		this.add(menu);
		this.setTitle("Geometry Roll!"); // set title for frame
		this.setResizable(false); // do no allow frame to change size
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // halt program execution when window is closed
		this.pack(); // fit components into window
		this.setVisible(true); // make window visible to user
		this.setLocationRelativeTo(null); // create window in center of screen
	}
	
}
