package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;

public class SimulatorCamera extends Camera {

	private Vector3f position = new Vector3f(765, 500, 800);
	private int round;

	public SimulatorCamera() {
		setPosition(position);
		setPitch(90);
	}

	@Override
	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.z -= 5f;
			if (position.z < 15) {
				position.z = 15;
				 if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
						position.z -= 5f;
					}
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.z += 5f;
			if (position.z >= (Math.pow(2, round+1)*100) ) {
				position.z = (float) (Math.pow(2, round+1)*100);
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