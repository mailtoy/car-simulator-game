package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

public class Player extends Entity {
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 30;
	private static final float MAX_ACC = 80;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float speedIncrease = 0;

	private String type;

	public Player(String type, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.type = type;
	}

	private void move() {
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
	}

	public void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (this.speedIncrease < MAX_ACC) {
				this.speedIncrease += 0.5;
			}
		} else {
			if (this.speedIncrease != 0) {
				this.speedIncrease -= 0.5;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			this.currentSpeed = RUN_SPEED + this.speedIncrease;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			if (this.currentSpeed > 0) {
				this.currentSpeed -= 0.5;
			} else {
				this.currentSpeed = 0;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		move();
	}

	public void checkInputs(String button) {
		if (button.equals("^")) {
			this.currentSpeed = RUN_SPEED;
		} else if (button.equals("v")) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}

		if (button.equals(">")) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (button.equals("<")) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		move();
	}

	public String getType() {
		return this.type;
	}

	public float getCurrentSpeed() {
		return this.currentSpeed;
	}
}
