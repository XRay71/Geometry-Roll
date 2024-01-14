/*
 * Rain Yeyang, Ray Hang
 * Date: June 21, 2022
 * Contains constants used throughout Geometry Roll
 */
import java.awt.Color;

public class Constants {

	// game constants
	public static final int GAME_HEIGHT = 600;
	public static final int GAME_WIDTH = 1000;
	public static final int GRID_UNIT = 40;
	public static final int[] PLAYER_WIDTHS = { 30, 30, 15, 30 };
	public static final int[] PLAYER_HEIGHTS = { 30, 15, 15, 30 };
	public static final int[] LEVEL_LENGTHS = { 16440, 21760, 40840 };
	public static final double[] PLAYER_STARTS = { GAME_HEIGHT - PLAYER_HEIGHTS[0],
			GAME_HEIGHT / 2.0 - PLAYER_HEIGHTS[1], PLAYER_HEIGHTS[2] };
	public static final double GRAVITY = -5.7 / 5.0 * 0.5;

	// game audio
	public static final String DEFAULT_AUDIO = "../../audio/default.wav";
	public static final String LVL1_AUDIO = "../../audio/lvl1.wav";
	public static final String LVL2_AUDIO = "../../audio/lvl2.wav";
	public static final String LVL3_AUDIO = "../../audio/lvl3.wav";
	public static final String[] LVL_AUDIO = { LVL1_AUDIO, LVL2_AUDIO, LVL3_AUDIO };
	public static final String LVL_START_AUDIO = "../../audio/start.wav";
	public static final String SUCCESS_AUDIO = "../../audio/success.wav";
	public static final String DEATH_AUDIO = "../../audio/death.wav";
	public static final String THE_GREGG_AUDIO = "../../audio/gregg.wav";

	// background images
	public static final String LVL1_BACKGROUND = "../../images/lvl1_background.png";
	public static final String LVL2_BACKGROUND = "../../images/lvl2_background.png";
	public static final String MENU_BACKGROUND = "../../images/main_menu_background.png";
	public static final String RULES = "../../images/rules.png";

	// game speed
	public static int GAME_SPEED = 6;

	// player sprites
	public static final String PLAYER_MODE_ZERO_SPRITE = "../../images/playermode_zero.png";
	public static final String PLAYER_MODE_ONE_SPRITE = "../../images/playermode_one.png";
	public static final String PLAYER_MODE_TWO_SPRITE = "../../images/playermode_two.png";
	public static final String PLAYER_MODE_THREE_SPRITE = "../../images/playermode_three.png";

	// obstacle images
	public static final String SQUARE = "../../images/obst_square.png";
	public static final String UPRIGHT_SPIKE = "../../images/obst_spike_up.png";
	public static final String UPSIDE_DOWN_SPIKE = "../../images/obst_spike_down.png";
	public static final String LEFT_UPRIGHT_DIAGONAL = "../../images/obst_left_triangle_up.png";
	public static final String LEFT_UPSIDE_DOWN_DIAGONAL = "../../images/obst_left_triangle_down.png";
	public static final String RIGHT_UPRIGHT_DIAGONAL = "../../images/obst_right_triangle_up.png";
	public static final String RIGHT_UPSIDE_DOWN_DIAGONAL = "../../images/obst_right_triangle_down.png";

	// obstacle images with the Gregg
	public static final String GREGG_ACTIVATE = "../../images/gregg_block.png";
	public static final String[] OBSTACLES = { SQUARE, UPRIGHT_SPIKE, UPSIDE_DOWN_SPIKE, LEFT_UPRIGHT_DIAGONAL,
		LEFT_UPSIDE_DOWN_DIAGONAL, RIGHT_UPRIGHT_DIAGONAL, RIGHT_UPSIDE_DOWN_DIAGONAL, GREGG_ACTIVATE };
	public static final String GREGG_BLOCK = "../../images/gregg_block.png";
	public static final String GREGG_SPIKE_UP = "../../images/gregg_spike_up.png";
	public static final String GREGG_SPIKE_DOWN = "../../images/gregg_spike_down.png";
	public static final String[] GREGGED = {GREGG_BLOCK, GREGG_SPIKE_UP, GREGG_SPIKE_DOWN};

	// theme colors
	public static final Color YELLOW = new Color(241, 194, 50);
	public static final Color PURPLE = new Color(64, 5, 111);
	public static final Color RED = new Color(90, 0, 0);

}