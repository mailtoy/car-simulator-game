package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import entities.MultiplePlayer;
import main.WindowDisplay;
import network.packet.ConnectPacket;
import network.packet.Packet;
import network.packet.Packet.PacketTypes;

public class Client extends Thread {
	private final String serverIP = "10.223.115.18";
	private InetAddress ipAddress;
	private DatagramSocket socket;

	private WindowDisplay windowDisplay;

	public Client(WindowDisplay windowDisplay) {
		this.windowDisplay = windowDisplay;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(serverIP);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		PacketTypes packetTypes = Packet.lookupPacket(new String(data).trim().substring(0, 2));
		Packet packet;

		switch (packetTypes) {
		case INVALID:
			break;
		case CONNECT:
			packet = new ConnectPacket(data);
			System.out.println(address.getHostAddress() + ":" + port + " " + ((ConnectPacket) packet).getType()
					+ " has joined the server.");

			MultiplePlayer multiplePlayer = new MultiplePlayer(((ConnectPacket) packet).getType(),
					this.windowDisplay.getPlayer().getModel(), ((ConnectPacket) packet).getPosition(),
					((ConnectPacket) packet).getRotX(), ((ConnectPacket) packet).getRotY(),
					((ConnectPacket) packet).getRotZ(), ((ConnectPacket) packet).getScale(), address, port);
			this.windowDisplay.addEntity(multiplePlayer);
			break;
		case DISCONNECT:
			break;
		default:
			break;
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 3001);

		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
