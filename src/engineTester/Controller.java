package engineTester;

import org.lwjgl.opengl.Display;

import connection.ClientType;
import entities.Camera;

public class Controller extends ClientType {

	public Controller() {
		super();
		run();
	}
	
	@Override
	public void setCamera() {
		camera = new Camera(player);
	}

	public void run() {
		while (!Display.isCloseRequested()) {
			try {
				player.move();
				camera.move();
				
				client.sendPosition(player.getPosition());
				render();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		closeRequest();
	}
	
	public static void main(String[] args) {
		new Controller();
	}
}
