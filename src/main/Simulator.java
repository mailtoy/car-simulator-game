package main;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.MultiplePlayer;
import entities.SimulatorCamera;
import network.packet.ConnectPacket;

public class Simulator extends WindowDisplay {

	public Simulator() {
		super();
		player = new MultiplePlayer(type, null, new Vector3f(305, 0, -10), 0, 180, 0, 0.6f, null, -1);
		camera = new SimulatorCamera();

		ConnectPacket connectPacket = new ConnectPacket(type, "map1");
		connectPacket.writeData(client);
	}

	@Override
	public void run() {
		while (!Display.isCloseRequested()) {
			camera.move();
			super.render();
		}
		super.closeqRequest();
	}

	public static void main(String[] args) {
		new Simulator();
	}

}
