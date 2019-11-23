package entities;

import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;

public abstract class Camera {
	protected Vector3f position = new Vector3f();
	protected float pitch = 20;
	protected float yaw;
	protected float roll;
	
	public Camera() {
		setPosition(new Vector3f(-650, 6, 6));
		setPitch(20);
	}

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
	public abstract void setRound(int round);
}
