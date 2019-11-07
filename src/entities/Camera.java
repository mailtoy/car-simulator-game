package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	
	private float distanceFromPlayer = 5;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(110, 3, -23);

	private float pitch = 0;
	private float yaw;
	private float roll;

	private Player player;
	
	public Camera(Player player){
		this.player = player; 
	}
	
	public void move(){
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
//		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
//			position.z-=0.2f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
//			position.z+=0.2f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
//			position.x+=0.2f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
//			position.x-=0.2f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) || zoom == "in"){
//			position.y+=0.2f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || zoom == "out"){
//			position.y-=0.2f;
//		}
	}
	
	public void zoom (String zoom) {
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) || zoom == "in"){
			position.y+=0.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || zoom == "out"){
			position.y-=0.2f;
		}
	}



	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
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
//		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
		return (float) -3;
	}
	
	private float calculateVerticalDistance() {
//		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
		return (float) 3;
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch(){
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
	
}
