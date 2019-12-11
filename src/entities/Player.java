package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
	private String endMapState;
	protected Vector3f frame;

	public Player(String type, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.type = type;
		if (model != null || scale != 0.0) {
			setFrame();
		}
	}

	public void move() {
		checkInputs();
		checkEndMap();
		setFrame();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
	}

	protected void checkInputs() {
//		System.out.println("x: " + Mouse.getX());
//		System.out.println("y: " + Mouse.getY());
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || (Mouse.getX() <= 247 && Mouse.getX() >= 88 && Mouse.getY() <= 265
				&& Mouse.getY() >= 230 && Mouse.isButtonDown(0))) {
			if (this.speedIncrease < MAX_ACC) {
				this.speedIncrease += 0.5;
			}
		} else {
			if (this.speedIncrease != 0) {
				this.speedIncrease -= 0.5;
			}
		}

		// fixed mouse detection here
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) || (Mouse.getX() <= 1110 && Mouse.getX() >= 1055 && Mouse.getY() <= 285
				&& Mouse.getY() >= 245 && Mouse.isButtonDown(0))) {
			this.currentSpeed = RUN_SPEED + this.speedIncrease;
			endMapState = "Forward";
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || (Mouse.getX() <= 1107 && Mouse.getX() >= 1060
				&& Mouse.getY() <= 165 && Mouse.getY() >= 125 && Mouse.isButtonDown(0))) {
			this.currentSpeed = -RUN_SPEED;
			this.speedIncrease = 0;
			endMapState = "Backward";
		} else {
			if (this.currentSpeed > 0) {
				this.currentSpeed -= 0.5;
			} else if (this.currentSpeed < 0) {
				this.currentSpeed += 0.5;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || (Mouse.getX() <= 1170 && Mouse.getX() >= 1120
				&& Mouse.getY() <= 230 && Mouse.getY() >= 186 && Mouse.isButtonDown(0))) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || (Mouse.getX() <= 1037 && Mouse.getX() >= 992
				&& Mouse.getY() <= 228 && Mouse.getY() >= 186 && Mouse.isButtonDown(0))) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
	}

	private void setFrame() {
		frame = new Vector3f(getPosition().getX() - 4, 0, getPosition().getZ() - 8);
	}

	public Vector3f getFrame() {
		return this.frame;
	}

	public String getType() {
		return this.type;
	}

	public float getCurrentSpeed() {
		return this.currentSpeed;
	}

	public void setCurrentSpeed(int currentSpeed) {
		this.currentSpeed = currentTurnSpeed;
	}

	public void checkEndMap() {
		if (this.getPosition().getX() <= 8 || this.getPosition().getZ() <= 8 || this.getPosition().getX() >= 3112
				|| this.getPosition().getZ() >= 3112) {
			if(endMapState.equals("Forward")) {
				this.currentSpeed = -5;
			} else if(endMapState.equals("Backward")) {
				this.currentSpeed = +5;
			}
			
		}
	}
}