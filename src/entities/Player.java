package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

public class Player extends Entity {
	private static final float RUN_SPEED = 1f;
	private static final float TURN_SPEED = 45;
	private static final float MAX_SPEED = 60;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	private String type;
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
		setFrame();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
	}

	protected void checkInputs() {
		// fixed mouse detection here
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		boolean isBtnDown = Mouse.isButtonDown(0);

		currentSpeed += (Keyboard.isKeyDown(Keyboard.KEY_UP)
				|| (mouseX <= 1110 && mouseX >= 1055 && mouseY <= 285 && mouseY >= 245 && isBtnDown))
						? ((currentSpeed <= MAX_SPEED) ? RUN_SPEED : 0)
						: (Keyboard.isKeyDown(Keyboard.KEY_DOWN)
								|| (mouseX <= 1107 && mouseX >= 1060 && mouseY <= 165 && mouseY >= 125 && isBtnDown))
										? ((currentSpeed >= -MAX_SPEED) ? -RUN_SPEED * 2 : 0)
										: ((currentSpeed > 0) ? -RUN_SPEED : (currentSpeed < 0) ? RUN_SPEED : 0);

		currentTurnSpeed = (currentSpeed != 0 && Keyboard.isKeyDown(Keyboard.KEY_RIGHT)
				|| (mouseX <= 1170 && mouseX >= 1120 && mouseY <= 230 && mouseY >= 186 && isBtnDown))
						? -TURN_SPEED
						: (currentSpeed != 0 && Keyboard.isKeyDown(Keyboard.KEY_LEFT)
								|| (mouseX <= 1037 && mouseX >= 992 && mouseY <= 228 && mouseY >= 186 && isBtnDown))
										? TURN_SPEED
										: 0;
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
}