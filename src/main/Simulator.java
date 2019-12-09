package main;

import org.lwjgl.opengl.Display;

import entities.Entity;
import entities.SimulatorCamera;
import network.packet.ConnectPacket;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Simulator extends WindowDisplay {
	protected SimulatorHandler simulatorHandler;

	public Simulator() {
		super();
		simulatorHandler = new SimulatorHandler(this);
		camera = new SimulatorCamera();
		camera.setRound(round);

		ConnectPacket connectPacket = new ConnectPacket(type, getDefaultMap());
		connectPacket.writeData(client);

		run();
	}

	@Override
	protected void run() {
		while (!Display.isCloseRequested()) {
			checkMapChanged();
			checkForceQuit();
			camera.move();
			render();
		}
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
		simulatorHandler.render();
		DisplayManager.updateDisplay();
	}

	public static void main(String[] args) {
		new Simulator();
	}

}
