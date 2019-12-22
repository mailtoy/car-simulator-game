package network.packet;

import network.Client;
import network.Server;

/**
 * A packet contained essential crashing information.
 * 
 * @author Issaree Srisomboon
 *
 */
public class CrashPacket extends Packet {
	private String player1;
	private String player2;

	/**
	 * Constructor of ConnectPacket with provided data in bytes. Define an ID as 03.
	 * 
	 * @param data Crash information in bytes
	 */
	public CrashPacket(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(":");
		this.player1 = dataArray[0];
		this.player2 = dataArray[1];
	}

	/**
	 * Constructor of CrashPacket with provided names of crashing cars. Define an ID
	 * as 03.
	 * 
	 * @param player1 Name of crashing car
	 * @param player2 Name of crashing car
	 */
	public CrashPacket(String player1, String player2) {
		super(03);
		this.player1 = player1;
		this.player2 = player2;
	}

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
		return ("03" + this.player1 + ":" + this.player2).getBytes();
	}

	/**
	 * Get a name of the first crashing car stored in the packet.
	 * 
	 * @return Name of crashing car
	 */
	public String getPlayer1() {
		return player1;
	}

	/**
	 * Get a name of the second crashing car stored in the packet.
	 * 
	 * @return Name of crashing car
	 */
	public String getPlayer2() {
		return player2;
	}

}
