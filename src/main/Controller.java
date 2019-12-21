package main;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.ControllerCamera;
import entities.MultiplePlayer;
import entities.Player;
import handlers.ControllerHandler;
import handlers.Handler;
import network.packet.ConnectPacket;
import network.packet.MovePacket;
import renderEngine.DisplayManager;

public class Controller extends WindowDisplay {
	private final int MAX = 500;
	private final int MIN = 8;
	protected float randPosX = new Random().nextInt(MAX - MIN) + MIN; // for now
	protected float randPosZ = new Random().nextInt(MAX - MIN) + MIN; // for now

	public Controller() {
		super();

		player = new MultiplePlayer(type, carColor, car, new Vector3f(randPosX, 0, randPosZ), 0, 180, 0, 0.6f, null,
				-1);
		handler = new ControllerHandler(this);
		camera = new ControllerCamera(player);

		ConnectPacket connectPacket = new ConnectPacket(type, getDefaultMap(), player.getColor(), player.getPosition(),
				player.getRotX(), player.getRotY(), player.getRotZ(), player.getScale());
		connectPacket.writeData(client);

		run();
	}

	@Override
	protected void run() {
		while (!Display.isCloseRequested()) {
			check();
			if (!isCrashed()) {
				camera.move();
				player.move();
				if (player.getCurrentSpeed() != 0) {
					sendMove(player.getPosition());
				}
			} else {
				checkReplayandQuit();
			}
			render();
		}
		handler.cleanUp();
		super.closeqRequest();
	}

	@Override
	protected void render() {
		super.renderComponents();

		ControllerHandler conHandler = ((ControllerHandler) handler);
		if (!isCrashed()) {
			conHandler.gaugeRender(player.getCurrentSpeed());
			int arrow = player.getArrow();
			int arrowLR = player.getArrowLR();
			if (player.getActive()) {
				conHandler.changeButtonGUIs(arrow);
			} else {
				conHandler.changeButtonGUIsBack(arrow);
			}
			if (player.getActiveLR()) {
				conHandler.changeButtonGUIs(arrowLR);
			} else {
				conHandler.changeButtonGUIsBack(arrowLR);
			}
		} else {
			conHandler.textRender();
			if (!conHandler.isAdded()) {
				conHandler.addCarCrashGUIs();
			}
		}
		DisplayManager.updateDisplay();
	}

	private void sendMove(Vector3f position) {
		MovePacket movePacket = new MovePacket(player.getType(), position, player.getRotX(), player.getRotY(),
				player.getRotZ());
		movePacket.writeData(client);
	}

	private void checkReplayandQuit() {
		if (player.isPressButton(-0.06, -0.3, 0.06, 0.3)) {
			((ControllerHandler) handler).removeCarCrashGUIs();

			Player newPlayer = new Player(type, carColor, car, new Vector3f(randPosZ, 0, randPosX), 0, 180, 0, 0.6f);
			((ControllerCamera) camera).setPlayer(newPlayer);
			sendMove(newPlayer.getPosition());
			super.setPlayer(newPlayer);
			super.setCrash(false);
		} else if (player.isPressButton(0.28, 0.05, 0.06, 0.3)) {
			handler.cleanUp();
			super.closeqRequest();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		new Controller();
	}
}