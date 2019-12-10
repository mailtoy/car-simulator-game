package network.packet;

import network.Client;
import network.Server;

public class CrashPacket extends Packet {
	private String player1;
	private String player2;

	public CrashPacket(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(":");
		this.player1 = dataArray[0];
		this.player2 = dataArray[1];
	}

	public CrashPacket(String player1, String player2) {
		super(03);
		this.player1 = player1;
		this.player2 = player2;
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
		return ("03" + this.player1 + ":" + this.player2).getBytes();
	}

	public String getPlayer1() {
		return player1;
	}

	public String getPlayer2() {
		return player2;
	}

}
