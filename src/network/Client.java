package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import entities.MultiplePlayer;
import main.Controller;
import network.packet.ConnectPacket;
import network.packet.Packet;
import network.packet.Packet.PacketTypes;

public class Client extends Thread {
	private final String serverIP = "10.223.115.18";
	private InetAddress ipAddress;
	private DatagramSocket socket;

	private Controller controller;

	public Client(Controller controller) {
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(serverIP);
			this.controller = controller;
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
			System.out.println(address.getHostAddress() + ":" + port + " " + ((ConnectPacket) packet).getName()
					+ " has joined the server.");

			MultiplePlayer multiplePlayer = new MultiplePlayer(((ConnectPacket) packet).getName(),
					((ConnectPacket) packet).getModel(), ((ConnectPacket) packet).getPosition(),
					((ConnectPacket) packet).getRotX(), ((ConnectPacket) packet).getRotY(),
					((ConnectPacket) packet).getRotZ(), ((ConnectPacket) packet).getScale(), address, port);
			controller.renderEntity(multiplePlayer);
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
