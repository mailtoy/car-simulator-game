package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class SimulatorCamera extends Camera {
	private Vector3f position = new Vector3f(765, 500, 800);
	private Boolean isClicked;
	private String whatButton;

	public SimulatorCamera() {
		setPosition(position);
		setPitch(90);
	}

	@Override
	public void move() {
		calculateMouseZoom();
		whatButton = "none";
		setIsClicked(false);
		boolean isForward = Keyboard.isKeyDown(Keyboard.KEY_UP);
		boolean isBackward = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		boolean isLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		boolean isRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		boolean isZoomIn = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || isPressButton(0.87,0.74 , 0.52, 0.70);
		boolean isZoomOut = Keyboard.isKeyDown(Keyboard.KEY_SPACE) || isPressButton(0.87,  0.74, -0.20, -0.0);
		boolean isMouseClicked = Mouse.isButtonDown(0);
		
		if (isForward) {
			position.z -= 5f;
		}
		if (isBackward) {
			position.z += 5f;
		}
		if (isRight) {
			position.x += 5f;
		}
		if (isLeft) {
			position.x -= 5f;
		}
		if (isZoomOut) {
			position.y += 5f;
			setIsClicked(true);
			whatButton = "+";
		}
		if (isZoomIn) {
			position.y -= 5f;
			setIsClicked(true);
			whatButton = "-";
		}
		if (isMouseClicked) {
			position.x -= Mouse.getDX();
			position.z += Mouse.getDY();
		}
	}
	
	private boolean isPressButton(double d, double e, double f, double g) {
		float mouseXCoords = (2f * Mouse.getX()) / Display.getWidth() - 1f;
		float mouseYCoords = (2f * Mouse.getY()) / Display.getHeight() - 1f;
		boolean isBtnDown = Mouse.isButtonDown(0);
		return (mouseXCoords <= d && mouseXCoords >= e && mouseYCoords >= f && mouseYCoords <= g && isBtnDown);
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

	public Boolean getIsClicked() {
		return isClicked;
	}

	public void setIsClicked(Boolean isClicked) {
		this.isClicked = isClicked;
	}

	public String getWhatButton() {
		return whatButton;
	}

}