package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * For controlling the simulator movement.
 * 
 * @author Worawat Chueajedton
 *
 */
public class SimulatorCamera extends Camera {
	private Vector3f position = new Vector3f(600, 500, 400);
	private Boolean isClicked;
	private String whatButton;

	public SimulatorCamera() {
		setPosition(position);
		setPitch(90);
	}

	/**
	 * 
	 * For checking simulator move forward, backward, left, right and also zoom in,
	 * zoom out. By using keyboard and mouse detected. Create boolean of each
	 * condition to move x and z position of camera also set whatButton in zoom in
	 * and out condition to use in SimulatorHandler.
	 * 
	 * 
	 */
	@Override
	public void move() {
		calculateMouseZoom();
		whatButton = "none";
		setIsClicked(false);
		boolean isForward = Keyboard.isKeyDown(Keyboard.KEY_UP);
		boolean isBackward = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		boolean isLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		boolean isRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		boolean isZoomIn = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || isPressButton(0.87, 0.74, -0.20, -0.0);
		boolean isZoomOut = Keyboard.isKeyDown(Keyboard.KEY_SPACE) || isPressButton(0.87, 0.74, 0.52, 0.70);
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

	/**
	 * 
	 * To check what button is clicked, by get position button and create frame of
	 * button to get an area which we can use mouse to click. When the mouse is
	 * click in that area the button will activate.
	 * 
	 * @param d is right of button area.
	 * @param e is left of button area.
	 * @param f is bottom of button area.
	 * @param g is top of button area.
	 * @return
	 */
	private boolean isPressButton(double d, double e, double f, double g) {
		float mouseXCoords = (2f * Mouse.getX()) / Display.getWidth() - 1f;
		float mouseYCoords = (2f * Mouse.getY()) / Display.getHeight() - 1f;
		boolean isBtnDown = Mouse.isButtonDown(0);
		return (mouseXCoords <= d && mouseXCoords >= e && mouseYCoords >= f && mouseYCoords <= g && isBtnDown);
	}

	/**
	 * 
	 * Make simulator can zoom in and out with wheel on mouse and create limit
	 * position of both function.
	 * 
	 */
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

	/**
	 * To get boolean when button is clicked
	 * 
	 * @return true or false, when button clicked.
	 */
	public Boolean getIsClicked() {
		return isClicked;
	}

	/**
	 * To set boolean when button is clicked
	 * 
	 * @param isClicked true when button is clicked, not when button is not clicked
	 */
	public void setIsClicked(Boolean isClicked) {
		this.isClicked = isClicked;
	}

	/**
	 * To get is it zoom in of zoom out button is use.
	 * 
	 * @return string + when zoom out, - when zoom in.
	 */
	public String getWhatButton() {
		return whatButton;
	}

}