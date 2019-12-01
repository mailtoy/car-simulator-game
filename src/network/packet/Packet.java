package network.packet;

import network.Client;
import network.Server;

public abstract class Packet {

	public static enum PacketTypes {
		INVALID(-1), CONNECT(00), DISCONNECT(01), MOVE(02), CRASH(03);

		private int packetId;

		private PacketTypes(int packetId) {
			this.packetId = packetId;
		}

		public int getId() {
			return this.packetId;
		}
	}

	public byte packetId;

	public Packet(int packetId) {
		this.packetId = (byte) packetId;
	}

	/**
	 * Send a packet to the Server from this Client.
	 * 
	 * @param client
	 */
	public abstract void writeData(Client client);

	/**
	 * Send a packet to all the clients within this Server.
	 * 
	 * @param server
	 */
	public abstract void writeData(Server server);

	public String readData(byte[] data) {
		return new String(data).trim().substring(2);
	}

	public abstract byte[] getData();

	public static PacketTypes lookupPacket(int id) {
		for (PacketTypes packetType : PacketTypes.values()) {
			if (packetType.getId() == id) {
				return packetType;
			}
		}
		return PacketTypes.INVALID;
	}

	public static PacketTypes lookupPacket(String idString) {
		try {
			return lookupPacket(Integer.parseInt(idString));
		} catch (NumberFormatException e) {
			return PacketTypes.INVALID;
		}
	}
}
