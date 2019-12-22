package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

/**
 * Player is a car on display that it is controlled by user as controller.
 * 
 * @author Kanchanok Kannee
 *
 */
public class Player extends Entity {
	private static final float RUN_SPEED = 1f;
	private static final float TURN_SPEED = 45;
	private static final float AVERAGE_SPEED = 60;
	private static final float MAX_SPEED = 180;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	private String type;
	private String color;
	protected Vector3f frame;
	protected Vector2f position;

	private boolean isActive = false;
	private boolean isActiveLR = false;
	private boolean isOption = false;
	private String arrow = "none";
	private String arrowLR = "none";
	private String option = "none";

	/**
	 * Constructor of the Player
	 */
	public Player(String type, String color, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.type = type;
		this.color = color;
	}

	/**
	 * Calculating the position of player
	 */
	public void move() {
		checkInputs();
		checkEndMap();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
	}

	/**
	 * Checking the input command from keyboard and mouse that click on screen.
	 */
	protected void checkInputs() {
		setActive(false);
		setActiveLR(false);
		setActiveOption(false);

		boolean isForward = Keyboard.isKeyDown(Keyboard.KEY_UP) || isPressButton(0.73, 0.64, -0.39, -0.27);
		boolean isBackward = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || isPressButton(0.73, 0.64, -0.69, -0.57);
		boolean isLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || isPressButton(0.69, 0.49, -0.55, -0.42);
		boolean isRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || isPressButton(0.83, 0.74, -0.55, -0.42);
		boolean isAccelerate = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || isPressButton(-0.52, -0.65, -0.63, -0.43);
		boolean isBrake = Keyboard.isKeyDown(Keyboard.KEY_SPACE) || isPressButton(-0.70, -0.79, -0.63, -0.45);
		if (!isBrake) {
			// accelerate speed
			if (isAccelerate && isForward) {
				currentSpeed += (currentSpeed < MAX_SPEED) ? RUN_SPEED : 0;
				setOption("acc");
				setActiveOption(true);
			} else if (isAccelerate && isBackward) {
				currentSpeed += (currentSpeed > -MAX_SPEED) ? -RUN_SPEED : 0;
				setOption("acc");
				setActiveOption(true);
			} else if (!isAccelerate && currentSpeed > AVERAGE_SPEED) {
				currentSpeed += -RUN_SPEED;
			} else if (isAccelerate && currentSpeed < -AVERAGE_SPEED) {
				currentSpeed += RUN_SPEED;
			}

			// direction movement
			if (isForward) {
				currentSpeed += ((currentSpeed < AVERAGE_SPEED) ? RUN_SPEED : 0);
				setArrow("forward");
				setActive(true);
			} else if (isBackward) {
				currentSpeed += ((currentSpeed > -AVERAGE_SPEED) ? -RUN_SPEED : 0);
				setArrow("backward");
				setActive(true);
			} else {
				currentSpeed += ((currentSpeed > 0) ? -RUN_SPEED : (currentSpeed < 0) ? RUN_SPEED : 0);
			}

			// turn movement
			if (currentSpeed != 0 && isRight && !isLeft) {
				currentTurnSpeed = -TURN_SPEED;
				setArrowLR("right");
				setActiveLR(true);
			} else if (currentSpeed != 0 && isLeft && !isRight) {
				currentTurnSpeed = TURN_SPEED;
				setArrowLR("left");
				setActiveLR(true);
			} else {
				currentTurnSpeed = 0;
			}

		} else {
			// brake
			currentSpeed += (currentSpeed > 0)
					? (currentSpeed == RUN_SPEED * 2 ? -RUN_SPEED * 2
							: (currentSpeed == RUN_SPEED) ? -RUN_SPEED : -RUN_SPEED * 3)
					: (currentSpeed < 0) ? (currentSpeed == -RUN_SPEED * 2 ? RUN_SPEED * 2
							: (currentSpeed == -RUN_SPEED) ? RUN_SPEED : RUN_SPEED * 3) : 0;
			setOption("brake");
			setActiveOption(true);
		}
	}

	/**
	 * Checking the button is clicked or not.
	 * 
	 * @param x1
	 *            maximum value of x-axis
	 * @param x2
	 *            minimum value of x-axis
	 * @param y1
	 *            minimum value of y-axis
	 * @param y2
	 *            maximum value of y-axis
	 * @return boolean, true when click and put mouse on correct position
	 */
	private boolean isPressButton(double x1, double x2, double y1, double y2) {
		float mouseXCoords = (2f * Mouse.getX()) / Display.getWidth() - 1f;
		float mouseYCoords = (2f * Mouse.getY()) / Display.getHeight() - 1f;
		boolean isBtnDown = Mouse.isButtonDown(0);
		return (mouseXCoords <= x1 && mouseXCoords >= x2 && mouseYCoords >= y1 && mouseYCoords <= y2 && isBtnDown);
	}

	/**
	 * To check when the car is hit the end of map. So the car cannot be run out
	 * map.
	 * 
	 */
	private void checkEndMap() {
		float x = getPosition().getX();
		float z = getPosition().getZ();
		if (x < 8) {
			updatePlayerPosition(x + 5, 0, z);
			currentSpeed = 1;
		}
		if (z < 8) {
			updatePlayerPosition(x, 0, z + 5);
			currentSpeed = 1;
		}
		if (x > 3192) {
			updatePlayerPosition(x - 5, 0, z);
			currentSpeed = 1;
		}
		if (z > 3192) {
			updatePlayerPosition(x, 0, z - 5);
			currentSpeed = 1;
		}
	}

	/**
	 * For easy to update player position when it needed.
	 * 
	 * @param x
	 *            for x position
	 * @param y
	 *            for y position
	 * @param z
	 *            for z position
	 */
	public void updatePlayerPosition(float x, float y, float z) {
		setPosition(new Vector3f(x, y, z));
	}

	/**
	 * Setting forward and backward active or not.
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.isActive = active;
	}

	/**
	 * Return true if button is clicked otherwise, return false.
	 * 
	 * @return boolean
	 */
	public boolean getActive() {
		return this.isActive;
	}

	/**
	 * Setting destination of the play for forward and backward
	 * 
	 * @param arrow
	 *            name of destination
	 */
	public void setArrow(String arrow) {
		this.arrow = arrow;
	}

	/**
	 * Return name of destination
	 * 
	 * @return destination
	 */
	public String getArrow() {
		return this.arrow;
	}

	/**
	 * Setting left and right active or not.
	 * 
	 * @param active
	 */
	public void setActiveLR(boolean active) {
		this.isActiveLR = active;
	}

	/**
	 * Return true if button is clicked otherwise, return false.
	 * 
	 * @return boolean
	 */
	public boolean getActiveLR() {
		return this.isActiveLR;
	}

	/**
	 * Setting destination of the play for left and right
	 * 
	 * @param arrow
	 *            name of destination
	 */
	public void setArrowLR(String arrow) {
		this.arrowLR = arrow;
	}

	/**
	 * Return name of destination
	 * 
	 * @return destination
	 */
	public String getArrowLR() {
		return this.arrowLR;
	}

	/**
	 * Setting accelerate and brake active or not.
	 * 
	 * @param active
	 */
	public void setActiveOption(boolean option) {
		this.isOption = option;
	}

	/**
	 * Return true if button is clicked otherwise, return false.
	 * 
	 * @return boolean
	 */
	public boolean getOptionActive() {
		return this.isOption;
	}

	/**
	 * Setting option of the play for brake and accelerate
	 * 
	 * @param arrow
	 *            name of options
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * Return name of option
	 * 
	 * @return options
	 */
	public String getOption() {
		return this.option;
	}

	/**
	 * Create a frame of player
	 */
	public void setFrame() {
		frame = new Vector3f(getPosition().getX() - 3.5f, 0, getPosition().getZ() - 7.5f);
	}

	/**
	 * Return a frame of player
	 * 
	 * @return
	 */
	public Vector3f getFrame() {
		return this.frame;
	}

	/**
	 * Return name of player
	 * 
	 * @return name
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Get color of play
	 * 
	 * @return color
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * Setting current speed of player
	 * 
	 * @param currentSpeed
	 */
	public void setCurrentSpeed(float currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	/**
	 * Return current speed of player
	 * 
	 * @return current speed
	 */
	public float getCurrentSpeed() {
		return this.currentSpeed;
	}

	/**
	 * Checking the replay button is clicked or not.
	 * 
	 * @return boolean, true when click and put mouse on correct position
	 */
	public boolean isReplay() {
		return isPressButton(-0.06, -0.3, 0.06, 0.3);
	}

	/**
	 * Checking the quit button is clicked or not.
	 * 
	 * @return boolean, true when click and put mouse on correct position
	 */
	public boolean isQuit() {
		return isPressButton(0.28, 0.05, 0.06, 0.3);
	}

}