package main;

import org.lwjgl.opengl.Display;

import entities.SimulatorCamera;
import handlers.SimulatorHandler;
import network.packet.ConnectPacket;
import renderEngine.DisplayManager;

/**
 * 
 * Simulator acts as a map, showing all detail in the terrain, seeing how cars
 * run in map.
 * 
 * @author Worawat Chueajedton
 *
 */
public class Simulator extends WindowDisplay {

	/**
	 * 
	 * Set up simulator, creating handler for handle the gui, creating camera for
	 * controlling the simulator and connectPacket for connect to server
	 * 
	 */
	public Simulator() {
		super();
		handler = new SimulatorHandler(this);
		camera = new SimulatorCamera();
		camera.setRound(round);

		ConnectPacket connectPacket = new ConnectPacket(type, getDefaultMap());
		connectPacket.writeData(client);

		run();
	}

	/**
	 * 
	 * Running simulator and checking is the map is change?. What is car color? and
	 * is this simulator is kicked form server or not.
	 * 
	 */
	@Override
	protected void run() {
		while (!Display.isCloseRequested()) {
			check();
			camera.move();
			render();
		}
		super.closeqRequest();
	}

	/**
	 * Render all component in simulator(terrains, entities).
	 * 
	 */
	@Override
	protected void render() {
		super.renderComponents();
		checkZoomButtonClick();
		DisplayManager.updateDisplay();
	}

	/**
	 * 
	 * To pass what button is click(zoom in or zoom out). From SimulatorCamera to
	 * SimulatorHandler.
	 * 
	 */
	public void checkZoomButtonClick() {
		SimulatorHandler simHandler = ((SimulatorHandler) handler);
		SimulatorCamera simCamerra = ((SimulatorCamera) camera);
		simHandler.changeDefaultGui();
		if (simCamerra.getIsClicked() == true) {
			simHandler.changeGui(simCamerra.getWhatButton());
		}

	}

	public static void main(String[] args) {
		new Simulator();
	}

}
