package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity {
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move() {
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
	}

	public void setCurrentSpeed(float currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public void setCurrentTurnSpeed(float currentTurnSpeed) {
		this.currentTurnSpeed = currentTurnSpeed;
	}

//	public void checkInputsClick(int check) {
//		if (check == 1) {
//			this.currentSpeed = RUN_SPEED;
//			super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
//			float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
//			float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
//			float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
//			super.increasePosition(dx, 0, dz);
//		} else {
//			this.currentSpeed = 0;
//		}
//
//		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
//			this.currentTurnSpeed = -TURN_SPEED;
//		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
//			this.currentTurnSpeed = TURN_SPEED;
//		} else {
//			this.currentTurnSpeed = 0;
//		}
//	}

}
