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
	private static final float MAX_SPEED = 180;

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

		if (model != null || scale != 0.0) {
			setFrame();
		}
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
		boolean isBtnDown = Mouse.isButtonDown(0);

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			currentSpeed += ((currentSpeed <= MAX_SPEED) ? RUN_SPEED : 0);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			currentSpeed += ((currentSpeed >= -MAX_SPEED) ? -RUN_SPEED * 2 : 0);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			//TODO
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || (getMouseXCoords() <= 0.73 && getMouseXCoords() >= 0.64
				&& getMouseYCoords() >= -0.39 && getMouseYCoords() <= -0.27 && isBtnDown)) {
			currentSpeed += ((currentSpeed < 60) ? RUN_SPEED : 0);
			direction = "forward";
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || (getMouseXCoords() <= 0.73 && getMouseXCoords() >= 0.64
				&& getMouseYCoords() >= -0.69 && getMouseYCoords() <= -0.57 && isBtnDown)) {
			currentSpeed += ((currentSpeed > -60) ? -RUN_SPEED * 2 : 0);
			direction = "backward";
		} else {
			currentSpeed += ((currentSpeed > 0) ? -RUN_SPEED : (currentSpeed < 0) ? RUN_SPEED : 0);
		}

		currentTurnSpeed = (currentSpeed != 0 && (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || (getMouseXCoords() <= 0.83
				&& getMouseXCoords() >= 0.74 && getMouseYCoords() >= -0.55 && getMouseYCoords() <= -0.42 && isBtnDown)))
						? -TURN_SPEED
						: (currentSpeed != 0 && (Keyboard.isKeyDown(Keyboard.KEY_LEFT)
								|| (getMouseXCoords() <= 0.63 && getMouseXCoords() >= 0.54 && getMouseYCoords() >= -0.55
										&& getMouseYCoords() <= -0.42 && isBtnDown))) ? TURN_SPEED : 0;
	}

	private static float getMouseXCoords() {
		float x = (2f * Mouse.getX()) / Display.getWidth() - 1f;
		return x;
	}

	private static float getMouseYCoords() {
		float y = (2f * Mouse.getY()) / Display.getHeight() - 1f;
		return y;
	}

	private void checkEndMap() {
		float x = getPosition().getX();
		float z = getPosition().getZ();
		if (x <= 8 || z <= 8 || x >= 3192 || z >= 3192) {
			currentSpeed = (direction.equals("forward")) ? -5 : (direction.equals("backward")) ? 5 : 0;
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

	public void setCurrentSpeed(int currentSpeed) {
		this.currentSpeed = currentTurnSpeed;
	}

	public float getCurrentTurnSpeed() {
		return this.currentTurnSpeed;
	}
}