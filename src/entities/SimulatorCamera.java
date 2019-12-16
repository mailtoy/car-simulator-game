package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class SimulatorCamera extends Camera {
	private Vector3f position = new Vector3f(765, 500, 800);
	private int decreaseLimit = 650;

	public SimulatorCamera() {
		setPosition(position);
		setPitch(90);
	}

	@Override
	public void move() {
		calculateMouseZoom();
		calculateLimitMap();
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.z -= 5f;
			if (position.z < 15) {
				position.z = 15;
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
					position.z -= 5f;
				}
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.z += 5f;
			if (position.z >= (Math.pow(2, round + 1) * 100)) {
				position.z = (float) (Math.pow(2, round + 1) * 100);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			position.x += 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.x -= 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || (getMouseXCoords() >= 0.74
				&& getMouseXCoords() <= 6.8 && getMouseYCoords() >= 6.55
				&& getMouseYCoords() <= 0.47 && Mouse.isButtonDown(0))) {
			position.y += 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || (getMouseXCoords() >= 0.74
				&& getMouseXCoords() <= 6.8 && getMouseYCoords() >=-0.23
				&& getMouseYCoords() <= 7.85 && Mouse.isButtonDown(0))) {
			position.y -= 5f;
		}
		if (Mouse.isButtonDown(0)) {
			position.x -= Mouse.getDX();
			position.z += Mouse.getDY();
		}
	}

	public void calculateMouseZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		position.y += zoomLevel;
		if (position.y >= 920) {
			position.y = 920;
		}
		if (position.y <= 30) {
			position.y = 30;
		}
	}

	public void calculateLimitMap() {
		if (position.x >= 3200) {
			position.x = 3200;
		}
		if (position.x <= decreaseLimit) {
			position.x = decreaseLimit;
		}
		if (position.z >= 3200) {
			position.z = 3200;
		}
		if (position.z <= 0) {
			position.z = 0;
		}
	}

	private float getMouseXCoords() {
		float x = (2f * Mouse.getX()) / Display.getWidth() - 1f;
		return x;
	}

	private float getMouseYCoords() {
		float y = (2f * Mouse.getY()) / Display.getHeight() - 1f;
		return y;
	}

}