package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import connection.ClientType;
import entities.Entity;
import renderEngine.DisplayManager;

public class Simulator extends ClientType {
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;

	public Simulator() {
		super();
		setCamera();
		run();
	}

	public void run() {
		while (!Display.isCloseRequested()) {
//			camera.move();
//			player.move();

			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);

			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}

			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

		try {
			client.sendDisconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void checkKeyInput(int keyInput) {
		// logic for checking the wall and if passes
		
		if (keyInput == Keyboard.KEY_UP) {
			player.setCurrentSpeed(RUN_SPEED);
		} else if (keyInput == Keyboard.KEY_DOWN) {
			player.setCurrentSpeed(-RUN_SPEED);
		} else {
			player.setCurrentSpeed(0);
		}
		
		if (keyInput == Keyboard.KEY_RIGHT) {
			player.setCurrentTurnSpeed(-TURN_SPEED);
		} else if (keyInput == Keyboard.KEY_LEFT) {
			player.setCurrentTurnSpeed(TURN_SPEED);
		} else {
			player.setCurrentTurnSpeed(0);
		}
		camera.move();
		player.move();
	}
	
	public void setCamera() {
		camera.setPosition(new Vector3f(100, 100, -100));
		camera.setPitch(90);
	}

	public static void main(String[] args) {
		new Simulator();
	}
}
