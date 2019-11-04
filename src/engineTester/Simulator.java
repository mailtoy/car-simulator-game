package engineTester;

import java.awt.event.KeyEvent;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import connection.ClientType;

public class Simulator extends ClientType {

	public Simulator() {
		super();
		setCamera();
		run();
	}

	public void run() {
		while (!Display.isCloseRequested()) {
			render();
		}
		closeRequest();
	}

	public void setCamera() {
		camera.setPosition(new Vector3f(100, 100, -100));
		camera.setPitch(90);
	}
	
	public static void main(String[] args) {
		new Simulator();
	}

}
