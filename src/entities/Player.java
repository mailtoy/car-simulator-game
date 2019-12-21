package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

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
		boolean isForward = Keyboard.isKeyDown(Keyboard.KEY_UP);
		boolean isBackward = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		boolean isLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		boolean isRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		boolean isAccelerate = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		boolean isBrake = Keyboard.isKeyDown(Keyboard.KEY_SPACE);

		if (!isBrake) {
			// accelerate
			currentSpeed += (isAccelerate && isForward) ? ((currentSpeed < MAX_SPEED) ? RUN_SPEED : 0)
					: (isAccelerate && isBackward) ? ((currentSpeed > -MAX_SPEED) ? -RUN_SPEED : 0) : 0;
			
			// direction movement
			if (isForward || isPressButton(0.73, 0.64, -0.39, -0.27)) {
				currentSpeed += ((currentSpeed < AVERAGE_SPEED) ? RUN_SPEED : 0);
				direction = FORWARD;
			} else if (isBackward || isPressButton(0.73, 0.64, -0.69, -0.57)) {
				currentSpeed += ((currentSpeed > -AVERAGE_SPEED) ? -RUN_SPEED : 0);
				direction = BACKWARD;
			} else {
				currentSpeed += ((currentSpeed > 0) ? -RUN_SPEED : (currentSpeed < 0) ? RUN_SPEED : 0);
			}
			
			// turn movement
			currentTurnSpeed = (currentSpeed != 0 && (isRight || isPressButton(0.83, 0.74, -0.55, -0.42))) ? -TURN_SPEED
					: (currentSpeed != 0 && (isLeft || isPressButton(0.69, 0.49, -0.55, -0.42))) ? TURN_SPEED : 0;
		} else {
			// brake
			currentSpeed += (currentSpeed > 0)
					? (currentSpeed == RUN_SPEED * 2 ? -RUN_SPEED * 2
							: (currentSpeed == RUN_SPEED) ? -RUN_SPEED : -RUN_SPEED * 3)
					: (currentSpeed < 0) ? (currentSpeed == -RUN_SPEED * 2 ? RUN_SPEED * 2
							: (currentSpeed == -RUN_SPEED) ? RUN_SPEED : RUN_SPEED * 3) : 0;
		}
	}

	private boolean isPressButton(double d, double e, double f, double g) {
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

	public float getCurrentSpeed() {
		return this.currentSpeed;
	}
}