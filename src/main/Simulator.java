package main;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.MultiplePlayer;
import entities.SimulatorCamera;
import network.packet.ConnectPacket;
import network.packet.DisconnectPacket;

public class Simulator extends WindowDisplay {

	public Simulator() {
		super();
		player = new MultiplePlayer(type, car, new Vector3f(305, 0, -10), 0, 180, 0, 0.6f, null, -1);
		camera = new SimulatorCamera();

		ConnectPacket connectPacket = new ConnectPacket(type, map);
		connectPacket.writeData(client);

		run();
	}

	@Override
	public void run() {
		while (!Display.isCloseRequested()) {
			camera.move();
			super.render();
		}
		super.closeqRequest();
		DisconnectPacket disconnectPacket = new DisconnectPacket(type);
		disconnectPacket.writeData(client);
	}

	public static void main(String[] args) {
		new Simulator();
	}

}
