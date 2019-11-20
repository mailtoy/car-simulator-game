package main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import entities.ControllerCamera;
import network.packet.ConnectPacket;
import network.packet.MovePacket;

public class Controller extends WindowDisplay {

	public Controller() {
		super();
		camera = new ControllerCamera(player);

		ConnectPacket connectPacket = new ConnectPacket(TYPE, player.getModel(), player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		connectPacket.writeData(client);

		run();
	}

	private void run() {
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			super.render();

			if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)
					|| Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				MovePacket packet = new MovePacket(player.getType(), player.getPosition(), player.getRotX(),
						player.getRotY(), player.getRotZ());
				packet.writeData(client);
			}
		}
		super.closeqRequest(TYPE);
	}

	public static void main(String[] args) {
		new Controller();
	}
}
