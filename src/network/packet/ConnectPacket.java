package network.packet;

import org.lwjgl.util.vector.Vector3f;

import network.Client;
import network.Server;

public class ConnectPacket extends Packet {
	private String type, map, carColor;
	private Vector3f position;
	private float rotX, rotY, rotZ, scale;

	public ConnectPacket(byte[] data) {
		super(00);

		String[] dataArray = readData(data).split(":");
		if (dataArray.length > 2) {
			this.type = dataArray[0];
			this.map = dataArray[1];
			this.carColor = dataArray[2];
			this.position = new Vector3f(Float.parseFloat(dataArray[3]), Float.parseFloat(dataArray[4]),
					Float.parseFloat(dataArray[5]));
			this.rotX = Float.parseFloat(dataArray[6]);
			this.rotY = Float.parseFloat(dataArray[7]);
			this.rotZ = Float.parseFloat(dataArray[8]);
			this.scale = Float.parseFloat(dataArray[9]);
		} else {
			this.type = dataArray[0];
			this.map = dataArray[1];
		}
	}

	public ConnectPacket(String type, String map) {
		super(00);
		this.type = type;
		this.map = map;
	}

	public ConnectPacket(String type, String map, String carColor, Vector3f position, float rotX, float rotY,
			float rotZ, float scale) {
		super(00);
		this.type = type;
		this.map = map;
		this.carColor = carColor;
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
				? ("00" + this.type + ":" + this.map + ":" + this.carColor + ":" + this.position.getX() + ":"
						+ this.position.getY() + ":" + this.position.getZ() + ":" + this.rotX + ":" + this.rotY + ":"
						+ this.rotZ + ":" + this.scale).getBytes()
				: ("00" + this.type + ":" + this.map).getBytes();
	}

	public String getType() {
		return type;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
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

	public String getCarColor() {
		return carColor;
	}
}