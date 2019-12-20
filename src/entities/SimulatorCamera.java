package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class SimulatorCamera extends Camera {
	private Vector3f position = new Vector3f(765, 500, 800);

	public SimulatorCamera() {
		setPosition(position);
		setPitch(90);
	}

	@Override
	public void move() {
		calculateMouseZoom();
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.z -= 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.z += 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			position.x += 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.x -= 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || (getMouseXCoords() >= 0.74
				&& getMouseXCoords() <= 0.86 && getMouseYCoords() >= 0.47
				&& getMouseYCoords() <= 0.63 && Mouse.isButtonDown(0))) {
			position.y += 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || (getMouseXCoords() >= 0.74
				&& getMouseXCoords() <= 0.86 && getMouseYCoords() >=-0.23
				&& getMouseYCoords() <= -0.07 && Mouse.isButtonDown(0))) {
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

	private float getMouseXCoords() {
		float x = (2f * Mouse.getX()) / Display.getWidth() - 1f;
		return x;
	}

	private float getMouseYCoords() {
		float y = (2f * Mouse.getY()) / Display.getHeight() - 1f;
		return y;
	}

}