package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class SimulatorCamera extends Camera {

	private Vector3f position = new Vector3f(305, 200, -80);
	
	public SimulatorCamera() {
		setPosition(position);
		setPitch(90);
	}

	@Override
	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.z -= 1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.z += 1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			position.x += 1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.x -= 1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= 1f;
		}
	}

}