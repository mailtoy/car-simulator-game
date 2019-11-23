package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;

public class SimulatorCamera extends Camera {

	private Vector3f position = new Vector3f(0, 500, 0);
	private int round;

	public SimulatorCamera() {
		setPosition(position);
		setPitch(90);
	}

	@Override
	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.z -= 5f;
			if (position.z < 200) {
				position.z = 200;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.z += 5f;
			if (position.z >= (round * 800) ) {
				position.z = round * 800;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			position.x += 5f;
			if (position.x >= (round * 800) ) {
				position.x = round * 800;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.x -= 5f;
			if (position.x < 300) {
				position.x = 300;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 5f;
			if (position.y >= 920) {
				position.y = 928;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= 5f;
			if (position.y <= 30) {
				position.y = 30;
			}
		}
	}

	@Override
	public void setRound(int round) {
		this.round = round;

	}

}