package network.packet;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import network.Client;
import network.Server;

public class ConnectPacket extends Packet {
	private String type;
	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ, scale;

	public ConnectPacket(byte[] data) {
		super(00);

		String[] dataArray = readData(data).split(":");
		this.type = dataArray[0];
		this.position = new Vector3f(Float.parseFloat(dataArray[2]), Float.parseFloat(dataArray[3]),
				Float.parseFloat(dataArray[4]));
		this.rotX = Float.parseFloat(dataArray[5]);
		this.rotY = Float.parseFloat(dataArray[6]);
		this.rotZ = Float.parseFloat(dataArray[7]);
		this.scale = Float.parseFloat(dataArray[8]);
	}

	public ConnectPacket(String type, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(00);
		this.type = type;
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	@Override
	public void writeData(Client client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(Server server) {
		server.broadcast(getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + this.type + ":" + this.model + ":" + this.position.getX() + ":" + this.position.getY() + ":"
				+ this.position.getZ() + ":" + this.rotX + ":" + this.rotY + ":" + this.rotZ + ":" + this.scale)
						.getBytes();
	}

	public String getType() {
		return this.type;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public float getScale() {
		return scale;
	}
}