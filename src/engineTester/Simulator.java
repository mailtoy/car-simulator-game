package engineTester;

import org.lwjgl.opengl.Display;

import connection.ClientType;
import entities.SimulatorCamera;

/**
 * Simulator receives the movement commands from Controller. Showing from the
 * top view of the car.
 * 
 * @author Issaree Srisomboon
 *
 */
public class Simulator extends ClientType {

	public Simulator() {
		super();
		camera = new SimulatorCamera();
		camera.setRound(round);
		run();
	}

	public void run() {
		while (!Display.isCloseRequested()) {
			camera.move();
			render();
		}
		closeRequest();
	}

	public static void main(String[] args) {
		new Simulator();
	}

}