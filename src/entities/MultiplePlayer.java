package entities;

import java.net.InetAddress;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class MultiplePlayer extends Player {
	private InetAddress ipAddress;
	private int port;

	public MultiplePlayer(String name, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale, InetAddress ipAddress, int port) {
		super(name, model, position, rotX, rotY, rotZ, scale);
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
