package main;

import java.util.Random;

import org.lwjgl.opengl.Display;

import entities.SimulatorCamera;
import network.packet.ConnectPacket;

public class Simulator extends WindowDisplay {
	protected final String TYPE = "Simulator" + new Random().nextInt(100); // for now

	public Simulator() {
		super();
		camera = new SimulatorCamera();

		ConnectPacket connectPacket = new ConnectPacket(TYPE.getBytes());
		connectPacket.writeData(client);

		run();
	}

	private void run() {
		while (!Display.isCloseRequested()) {
			camera.move();
			super.render();
		}
		super.closeqRequest(TYPE);
	}

	public static void main(String[] args) {
		new Simulator();
	}

}
