package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class ControllerCamera extends Camera {
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private int round;
	private Player player;
	private Vector3f upLeft;

	public ControllerCamera(Player player) {
		this.player = player;
	}

	@Override
	public void move() {
		setUpLeft();
		checkCrash();
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

	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
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

	private void setUpLeft() {
		upLeft = new Vector3f(player.getPosition().getX() - 4, 0, player.getPosition().getZ() - 8);
	}

	private void checkCrash() {
		int carW = 8;
		int carH = 16;
		if ((upLeft.getX() >= 799 && upLeft.getX() <= 701)
				|| (upLeft.getX() + carW >= 799 && upLeft.getX() + carW <= 801)
				|| (upLeft.getX() <= 799 && upLeft.getX() + carW >= 801)) {
			System.out.println("เข้าไม่สุดจ้าาาา");
			if (upLeft.getZ() >= 799 && upLeft.getZ() <= 801) {
				System.out.println("เข้าสุดละจ้าาาา");
				player.setPosition(new Vector3f(850, 0, 850));
			} else if (upLeft.getZ() + carH >= 799 && upLeft.getZ() + carH <= 801) {
				System.out.println("เข้าสุดละจ้าาาา");
				player.setPosition(new Vector3f(850, 0, 850));
			} else if (upLeft.getZ() <= 799 && upLeft.getZ() + carH >= 801) {
				System.out.println("เข้าสุดละจ้าาาา");
				player.setPosition(new Vector3f(850, 0, 850));
			}
		}
	}

	@Override
	public void setRound(int round) {
		this.round = round;
	}

}
