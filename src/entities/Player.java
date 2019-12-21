package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import handlers.ControllerHandler;
import handlers.Handler;
import main.Controller;
import models.TexturedModel;
import renderEngine.DisplayManager;

public class Player extends Entity {
	private static final float RUN_SPEED = 1f;
	private static final float TURN_SPEED = 45;
	private static final float AVERAGE_SPEED = 60;
	private static final float MAX_SPEED = 180;

	private static final String FORWARD = "forward";
	private static final String BACKWARD = "backward";

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private String direction;

	private String type;
	private String color;
	protected Vector3f frame;
	protected Vector2f position;
	private boolean isActive = false;
	private boolean isActiveLR = false;
	private boolean isOption = false;
	private int arrow;
	private int arrowLR;
	private int option;

	public Player(String type, String color, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.type = type;
		this.color = color;
	}

	public void move() {
		checkInputs();
		checkEndMap();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
	}

	protected void checkInputs() {
		setActive(false);
		setActiveLR(false);
		setOption(false);
		// check x and y
		// float mouseXCoords = (2f * Mouse.getX()) / Display.getWidth() - 1f;
		// float mouseYCoords = (2f * Mouse.getY()) / Display.getHeight() - 1f;
		// System.out.println("x: " + mouseXCoords);
		// System.out.println("y: " + mouseYCoords);

		boolean isForward = Keyboard.isKeyDown(Keyboard.KEY_UP) || isPressButton(0.73, 0.64, -0.39, -0.27);
		boolean isBackward = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || isPressButton(0.73, 0.64, -0.69, -0.57);
		boolean isLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || isPressButton(0.69, 0.49, -0.55, -0.42);
		boolean isRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || isPressButton(0.83, 0.74, -0.55, -0.42);
		boolean isAccelerate = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		boolean isBrake = Keyboard.isKeyDown(Keyboard.KEY_SPACE) || isPressButton(-0.70, -0.79, -0.63, -0.45);

		if (!isBrake) {
			// accelerate
			currentSpeed += ((isAccelerate || isPressButton(-0.52, -0.65, -0.63, -0.43)) && isForward)
					? ((currentSpeed < MAX_SPEED) ? RUN_SPEED : 0)
					: (isAccelerate && isBackward) ? ((currentSpeed > -MAX_SPEED) ? -RUN_SPEED : 0)
							: (!isAccelerate && currentSpeed > AVERAGE_SPEED) ? -RUN_SPEED
									: (!isAccelerate && currentSpeed < -AVERAGE_SPEED) ? +RUN_SPEED : 0;
			if ((isAccelerate || isPressButton(-0.52, -0.65, -0.63, -0.43)) && isForward) {
				setOption(6);
				setOption(true);
			} else if ((isAccelerate || isPressButton(-0.52, -0.65, -0.63, -0.43)) && isBackward) {
				setOption(6);
				setOption(true);
			}

			// direction movement
			if (isForward) {
				currentSpeed += ((currentSpeed < AVERAGE_SPEED) ? RUN_SPEED : 0);
				direction = FORWARD;
				setArrow(1);
				setActive(true);
			} else if (isBackward) {
				currentSpeed += ((currentSpeed > -AVERAGE_SPEED) ? -RUN_SPEED : 0);
				direction = BACKWARD;
				setArrow(2);
				setActive(true);
			} else {
				currentSpeed += ((currentSpeed > 0) ? -RUN_SPEED : (currentSpeed < 0) ? RUN_SPEED : 0);
			}

			// turn movement
			currentTurnSpeed = (currentSpeed != 0 && isRight) ? -TURN_SPEED
					: (currentSpeed != 0 && isLeft) ? TURN_SPEED : 0;
			if (isRight || isPressButton(0.83, 0.74, -0.55, -0.42)) {
				setArrowLR(3);
				setActiveLR(true);
			} else if (isLeft || isPressButton(0.69, 0.49, -0.55, -0.42)) {
				setArrowLR(4);
				setActiveLR(true);
			}

		} else {
			// brake
			currentSpeed += (currentSpeed > 0)
					? (currentSpeed == RUN_SPEED * 2 ? -RUN_SPEED * 2
							: (currentSpeed == RUN_SPEED) ? -RUN_SPEED : -RUN_SPEED * 3)
					: (currentSpeed < 0) ? (currentSpeed == -RUN_SPEED * 2 ? RUN_SPEED * 2
							: (currentSpeed == -RUN_SPEED) ? RUN_SPEED : RUN_SPEED * 3) : 0;
			setOption(5);
			setOption(true);

		}
	}

	public boolean isPressButton(double d, double e, double f, double g) {
		float mouseXCoords = (2f * Mouse.getX()) / Display.getWidth() - 1f;
		float mouseYCoords = (2f * Mouse.getY()) / Display.getHeight() - 1f;
		boolean isBtnDown = Mouse.isButtonDown(0);
		return (mouseXCoords <= d && mouseXCoords >= e && mouseYCoords >= f && mouseYCoords <= g && isBtnDown);
	}

	private void checkEndMap() {
		float x = getPosition().getX();
		float z = getPosition().getZ();
		if (x <= 8 || z <= 8 || x >= 3192 || z >= 3192) {
			currentSpeed = (direction.equals(FORWARD)) ? -5 : (direction.equals(BACKWARD)) ? 5 : 0;
		}
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public boolean getActive() {
		return this.isActive;
	}

	public void setArrow(int arrow) {
		this.arrow = arrow;
	}

	public int getArrow() {
		return this.arrow;
	}

	public void setActiveLR(boolean active) {
		this.isActiveLR = active;
	}

	public boolean getActiveLR() {
		return this.isActiveLR;
	}

	public void setArrowLR(int arrow) {
		this.arrowLR = arrow;
	}

	public int getArrowLR() {
		return this.arrowLR;
	}

	public void setOption(boolean option) {
		this.isOption = option;
	}

	public boolean getOptionActive() {
		return this.isOption;
	}

	public void setOption(int option) {
		this.option = option;
	}

	public int getOption() {
		return this.option;
	}

	public void setFrame() {
		frame = new Vector3f(getPosition().getX() - 3.5f, 0, getPosition().getZ() - 7.5f);
	}

	public Vector3f getFrame() {
		return this.frame;
	}

	public String getType() {
		return this.type;
	}

	public String getColor() {
		return this.color;
	}

	public void setCurrentSpeed(float currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public float getCurrentSpeed() {
		return this.currentSpeed;
	}
}