package main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import network.packet.ConnectPacket;
import network.packet.MovePacket;

public class Controller extends WindowDisplay {

	public Controller() {
		super();

		ConnectPacket connectPacket = new ConnectPacket(TYPE, player.getModel(), player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		connectPacket.writeData(client);

		run();
	}

	private void run() {
		while (!Display.isCloseRequested()) {

			if (Keyboard.next()) {
				MovePacket packet = new MovePacket(player.getType(), player.getPosition(), player.getRotX(),
						player.getRotY(), player.getRotZ());
				packet.writeData(client);
			}

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
