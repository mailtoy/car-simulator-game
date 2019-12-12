package main;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.ControllerCamera;
import entities.MultiplePlayer;

import fontRendering.GaugeTextMaster;
import handlers.ControllerHandler;
import network.packet.ConnectPacket;
import network.packet.MovePacket;
import renderEngine.DisplayManager;

public class Controller extends WindowDisplay {
	private final int MAX = 3112;
	private final int MIN = 8;
	protected final float randPosX = new Random().nextInt(MAX - MIN) + MIN; // for now
	protected final float randPosZ = new Random().nextInt(MAX - MIN) + MIN; // for now
	protected Gauge gauge;
	protected float speed = 0;

	public Controller() {
		super();

		player = new MultiplePlayer(type, carColor, car, new Vector3f(randPosX, 0, randPosZ), 0, 180, 0, 0.6f, null,
				-1);
		handler = new ControllerHandler(this);
		gauge = new Gauge(this, speed);
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
//			gauge = new Gauge(this, player.getCurrentSpeed());

			if (!isCrashed()) {
				camera.move();
				player.move();

				if (player.getCurrentSpeed() != 0) {
					MovePacket movePacket = new MovePacket(player.getType(), player.getPosition(), player.getRotX(),
							player.getRotY(), player.getRotZ());
					movePacket.writeData(client);
				}
			}
			render();
		}
		handler.cleanUp();
		GaugeTextMaster.cleanUp();
		super.closeqRequest();
	}

	@Override
	protected void render() {
		super.renderComponents();
		GaugeTextMaster.render();

		if (isCrashed()) {
			((ControllerHandler) handler).textRender();
		}
		DisplayManager.updateDisplay();
	}

	public void setGauge(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	public static void main(String[] args) {
		new Controller();
	}
}