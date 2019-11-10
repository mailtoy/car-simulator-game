package network.packet;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import network.Client;
import network.Server;

public class ConnectPacket extends Packet {
	private String name;
	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ, scale;

	public ConnectPacket(byte[] data) {
		super(00);
		this.name = readData(data);
	}

	public ConnectPacket(String name, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		super(00);
		this.name = name;
		this.model = model;
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
		return ("00" + this.name).getBytes();
	}

	public String getName() {
		return this.name;
	}

	public TexturedModel getModel() {
		return model;
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