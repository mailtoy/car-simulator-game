package main;

import java.util.Random;

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
	protected final String TYPE = "Controller" + new Random().nextInt(100); // for now
	private boolean isPressed = false;

	public Controller() {
		super();
		controllerHandler = new ControllerHandler(this);

		player = new MultiplePlayer(TYPE, car, new Vector3f(305, 0, -10), 0, 180, 0, 0.6f, null, -1);
		camera = new ControllerCamera(player);

		ConnectPacket connectPacket = new ConnectPacket(TYPE, player.getPosition(), player.getRotX(), player.getRotY(),
				player.getRotZ(), player.getScale());
		connectPacket.writeData(client);

		run();
	}

	private void run() {
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
		DisconnectPacket disconnectPacket = new DisconnectPacket(TYPE, player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		disconnectPacket.writeData(client);
		super.closeqRequest();
	}

	public void setPressed(String pressedButton) {
		player.checkInputs(pressedButton);
		isPressed = true;
	}

	public static void main(String[] args) {
		new Controller();
	}
}
