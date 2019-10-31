package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import connection.ClientType;
import entities.Entity;
import renderEngine.DisplayManager;

public class Controller extends ClientType {

	public Controller() {
		super();
		run();
	}

	public static void main(String[] args) {
		new Controller();
	}

	public void run() {
		while (!Display.isCloseRequested()) {
			try {
				checkKeyInputs();

				camera.move();
//				player.move();

				renderer.processEntity(player);
				renderer.processTerrain(terrain);
				renderer.processTerrain(terrain2);

				for (Entity entity : entities) {
					renderer.processEntity(entity);
				}

				renderer.render(light, camera);
				DisplayManager.updateDisplay();

			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public void checkKeyInputs() throws Exception {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			client.sendKeyInput(Keyboard.KEY_UP);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			client.sendKeyInput(Keyboard.KEY_DOWN);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			client.sendKeyInput(Keyboard.KEY_RIGHT);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			client.sendKeyInput(Keyboard.KEY_LEFT);
		}
	}

}
