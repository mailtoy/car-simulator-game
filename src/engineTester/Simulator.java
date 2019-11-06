package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import connection.ClientType;
import entities.Camera;

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
		setCamera();
		run();
	}
	
	@Override
	public void setCamera() {
		camera = new Camera();
		camera.setPosition(new Vector3f(100, 100, -100));
		camera.setPitch(90);
	}

	public void run() {
		while (!Display.isCloseRequested()) {
			render();
		}
		closeRequest();
	}

	public static void main(String[] args) {
		new Simulator();
	}

}
