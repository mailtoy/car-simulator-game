package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
//			if (position.x >= ((round+1) * 800) ) {
//				position.x = (round+1) * 800;
//			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.x -= 5f;
//			if (position.x < 0) {
//				position.x = 0;
//			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Mouse.getX() <= 1338 && Mouse.getX() >= 1256
				&& Mouse.getY() <= 730 && Mouse.getY() >= 663 && Mouse.isButtonDown(0)) {
			position.y += 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Mouse.getX() <= 1338 && Mouse.getX() >= 1256
				&& Mouse.getY() <= 419 && Mouse.getY() >= 344 && Mouse.isButtonDown(0)) {
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
			position.y = 928;
		}
		if (position.y <= 30) {
			position.y = 30;
		}
	}

}