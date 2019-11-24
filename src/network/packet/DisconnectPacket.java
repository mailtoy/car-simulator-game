package network.packet;

import org.lwjgl.util.vector.Vector3f;

import network.Client;
import network.Server;

public class DisconnectPacket extends Packet {
	private String type;
	private Vector3f position;
	private float rotX, rotY, rotZ, scale;

	public DisconnectPacket(byte[] data) {
		super(01);

		String[] dataArray = readData(data).split(":");
		if (dataArray.length != 1) {
			this.type = dataArray[0];
			this.position = new Vector3f(Float.parseFloat(dataArray[1]), Float.parseFloat(dataArray[2]),
					Float.parseFloat(dataArray[3]));
			this.rotX = Float.parseFloat(dataArray[4]);
			this.rotY = Float.parseFloat(dataArray[5]);
			this.rotZ = Float.parseFloat(dataArray[6]);
			this.scale = Float.parseFloat(dataArray[7]);
		} else {
			this.type = readData(data);
		}
	}

	public DisconnectPacket(String type) {
		super(01);
		this.type = type;
	}

	public DisconnectPacket(String type, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(01);
		this.type = type;
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
		return this.type.contains("Controller")
				? ("01" + this.type + ":" + this.position.getX() + ":" + this.position.getY() + ":"
						+ this.position.getZ() + ":" + this.rotX + ":" + this.rotY + ":" + this.rotZ + ":" + this.scale)
								.getBytes()
				: ("01" + this.type).getBytes();
	}

	public String getType() {
		return type;
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