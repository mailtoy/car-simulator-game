package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import connection.ClientType;
import entities.Camera;
import entities.Entity;
import entities.Player;
import models.TexturedModel;
import renderEngine.DisplayManager;

public class Controller extends ClientType {
	
	public Controller() {
		super();
		
		setCar(stanfordBunny);
		run();
	}
	
	public static void main(String[] args) {
		new Controller();
	}
	
	public void setCar(TexturedModel carModel) {
		player = new Player(carModel, new Vector3f(0, 0, -40), 0, 180, 0, 0.6f);
		camera = new Camera(player);
		
//		try {
//			client.sendSelectCar(player);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	public void run() {
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
//		try {
//			client.sendDisconnect();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	@Override
	public void write(String message) {
		System.out.println(message);
	}

}
