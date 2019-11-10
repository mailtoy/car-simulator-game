package entities;

import org.lwjgl.util.vector.Vector3f;

public abstract class Camera {
	protected Vector3f position = new Vector3f(0, 5, 0);
	protected float pitch = 10;
	protected float yaw;
	protected float roll;

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public abstract void move();
}
