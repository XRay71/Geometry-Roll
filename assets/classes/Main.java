/*
 * Ray Hang, Rain Yeyang
 * Date: June 21, 2022
 * Initiates the Geometry Roll game
 */
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main {

	public static GameFrame gameFrame;

	public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

		// run GameFrame constructor
		gameFrame = new GameFrame();

	}
}
