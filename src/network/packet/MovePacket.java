package network.packet;

import org.lwjgl.util.vector.Vector3f;

import network.Client;
import network.Server;

public class MovePacket extends Packet {
	private String type;
	private Vector3f position;
	private float rotX, rotY, rotZ;

	public MovePacket(byte[] data) {
		super(02);

		String[] dataArray = readData(data).split(":");
		this.type = dataArray[0];
		this.position = new Vector3f(Float.parseFloat(dataArray[1]), Float.parseFloat(dataArray[2]),
				Float.parseFloat(dataArray[3]));
		this.rotX = Float.parseFloat(dataArray[4]);
		this.rotY = Float.parseFloat(dataArray[5]);
		this.rotZ = Float.parseFloat(dataArray[6]);
	}

	public MovePacket(String type, Vector3f position, float rotX, float rotY, float rotZ) {
		super(02);
		this.type = type;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
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
		return ("02" + this.type + ":" + this.position.getX() + ":" + this.position.getY() + ":"
				+ this.position.getZ() + ":" + this.rotX + ":" + this.rotY + ":" + this.rotZ)
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
}