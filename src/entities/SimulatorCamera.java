package entities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Toolkit;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import handlers.SimulatorHandler;
import main.WindowDisplay;

public class SimulatorCamera extends Camera {
	private Vector3f position = new Vector3f(765, 500, 800);
	private static Dimension screenSize;
	private int decreaseLimit = 650;
	private SimulatorHandler simulatorHandler;

	public SimulatorCamera(WindowDisplay windowDisplay) {
		super(windowDisplay);
		setPosition(position);
		setPitch(90);
	}

	@Override
	public void move() {
		simulatorHandler = (SimulatorHandler) windowDisplay.getHandler();
		float buttonZInPoX = simulatorHandler.getZoomIn().getPosition().getX();
		float buttonZInPoY = simulatorHandler.getZoomIn().getPosition().getY();
		float buttonZOutPoX = simulatorHandler.getZoomOut().getPosition().getX();
		float buttonZOutPoY = simulatorHandler.getZoomOut().getPosition().getY();

		calculateMouseZoom();
		calculateLimitMap();
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.z -= 5f;
			if (position.z < 15) {
				position.z = 15;
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
					position.z -= 5f;
				}
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.z += 5f;
			if (position.z >= (Math.pow(2, round + 1) * 100)) {
				position.z = (float) (Math.pow(2, round + 1) * 100);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			position.x += 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.x -= 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)
				|| Mouse.getX() <= buttonZInPoX - 5 && Mouse.getX() >= buttonZInPoX + 5
						&& Mouse.getY() <= buttonZInPoY - 5 && Mouse.getY() >= buttonZInPoY + 5) {
			position.y += 5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Mouse.getX() == buttonZOutPoX && Mouse.getY() == buttonZOutPoY) {
			position.y -= 5f;
		}
//		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Mouse.getX() <= 1338 && Mouse.getX() >= 1256
//				&& Mouse.getY() <= 730 && Mouse.getY() >= 663 && Mouse.isButtonDown(0)) {
//			position.y += 5f;
//		}
//		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Mouse.getX() <= 1338 && Mouse.getX() >= 1256
//				&& Mouse.getY() <= 419 && Mouse.getY() >= 344 && Mouse.isButtonDown(0)) {
//			position.y -= 5f;
//			decreaseLimit -= 10; 
//		}
		if (Mouse.isButtonDown(0)) {
			position.x -= Mouse.getDX();
			position.z += Mouse.getDY();
		}
	}

	public void calculateMouseZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		position.y += zoomLevel;
		if (position.y >= 920) {
			position.y = 920;
		}
		if (position.y <= 30) {
			position.y = 30;
		}
	}

	public void calculateLimitMap() {
		if (position.x >= 3200) {
			position.x = 3200;
		}
		if (position.x <= decreaseLimit) {
			position.x = decreaseLimit;
		}
		if (position.z >= 3200) {
			position.z = 3200;
		}
		if (position.z <= 0) {
			position.z = 0;
		}
	}

}