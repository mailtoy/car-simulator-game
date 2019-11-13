package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class SimulatorCamera extends Camera {

	private Vector3f position = new Vector3f(205, 30, -270);

	public SimulatorCamera() {
		setPosition(position);
		setPitch(90);
	}

	@Override
	public void move() {
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
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 5f;
			if (position.y >= 920) {
				position.y = 928;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= 5f;
			if (position.y <= 30 ) {
				position.y = 30;
			}
		}
	}

}