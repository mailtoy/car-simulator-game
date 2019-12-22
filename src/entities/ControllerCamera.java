package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ControllerCamera extends Camera {
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private Player player;

	public ControllerCamera(Player player) {
		this.player = player;
	}

	@Override
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}

	public void zoom(String zoom) {
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || zoom.equals("in")) {
			position.y += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || zoom.equals("out")) {
			position.y -= 0.2f;
		}
	}

	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance;
	}

	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	/**
	 * 
	 * Make controller can zoom in and out with wheel on mouse and create limit
	 * position of both function.
	 * 
	 */
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		if (distanceFromPlayer >= 200) {
			distanceFromPlayer = 200;
		}
		if (distanceFromPlayer <= 15) {
			distanceFromPlayer = 15;
		}
	}

	private void calculatePitch() {
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}

	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
