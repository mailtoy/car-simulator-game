package main;

import java.util.Random;

import org.lwjgl.opengl.Display;

import network.packet.ConnectPacket;

public class Controller extends WindowDisplay {
	private final String TYPE = "Controller" + new Random().nextInt(1000); // for now

	public Controller() {
		super();

		ConnectPacket connectPacket = new ConnectPacket(TYPE, player.getModel(), player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		connectPacket.writeData(client);

		run();
	}

	private void run() {
		while (!Display.isCloseRequested()) {
			player.move();
			camera.move();
			super.render();
		}
		super.closeqRequest(TYPE);
	}

	public static void main(String[] args) {
		new Controller();
	}
}
