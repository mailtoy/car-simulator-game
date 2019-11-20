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
import network.packet.DisconnectPacket;
import network.packet.MovePacket;
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

	@Override
	public void run() {
		System.out.println("Waiting for connection...");
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
			handleConnect((ConnectPacket) packet, address, port);
			break;
		case DISCONNECT:
			packet = new DisconnectPacket(data);
			handleDisconnect((DisconnectPacket) packet, address, port);
			break;
		case MOVE:
			packet = new MovePacket(data);
			handleMove((MovePacket) packet);
			break;
		default:
			break;
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

	private void handleConnect(ConnectPacket packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((ConnectPacket) packet).getType()
				+ " has connected.");

		MultiplePlayer multiplePlayer = new MultiplePlayer(((ConnectPacket) packet).getType(),
				((ConnectPacket) packet).getPosition(), ((ConnectPacket) packet).getRotX(),
				((ConnectPacket) packet).getRotY(), ((ConnectPacket) packet).getRotZ(),
				((ConnectPacket) packet).getScale(), address, port);
		addConnection(multiplePlayer, ((ConnectPacket) packet));
	}

	private void handleDisconnect(DisconnectPacket packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((DisconnectPacket) packet).getType()
				+ " has disconnected.");

		removeConnection((DisconnectPacket) packet);
	}

	private void handleMove(MovePacket packet) {
		System.out.println(packet.getType() + " has move to " + packet.getPosition());

		if (getMultiplePlayer(packet.getType()) != null) {
			int index = getMultiplePlayerIndex(packet.getType());
			connectedPlayers.get(index).setPosition(packet.getPosition());
			connectedPlayers.get(index).setRotX(packet.getRotX());
			connectedPlayers.get(index).setRotY(packet.getRotY());
			connectedPlayers.get(index).setRotZ(packet.getRotX());

			packet.writeData(this);
		}
	}

	private void addConnection(MultiplePlayer multiplePlayer, ConnectPacket packet) {
		boolean isConnected = false;

		for (MultiplePlayer player : this.connectedPlayers) {
			if (multiplePlayer.getType().equals(player.getType())) { 
				if (player.getIpAddress() == null) { // fix this later
					player.setIpAddress(multiplePlayer.getIpAddress());
				}
				if (player.getPort() == -1) { // fix this later
					player.setPort(multiplePlayer.getPort());
				}
				isConnected = true;
			} else {
				// relay to the current connected player (multiplePlayer) that there is a new
				// player (player)
				sendData(packet.getData(), player.getIpAddress(), player.getPort());

				// relay to the new player (player) that the currently connected player
				// (multiplePlayer) exists
				packet = new ConnectPacket(player.getType(), player.getModel(), player.getPosition(), player.getRotX(),
						player.getRotY(), player.getRotZ(), player.getScale());
				sendData(packet.getData(), multiplePlayer.getIpAddress(), multiplePlayer.getPort());
			}
		}
		if (!isConnected) {
			connectedPlayers.add(multiplePlayer);
			packet.writeData(this);
		}
	}

	private void removeConnection(DisconnectPacket packet) {
		this.connectedPlayers.remove(getMultiplePlayerIndex(packet.getType()));
		packet.writeData(this);
	}

	private MultiplePlayer getMultiplePlayer(String type) {
		for (MultiplePlayer player : this.connectedPlayers) {
			return player.getType().equals(type) ? player : null;
		}
		return null;
	}

	private int getMultiplePlayerIndex(String type) {
		int index = 0;
		for (MultiplePlayer player : this.connectedPlayers) {
			if (player.getType().equals(type)) {
				break;
			}
			index++;
		}
		return index;
	}

	public static void main(String[] args) {
		new Server().start();
	}
}