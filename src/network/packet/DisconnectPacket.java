package network.packet;

import network.Client;
import network.Server;

public class DisconnectPacket extends Packet {
	private String type;

	public DisconnectPacket(byte[] data) {
		super(01);
		this.type = readData(data);
	}

	public DisconnectPacket(String type) {
		super(01);
		this.type = type;
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
		return ("01" + this.type).getBytes();
	}

	public String getType() {
		return type;
	}
}