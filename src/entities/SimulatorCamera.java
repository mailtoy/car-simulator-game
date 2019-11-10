package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class SimulatorCamera extends Camera {

	private Vector3f position = new Vector3f(0, 5, 0);
	
	public SimulatorCamera() {
		setPosition(new Vector3f(100, 100, -100));
		setPitch(90);
	}

	@Override
	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.z -= 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.z += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			position.x += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.x -= 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= 0.2f;
		}
	}

}