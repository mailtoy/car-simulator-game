package main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.ControllerCamera;
import entities.Entity;
import entities.MultiplePlayer;
import fontRendering.TextMaster;
import network.packet.ConnectPacket;
import network.packet.MovePacket;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Controller extends WindowDisplay {
	protected ControllerHandler controllerHandler;
	protected Gauge gauge;

	public Controller() {
		super();

		player = new MultiplePlayer(type, car, new Vector3f(randPosX, 0, randPosZ), 0, 180, 0, 0.6f, null, -1);
		controllerHandler = new ControllerHandler(this);
		gauge = new Gauge(this, player.getCurrentSpeed());
		camera = new ControllerCamera(player);

		ConnectPacket connectPacket = new ConnectPacket(type, getDefaultMap(), player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		connectPacket.writeData(client);

		run();
	}

	@Override
	protected void run() {
		while (!Display.isCloseRequested()) {
			checkMapChanged();
			checkForceQuit();
			gauge = new Gauge(this, player.getCurrentSpeed());

			if (!isCrashed) {
				camera.move();
				player.move();

				boolean isForward = Keyboard.isKeyDown(Keyboard.KEY_UP);
				boolean isBackward = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
				boolean isLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
				boolean isRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
				// boolean for mouse detect here

				if (isForward || isBackward || isLeft || isRight || player.getCurrentSpeed() != 0) {
					MovePacket movePacket = new MovePacket(player.getType(), player.getPosition(), player.getRotX(),
							player.getRotY(), player.getRotZ());
					movePacket.writeData(client);
				}
			}
			render();
		}
		TextMaster.cleanUp();
		controllerHandler.cleanUp();
		super.closeqRequest();
	}

	@Override
	protected void render() {
		for (Terrain terrain : terrains) {
			renderer.processTerrain(terrain);
		}
		for (Entity entity : entities) {
			renderer.processEntity(entity);
		}
		renderer.render(light, camera);
		controllerHandler.render();

		if (isCrashed) {
			TextMaster.render();
		}
		DisplayManager.updateDisplay();
	}

	public static void main(String[] args) {
		new Controller();
	}
}