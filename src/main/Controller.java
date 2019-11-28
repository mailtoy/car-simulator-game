package main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.ControllerCamera;
import entities.MultiplePlayer;
import network.packet.ConnectPacket;
import network.packet.DisconnectPacket;
import network.packet.MovePacket;

public class Controller extends WindowDisplay {
	protected ControllerHandler controllerHandler;
	private boolean isPressed = false;

	public Controller() {
		super();
		player = new MultiplePlayer(type, car, new Vector3f(305, 0, -10), 0, 180, 0, 0.6f, null, -1);
		controllerHandler = new ControllerHandler(this);
		camera = new ControllerCamera(player);

		ConnectPacket connectPacket = new ConnectPacket(type, map, player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		connectPacket.writeData(client);

		run();
	}

	@Override
	public void run() {
		while (!Display.isCloseRequested()) {
			camera.move();
			player.checkInputs();
			super.render();

			boolean isForward = Keyboard.isKeyDown(Keyboard.KEY_UP);
			boolean isBackward = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
			boolean isLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
			boolean isRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);

			if (isForward || isBackward || isLeft || isRight || player.getCurrentSpeed() > 0 || isPressed) {
				controllerHandler.updateSpeed(player.getCurrentSpeed());
				isPressed = false;

				MovePacket movePacket = new MovePacket(player.getType(), player.getPosition(), player.getRotX(),
						player.getRotY(), player.getRotZ());
				movePacket.writeData(client);
			}
		}
		super.closeqRequest();
		DisconnectPacket disconnectPacket = new DisconnectPacket(type, player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		disconnectPacket.writeData(client);
	}

	public void setPressed(String pressedButton) {
		player.checkInputs(pressedButton);
		isPressed = true;
	}

	public static void main(String[] args) {
		new Controller();
	}
}
