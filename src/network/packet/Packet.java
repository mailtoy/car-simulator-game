package network.packet;

import network.Client;
import network.Server;

/**
 * Network packet carries the information that will help it get to its
 * destination.
 * 
 * @author Issaree Srisomboon
 *
 */
public abstract class Packet {

	/**
	 * Types of packet defined by its ID.
	 */
	public static enum PacketTypes {
		INVALID(-1), CONNECT(00), DISCONNECT(01), MOVE(02), CRASH(03);

		private int packetId;

		/**
		 * Constructor of type of packet.
		 * 
		 * @param packetId ID defines a type of a packet.
		 */
		private PacketTypes(int packetId) {
			this.packetId = packetId;
		}

		/**
		 * Get ID of packet type.
		 * 
		 * @return int ID of packet type.
		 */
		public int getId() {
			return this.packetId;
		}
	}

	public byte packetId;

	/**
	 * Constructor of packet.
	 * 
	 * @param packetId ID defines a type of a packet.
	 */
	public Packet(int packetId) {
		this.packetId = (byte) packetId;
	}

	/**
	 * Send a packet to the Server from this Client.
	 * 
	 * @param client Client that wants to send a packet through the server.
	 */
	public abstract void writeData(Client client);

	/**
	 * Send a packet to all the clients within this Server.
	 * 
	 * @param server Server that wants to send a packet to all clients.
	 */
	public abstract void writeData(Server server);

	/**
	 * Read a packet data in byte and return without its ID.
	 * 
	 * @param data Packet data
	 * @return Data of packet in String.
	 */
	public String readData(byte[] data) {
		return new String(data).trim().substring(2);
	}

	/**
	 * Get a data out of packet.
	 * 
	 * @return The data of packet in bytes.
	 */
	public abstract byte[] getData();

	/**
	 * Get a type of packet from provided ID.
	 * 
	 * @param id ID of the packet
	 * @return A type of packet if it is valid otherwise, return type of invalid
	 *         packet.
	 */
	public static PacketTypes lookupPacket(int id) {
		for (PacketTypes packetType : PacketTypes.values()) {
			if (packetType.getId() == id) {
				return packetType;
			}
		}
		return PacketTypes.INVALID;
	}

	/**
	 * Get a type of packet from provided ID in String.
	 * 
	 * @param idString ID in String of the packet
	 * @return A type of packet if it is valid otherwise, return type of invalid
	 *         packet.
	 */
	public static PacketTypes lookupPacket(String idString) {
		try {
			return lookupPacket(Integer.parseInt(idString));
		} catch (NumberFormatException e) {
			return PacketTypes.INVALID;
		}
	}
}
