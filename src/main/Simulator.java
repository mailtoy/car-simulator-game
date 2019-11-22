package main;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.MultiplePlayer;
import entities.SimulatorCamera;
import network.packet.ConnectPacket;
import network.packet.DisconnectPacket;

public class Simulator extends WindowDisplay {
	protected final String TYPE = "Simulator" + new Random().nextInt(100); // for now

	public Simulator() {
		super();
		player = new MultiplePlayer(TYPE, car, new Vector3f(305, 0, -10), 0, 180, 0, 0.6f, null, -1); // get model only
		camera = new SimulatorCamera();

		ConnectPacket connectPacket = new ConnectPacket(TYPE);
		connectPacket.writeData(client);

		run();
	}

	private void run() {
		while (!Display.isCloseRequested()) {
			camera.move();
			super.render();
		}
		DisconnectPacket disconnectPacket = new DisconnectPacket(TYPE);
		disconnectPacket.writeData(client);
		super.closeqRequest();
	}

	public static void main(String[] args) {
		new Simulator();
	}

}
