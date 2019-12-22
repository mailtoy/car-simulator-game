package entities;

import java.net.InetAddress;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

/**
 * For multiple players connected in the server.
 * 
 * @author Issaree Srisomboon
 *
 */
public class MultiplePlayer extends Player {
	private InetAddress ipAddress;
	private int port;

	/**
	 * Constructor of MultiplePlayer contained necessary information of Player and,
	 * IP address and a port number of connected client.
	 * 
	 * @param name      Specific name of client
	 * @param color     Car model's color
	 * @param model     Model of car
	 * @param position  Current position of player
	 * @param rotX      Player's X axis rotation
	 * @param rotY      Player's Y axis rotation
	 * @param rotZ      Player's Z axis rotation
	 * @param scale     Scale of car model
	 * @param ipAddress Local IP address of client
	 * @param port      Port number of client
	 */
	public MultiplePlayer(String name, String color, TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, InetAddress ipAddress, int port) {
		super(name, color, model, position, rotX, rotY, rotZ, scale);
		this.ipAddress = ipAddress;
		this.port = port;
	}

	/**
	 * Constructor of MultiplePlayer created in the server side.
	 * 
	 * @param name      Specific name of client
	 * @param color     Car model's color
	 * @param position  Current position of player
	 * @param rotX      Player's X axis rotation
	 * @param rotY      Player's Y axis rotation
	 * @param rotZ      Player's Z axis rotation
	 * @param scale     Scale of car model
	 * @param ipAddress Local IP address of client
	 * @param port      Port number of client
	 */
	public MultiplePlayer(String name, String color, Vector3f position, float rotX, float rotY, float rotZ, float scale,
			InetAddress ipAddress, int port) {
		super(name, color, null, position, rotX, rotY, rotZ, scale);
		this.ipAddress = ipAddress;
		this.port = port;
	}

	/**
	 * Get IP address of the client owned this MultiplePlayer.
	 * 
	 * @return IP address of client
	 */
	public InetAddress getIpAddress() {
		return ipAddress;
	}

	/**
	 * Set IP address of the client owned this MultiplePlayer.
	 * 
	 * @param ipAddress IP Address
	 */
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Get the port number of the client owned this MultiplePlayer.
	 * 
	 * @return Port number of client
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Set the port number owned this MultiplePlayer.
	 * 
	 * @param port Port number
	 */
	public void setPort(int port) {
		this.port = port;
	}

}
