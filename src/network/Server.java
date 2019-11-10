package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import entities.MultiplePlayer;
import network.packet.ConnectPacket;
import network.packet.Packet;
import network.packet.Packet.PacketTypes;

public class Server extends Thread {
	private DatagramSocket socket;
	private List<MultiplePlayer> connectedPlayers;

	public Server() {
		try {
			this.socket = new DatagramSocket(3001);
			this.connectedPlayers = new ArrayList<MultiplePlayer>();
		} catch (SocketException e) {
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
					+ " has connected.");
			
			MultiplePlayer multiplePlayer = new MultiplePlayer(((ConnectPacket) packet).getName(),
					((ConnectPacket) packet).getModel(), ((ConnectPacket) packet).getPosition(),
					((ConnectPacket) packet).getRotX(), ((ConnectPacket) packet).getRotY(),
					((ConnectPacket) packet).getRotZ(), ((ConnectPacket) packet).getScale(), address, port);
			addConnection(multiplePlayer, ((ConnectPacket) packet));
			break;
		case DISCONNECT:
			break;
		default:
			break;
		}
	}

	public void addConnection(MultiplePlayer players, ConnectPacket packet) {
		boolean isConnected = false;
		for (MultiplePlayer player : this.connectedPlayers) {
			if (players.getName().equalsIgnoreCase(player.getName())) {
				if (player.getIpAddress() == null) {
					player.setIpAddress(players.getIpAddress());
				}
				if (player.getPort() == -1) {
					player.setPort(players.getPort());
				}
			} else {
				sendData(packet.getData(), player.getIpAddress(), player.getPort());
			}
		}
		if (!isConnected) {
			connectedPlayers.add(players);
			packet.writeData(this);
		}
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);

		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcast(byte[] data) {
		for (MultiplePlayer player : connectedPlayers) {
			sendData(data, player.getIpAddress(), player.getPort());
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}