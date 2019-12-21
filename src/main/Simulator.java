package main;

import org.lwjgl.opengl.Display;

import entities.SimulatorCamera;
import handlers.ControllerHandler;
import handlers.SimulatorHandler;
import network.packet.ConnectPacket;
import renderEngine.DisplayManager;

public class Simulator extends WindowDisplay {

	public Simulator() {
		super();
		handler = new SimulatorHandler(this);
		camera = new SimulatorCamera();
		camera.setRound(round);
		
		ConnectPacket connectPacket = new ConnectPacket(type, getDefaultMap());
		connectPacket.writeData(client);

		run();
	}

	@Override
	protected void run() {
		while (!Display.isCloseRequested()) {
			check();
			camera.move();
			render();
		}
		super.closeqRequest();
	}

	@Override
	protected void render() {
		super.renderComponents();
		SimulatorHandler simHandler = ((SimulatorHandler) handler);
		SimulatorCamera simCamerra = ((SimulatorCamera) camera);
		simHandler.changeDefaultGui();
		if(simCamerra.getIsClicked() == true) {
				simHandler.changeGui(simCamerra.getWhatButton());
		}
		
		DisplayManager.updateDisplay();
	}

	public static void main(String[] args) {
		new Simulator();
	}

}
