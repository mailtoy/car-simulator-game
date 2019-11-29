package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;

public class ControllerCamera extends Camera {
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private int round;
	private Player player;
	private Vector3f upLeft;

	public Vector3f getUpLeft() {
		return upLeft;
	}

	public ControllerCamera(Player player) {
		this.player = player;
	}

	@Override
	public void move() {
		setFrame();
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
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || zoom == "in") {
			position.y += 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || zoom == "out") {
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
//		return (float) -2;
	}

	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
//		return (float) 3;
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

	public void checkCrash() {
//		if (750 >= upLeft.getX() && 750 <= upRight.getX()) {
////			player.setPosition(new Vector3f(upLeft.getX() + 6, 0, upLeft.getZ() + 6));
////			player.setCurrentSpeed(0);
////			System.out.println("เร่งสิเหยดแม่ม!!!");
//		}
//		if (750 >= upLeft.getZ() && 750 <= downLeft.getZ()) {
//			player.setPosition(new Vector3f(upLeft.getX() + 6, 0, upLeft.getZ() + 6));
//			player.setCurrentSpeed(0);
//		}
//		if (750 >= downLeft.getX() && 750 <= downRight.getX()) {
//			player.setPosition(player.getPosition());
//		}
//		if (750 >= downRight.getZ() && 750 <= upRight.getZ()) {
//			player.setPosition(player.getPosition());
//		}

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
			}
			else if (upLeft.getZ() <= 799 && upLeft.getZ() + carH >= 801) {
				System.out.println("เข้าสุดละจ้าาาา");
				player.setPosition(new Vector3f(850, 0, 850));
			}

		}
	}

	public void setFrame() {
		upLeft = new Vector3f(player.getPosition().getX() - 4, 0, player.getPosition().getZ() - 8);
	}

	@Override
	public void setRound(int round) {
		this.round = round;
	}

}
