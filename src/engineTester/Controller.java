package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import connection.ClientType;

/**
 * Controller controls the movement of the car, then sends it through Server to
 * be able to parallel move in Simulator.
 * 
 * @author Issaree Srisomboon
 *
 */
public class Controller extends ClientType {
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;

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
				checkKeyInput();
				render();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		closeRequest();
	}

	public void checkKeyInput() throws Exception {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			client.sendKeyInput(Keyboard.KEY_UP, RUN_SPEED);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			client.sendKeyInput(Keyboard.KEY_DOWN, -RUN_SPEED);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			client.sendKeyInput(Keyboard.KEY_RIGHT, -TURN_SPEED);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			client.sendKeyInput(Keyboard.KEY_LEFT, TURN_SPEED);
		}
	}

}
