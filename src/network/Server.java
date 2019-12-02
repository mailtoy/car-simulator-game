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
import network.packet.CrashPacket;
import network.packet.DisconnectPacket;
import network.packet.MovePacket;
import network.packet.Packet;
import network.packet.Packet.PacketTypes;

public class Server extends Thread {
	private DatagramSocket socket;
	private List<MultiplePlayer> connectedPlayers;
	private List<MultiplePlayer> connectedControllers;
	private ServerGUI serverGUI;

	public Server() {
		this.serverGUI = new ServerGUI(this);
		try {
			this.socket = new DatagramSocket(3001);
			this.connectedPlayers = new ArrayList<MultiplePlayer>();
			this.connectedControllers = new ArrayList<MultiplePlayer>();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		serverGUI.appendResponse("Waiting for connection...");
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
		serverGUI.appendResponse("[" + address.getHostAddress() + ":" + port + "] " + ((ConnectPacket) packet).getType()
				+ " has connected.");

		MultiplePlayer multiplePlayer = new MultiplePlayer(((ConnectPacket) packet).getType(),
				((ConnectPacket) packet).getPosition(), ((ConnectPacket) packet).getRotX(),
				((ConnectPacket) packet).getRotY(), ((ConnectPacket) packet).getRotZ(),
				((ConnectPacket) packet).getScale(), address, port);
		addConnection(multiplePlayer, packet);
	}

	private void handleDisconnect(DisconnectPacket packet, InetAddress address, int port) {
		serverGUI.appendResponse("[" + address.getHostAddress() + ":" + port + "] "
				+ ((DisconnectPacket) packet).getType() + " has disconnected.");

		removeConnection(packet);
	}

	private void handleMove(MovePacket packet) {
		serverGUI.appendResponse(packet.getType() + " has move to " + packet.getPosition());

		if (getMultiplePlayer(packet.getType()) != null) {
			int index = getMultiplePlayerIndex(packet.getType());
			MultiplePlayer player = connectedPlayers.get(index);
			player.setPosition(packet.getPosition());
			player.setRotX(packet.getRotX());
			player.setRotY(packet.getRotY());
			player.setRotZ(packet.getRotX());

			packet.writeData(this);
//			if (connectedControllers.size() != 1) {
//				checkCrash();
//			}
		}
	}

	private void checkCrash() {
		final int carWidth = 8;
		final int carHeight = 16;
		for (int i = 0; i < connectedControllers.size() - 1; i++) {
			for (int j = i + 1; j < connectedControllers.size(); j++) {
				float playerFrameX = connectedControllers.get(i).getFrame().getX();
				float playerFrameZ = connectedControllers.get(i).getFrame().getZ();
				float nextPlayerPosX = connectedControllers.get(j).getPosition().getX();
				float nextPlayerPosZ = connectedControllers.get(i).getPosition().getZ();
				if (playerFrameX >= nextPlayerPosX && playerFrameX <= nextPlayerPosZ
						|| playerFrameX + carWidth >= nextPlayerPosX && playerFrameX + carWidth <= nextPlayerPosZ
						|| playerFrameX <= nextPlayerPosX && playerFrameX + carWidth >= nextPlayerPosZ) {
					System.out.println("crash?");
					if (playerFrameZ >= nextPlayerPosX && playerFrameZ <= nextPlayerPosZ
							|| playerFrameZ + carHeight >= nextPlayerPosX && playerFrameZ + carHeight <= nextPlayerPosZ
							|| playerFrameZ <= nextPlayerPosX && playerFrameZ + carHeight >= nextPlayerPosZ) {
						System.out.println("crash!");
					}
				}
			}
		}
	}

	private void addConnection(MultiplePlayer multiplePlayer, ConnectPacket packet) {
		boolean isConnected = false;
		packet.setMap(serverGUI.getSelectedMap());

		for (MultiplePlayer player : connectedPlayers) {
			if (multiplePlayer.getType().equals(player.getType())) {
				player.setIpAddress(multiplePlayer.getIpAddress());
				player.setPort(multiplePlayer.getPort());
				isConnected = true;
			} else {
				// relay to the current connected player (multiplePlayer) that there is a new
				// player (player)
				sendData(packet.getData(), player.getIpAddress(), player.getPort());

				// relay to the new player (player) that the currently connected player
				// (multiplePlayer) exists
				ConnectPacket updatePacket = new ConnectPacket(player.getType(), serverGUI.getSelectedMap(),
						player.getPosition(), player.getRotX(), player.getRotY(), player.getRotZ(), player.getScale());
				sendData(updatePacket.getData(), multiplePlayer.getIpAddress(), multiplePlayer.getPort());
			}
		}
		if (!isConnected) {
			connectedPlayers.add(multiplePlayer);
			if (packet.getType().contains("Controller")) {
				connectedControllers.add(multiplePlayer);
			}
			packet.writeData(this);
		}
		if (connectedPlayers.size() != 0) {
			serverGUI.setMapEnabled(false);
		}
	}

	private void removeConnection(DisconnectPacket packet) {
		this.connectedPlayers.remove(getMultiplePlayerIndex(packet.getType()));
		packet.writeData(this);

		if (connectedPlayers.size() == 0) {
			serverGUI.setMapEnabled(true);
		}
	}

	private MultiplePlayer getMultiplePlayer(String type) {
		for (MultiplePlayer player : connectedPlayers) {
			if (player.getType().equals(type)) {
				return player;
			}
		}
		return null;
	}

	private int getMultiplePlayerIndex(String type) {
		int index = 0;
		for (MultiplePlayer player : connectedPlayers) {
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