package main;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.ControllerCamera;
import entities.MultiplePlayer;
import entities.Player;
import handlers.ControllerHandler;
import network.packet.ConnectPacket;
import network.packet.MovePacket;
import renderEngine.DisplayManager;

public class Controller extends WindowDisplay {
	private final int MAX = 500;
	private final int MIN = 8;
	protected float randPosX = new Random().nextInt(MAX - MIN) + MIN; // for now
	protected float randPosZ = new Random().nextInt(MAX - MIN) + MIN; // for now
	protected ControllerHandler conHandler;

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
		conHandler = ((ControllerHandler) handler);
		if (!isCrashed()) {
			conHandler.gaugeRender(player.getCurrentSpeed());
			renderBtns();
		} else {
			conHandler.textRender();
			if (!conHandler.isAdded()) {
				conHandler.addCarCrashGUIs();
			}
		}
		DisplayManager.updateDisplay();
	}

	private void renderBtns() {
		String arrow = player.getArrow();
		String arrowLR = player.getArrowLR();
		String option = player.getOption();

		if (player.getActive()) {
			conHandler.setBtnActive(arrow);
		} else {
			conHandler.setBtnInactive(arrow);
		}

		if (player.getActiveLR()) {
			conHandler.setBtnActive(arrowLR);
		} else {
			conHandler.setBtnInactive(arrowLR);
		}

		if (player.getOptionActive()) {
			conHandler.setBtnActive(option);
		} else {
			conHandler.setBtnInactive(option);
		}
	}

	private void sendMove(Vector3f position) {
		MovePacket movePacket = new MovePacket(player.getType(), position, player.getRotX(), player.getRotY(),
				player.getRotZ());
		movePacket.writeData(client);
	}

	private void checkReplayandQuit() {
		if (player.isReplay()) {
			((ControllerHandler) handler).removeCarCrashGUIs();

			Player newPlayer = new Player(type, carColor, car, new Vector3f(randPosZ, 0, randPosX), 0, 180, 0, 0.6f);
			((ControllerCamera) camera).setPlayer(newPlayer);
			sendMove(newPlayer.getPosition());
			super.setPlayer(newPlayer);
			super.setCrash(false);
		} else if (player.isQuit()) {
			handler.cleanUp();
			super.closeqRequest();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		new Controller();
	}
}