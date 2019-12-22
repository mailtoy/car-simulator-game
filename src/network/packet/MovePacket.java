package network.packet;

import org.lwjgl.util.vector.Vector3f;

import network.Client;
import network.Server;

/**
 * A packet contained essential movement information.
 * 
 * @author Issaree Srisomboon
 *
 */
public class MovePacket extends Packet {
	private String type;
	private Vector3f position;
	private float rotX, rotY, rotZ;

	/**
	 * Constructor of MovePacket with provided data in bytes. Define an ID as 02.
	 * 
	 * @param data Movement information in bytes.
	 */
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

	/**
	 * Constructor of MovePacket with provided movement information. Define an ID as
	 * 02.
	 * 
	 * @param type     Specific name of the controller that requires a movement
	 * @param position New position of the controller
	 * @param rotX     New rotation in X axis
	 * @param rotY     New rotation in Y axis
	 * @param rotZ     New rotation in Z axis
	 */
	public MovePacket(String type, Vector3f position, float rotX, float rotY, float rotZ) {
		super(02);
		this.type = type;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
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
		return ("02" + this.type + ":" + this.position.getX() + ":" + this.position.getY() + ":" + this.position.getZ()
				+ ":" + this.rotX + ":" + this.rotY + ":" + this.rotZ).getBytes();
	}

	/**
	 * Get a specific name of the client stored in the packet.
	 * 
	 * @return Client's type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Get a new position of the client stored in the packet.
	 * 
	 * @return Client's new position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Get a new rotation in X axis of the client stored in the packet.
	 * 
	 * @return Client's new X axis rotation
	 */
	public float getRotX() {
		return rotX;
	}

	/**
	 * Get a new rotation in Y axis of the client stored in the packet.
	 * 
	 * @return Client's new Y axis rotation
	 */
	public float getRotY() {
		return rotY;
	}

	/**
	 * Get a new rotation in Z axis of the client stored in the packet.
	 * 
	 * @return Client's new Z axis rotation
	 */
	public float getRotZ() {
		return rotZ;
	}
}