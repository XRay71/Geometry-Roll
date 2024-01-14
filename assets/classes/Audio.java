/*
 * Ray Hang, Rain Yeyang
 * Date: June 21, 202
 * Plays audio files
 */
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {

	public Clip clip;

	public Audio(String file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		// initialize clip with corresponding audio file
		File soundFile = new File(file);
		AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
		clip = AudioSystem.getClip();
		clip.open(sound);
	}

	public void play() {
		// play clip from the beginning
		clip.setFramePosition(0);
		clip.start();
	}

	public void loop() {
		// continue looping
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
	}

	public void setVolume(float volume) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		// change volume of clip
		gainControl.setValue(20f * (float) Math.log10(volume));
	}
}
