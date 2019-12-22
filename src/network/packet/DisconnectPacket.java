package network.packet;

import network.Client;
import network.Server;

/**
 * A packet contained essential disconnection information.
 * 
 * @author Issaree Srisomboon
 *
 */
public class DisconnectPacket extends Packet {
	private String type;

	/**
	 * Constructor of DisconnectPacket with provided data in bytes. Define an ID as
	 * 01.
	 * 
	 * @param data Disconnection information in bytes
	 */
	public DisconnectPacket(byte[] data) {
		super(01);
		this.type = readData(data);
	}

	/**
	 * Constructor of DisconnectPacket with provided type of client that requires to
	 * disconnect. Define an ID as 01.
	 * 
	 * @param type Type of disconnected client
	 */
	public DisconnectPacket(String type) {
		super(01);
		this.type = type;
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
		return ("01" + this.type).getBytes();
	}

	/**
	 * Get type of disconnected client stored in the packet.
	 * 
	 * @return Type of disconnected client.
	 */
	public String getType() {
		return type;
	}
}