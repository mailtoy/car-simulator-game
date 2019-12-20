package network.packet;

import org.lwjgl.util.vector.Vector3f;

import network.Client;
import network.Server;

/**
 * Packet that contains essential connection information.
 * 
 * @author Issaree Srisomboon
 *
 */
public class ConnectPacket extends Packet {
	private String type, map, carColor;
	private Vector3f position;
	private float rotX, rotY, rotZ, scale;

	/**
	 * Constructor of ConnectPacket with provided data in bytes. Define an ID as 00;
	 * 
	 * @param data Connection information in bytes.
	 */
	public ConnectPacket(byte[] data) {
		super(00);

		String[] dataArray = readData(data).split(":");
		// Controller connected
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
			// Simulator connected
			this.type = dataArray[0];
			this.map = dataArray[1];
		}
	}

	/**
	 * Constructor of ConnectPacket for the simulator which only needs to store type
	 * and map of it. Define a default ID as 00;
	 * 
	 * @param type Specific name of the simulator that wants to connect.
	 * @param map  Map showed in the window displaying.
	 */
	public ConnectPacket(String type, String map) {
		super(00);
		this.type = type;
		this.map = map;
	}

	/**
	 * Constructor of ConnectPacket for the controller which needs the player (car)
	 * information in order to be stored in the server. Define a default ID as 00;
	 * 
	 * @param type     Specific name of the controller that wants to connect
	 * @param map      Map showed in the window displaying
	 * @param carColor Color of the car model
	 * @param position Initial position of the car
	 * @param rotX     Car's rotation in X axis
	 * @param rotY     Car's rotation in Y axis
	 * @param rotZ     Car's rotation in Z axis
	 * @param scale    Size of car
	 */
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

	/**
	 * Send a packet to the Server from this Client.
	 * 
	 * @param client Client that wants to send a packet through the server.
	 */
	@Override
	public void writeData(Client client) {
		client.sendData(getData());
	}

	/**
	 * Send a packet to all the clients within this Server.
	 * 
	 * @param server Server that wants to send a packet to all clients.
	 */
	@Override
	public void writeData(Server server) {
		server.broadcast(getData());
	}

	/**
	 * Get a data out of packet.
	 * 
	 * @return The data of packet in bytes.
	 */
	@Override
	public byte[] getData() {
		return this.type.contains("Controller")
				? ("00" + this.type + ":" + this.map + ":" + this.carColor + ":" + this.position.getX() + ":"
						+ this.position.getY() + ":" + this.position.getZ() + ":" + this.rotX + ":" + this.rotY + ":"
						+ this.rotZ + ":" + this.scale).getBytes()
				: ("00" + this.type + ":" + this.map).getBytes();
	}

	/**
	 * Get a specific name of the client stored in the packet.
	 * 
	 * @return Client's type stored in the packet
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get a map of the client stored in the packet.
	 * 
	 * @return Client's map stored in the packet.
	 */
	public String getMap() {
		return map;
	}

	/**
	 * Modified a map attribute stored in the packet.
	 * 
	 * @param map A new map
	 */
	public void setMap(String map) {
		this.map = map;
	}

	/**
	 * Get a current position of the car
	 * 
	 * @return Position of a the car
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Get a current X axis rotation of the car.
	 * 
	 * @return X axis rotation of the car
	 */
	public float getRotX() {
		return rotX;
	}

	/**
	 * Get a current Y axis rotation of the car.
	 * 
	 * @return Y axis rotation of the car
	 */
	public float getRotY() {
		return rotY;
	}

	/**
	 * Get a current Z axis rotation of the car.
	 * 
	 * @return Z axis rotation of the car
	 */
	public float getRotZ() {
		return rotZ;
	}

	/**
	 * Get a scale of the car model.
	 * 
	 * @return A scale of the car model
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Get a color of the car model.
	 * 
	 * @return A scale of the car model
	 */
	public String getCarColor() {
		return carColor;
	}
}