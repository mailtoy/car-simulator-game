package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import entities.MultiplePlayer;
import network.packet.ConnectPacket;
import network.packet.CrashPacket;
import network.packet.DisconnectPacket;
import network.packet.MovePacket;
import network.packet.Packet;
import network.packet.Packet.PacketTypes;

/**
 * Server acts as the central clearing house of all interactions handling
 * connections and clients behavior.
 * 
 * @author Issaree Srisomboon
 *
 */
public class Server extends Thread {
	private DatagramSocket socket;
	private ArrayList<MultiplePlayer> connectedPlayers;
	private ServerGUI serverGUI;

	/**
	 * Start the UDP connection server with requested port.
	 * 
	 * @param port Port the server listens on.
	 */
	public Server(int port) {
		this.serverGUI = new ServerGUI(this);
		this.connectedPlayers = new ArrayList<MultiplePlayer>();
		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receive incoming packets sent from clients.
	 */
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

	/**
	 * Organize the packet based on its type.
	 * 
	 * @param data    Data of packet
	 * @param address IP address of packet
	 * @param port    Port of packet
	 */
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

	/**
	 * Send a data packet to the provided IP address and port.
	 * 
	 * @param data      Data that wants to send through
	 * @param ipAddress Receiver's IP address
	 * @param port      Receiver's port
	 */
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a data packet to all connected clients within the server.
	 * 
	 * @param data Data that wants to send through.
	 */
	public void broadcast(byte[] data) {
		for (MultiplePlayer player : connectedPlayers) {
			sendData(data, player.getIpAddress(), player.getPort());
		}
	}

	/**
	 * Check the type of client whether it is controller or not.
	 * 
	 * @param type Type specified of provided client.
	 * @return boolean Return true if this client is a controller otherwise, return
	 *         false.
	 */
	public boolean isController(String type) {
		return type.contains("Controller");
	}

	/**
	 * Handle a connection of clients by creating a new player using the connect
	 * packet's data and add it into the server. Append a status of connection in
	 * the server interface.
	 * 
	 * @param packet  ConnectPacket sent from client
	 * @param address IP address of the client
	 * @param port    Port of the client
	 */
	private void handleConnect(ConnectPacket packet, InetAddress address, int port) {
		serverGUI.appendResponse("[" + address.getHostAddress() + ":" + port + "] " + ((ConnectPacket) packet).getType()
				+ " has connected.");

		MultiplePlayer multiplePlayer = new MultiplePlayer(((ConnectPacket) packet).getType(),
				((ConnectPacket) packet).getCarColor(), ((ConnectPacket) packet).getPosition(),
				((ConnectPacket) packet).getRotX(), ((ConnectPacket) packet).getRotY(),
				((ConnectPacket) packet).getRotZ(), ((ConnectPacket) packet).getScale(), address, port);
		addConnection(multiplePlayer, packet);
	}

	/**
	 * Handle a disconnection of clients. Append a status of disconnection in the
	 * server interface.
	 * 
	 * @param packet
	 * @param address
	 * @param port
	 */
	private void handleDisconnect(DisconnectPacket packet, InetAddress address, int port) {
		serverGUI.appendResponse("[" + address.getHostAddress() + ":" + port + "] "
				+ ((DisconnectPacket) packet).getType() + " has disconnected.");

		removeConnection(packet);
	}

	/**
	 * Handle movement of each client (controller). Check by the packet type whether
	 * it exists in the server or not. If it does, set a new position and rotation
	 * followed by the packet data and broadcast to all clients within the server
	 * that this controller's car is changing the position. And if there are two or
	 * more controllers' car connected to the server, then check a car crash
	 * situation.
	 * 
	 * @param packet Packet contains a new position and a rotation view of the
	 *               client.
	 */
	private void handleMove(MovePacket packet) {
		if (getMultiplePlayer(packet.getType()) != null) {
			int index = getMultiplePlayerIndex(packet.getType());
			MultiplePlayer player = connectedPlayers.get(index);
			player.setPosition(packet.getPosition());
			player.setRotX(packet.getRotX());
			player.setRotY(packet.getRotY());
			player.setRotZ(packet.getRotX());

			packet.writeData(this);
			if (getConnectedControllers().size() >= 2) {
				handleCrash(getConnectedControllers());
			}
		}
	}

	/**
	 * Handle a crash situation occurred by two or more controllers' car. Set a
	 * frame of each car based on its position and then check if their frames are
	 * currently overlapped or not. If there is a car crash then send a CrashPacket
	 * contained the type of injured cars and broadcast to all the connected
	 * clients.
	 * 
	 * @param controllers ArrayList contained only clients that are controller.
	 */
	private void handleCrash(ArrayList<MultiplePlayer> controllers) {
		final int carWidth = 6;
		final int carHeight = 14;
		for (int i = 0; i < controllers.size() - 1; i++) {
			for (int j = i + 1; j < controllers.size(); j++) {
				controllers.get(i).setFrame();
				controllers.get(j).setFrame();
				float playerFrameX = controllers.get(i).getFrame().getX();
				float playerFrameZ = controllers.get(i).getFrame().getZ();
				float nextPlayerPosX = controllers.get(j).getFrame().getX();
				float nextPlayerPosZ = controllers.get(j).getFrame().getZ();

				// logic here pls
				if ((playerFrameX >= nextPlayerPosX && playerFrameX <= nextPlayerPosX + carWidth)
						|| (playerFrameX + carWidth >= nextPlayerPosX
								&& playerFrameX + carWidth <= nextPlayerPosX + carWidth)) {
					if ((playerFrameZ >= nextPlayerPosZ && playerFrameZ <= nextPlayerPosZ + carHeight)
							|| (playerFrameZ + carHeight >= nextPlayerPosZ
									&& playerFrameZ + carHeight <= nextPlayerPosZ + carHeight)) {
						serverGUI.appendResponse(controllers.get(i).getType() + " and " + controllers.get(j).getType()
								+ " are crashing!");

						CrashPacket crashPacket = new CrashPacket(controllers.get(i).getType(),
								controllers.get(j).getType());
						crashPacket.writeData(this);
					}
				}
			}
		}
	}

	/**
	 * AIf this client already in the server then update just its IP address and
	 * port otherwise, add a new connection of this client and update to other
	 * existing clients that there is a new client in the server.
	 * 
	 * @param multiplePlayer A player created based on the ConnectPacket.
	 * @param packet         ConnectPacket sent from client.
	 */
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
						player.getColor(), player.getPosition(), player.getRotX(), player.getRotY(), player.getRotZ(),
						player.getScale());
				sendData(updatePacket.getData(), multiplePlayer.getIpAddress(), multiplePlayer.getPort());
			}
		}
		if (!isConnected) {
			connectedPlayers.add(multiplePlayer);
			String type = multiplePlayer.getType();
			serverGUI.addClient(isController(type) ? type + ":" + multiplePlayer.getColor() : type);
			packet.writeData(this);
		}
		if (connectedPlayers.size() != 0) {
			serverGUI.setMapEnabled(false);
		}
	}

	/**
	 * Remove a client from the server then broadcast to all the connected clients.
	 * 
	 * @param packet DisconnectPacket sent from client.
	 */
	private void removeConnection(DisconnectPacket packet) {
		this.connectedPlayers.remove(getMultiplePlayerIndex(packet.getType()));
		serverGUI.removeClient(packet.getType());
		if (connectedPlayers.size() == 0) {
			serverGUI.setMapEnabled(true);
		}
		packet.writeData(this);
	}

	/**
	 * Get a player (car) of provided client type.
	 * 
	 * @param type A client's type
	 * @return Return the player if it exists in the server otherwise, return null.
	 */
	private MultiplePlayer getMultiplePlayer(String type) {
		for (MultiplePlayer player : connectedPlayers) {
			if (player.getType().equals(type)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Get an index of a player (car) of provided client type.
	 * 
	 * @param type A client's type
	 * @return Return the index of player if it exists in the server otherwise,
	 *         return the index of 0.
	 */
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

	/**
	 * Get ArrayList contained all of controllers connected in the server.
	 * 
	 * @return ArrayList of controllers
	 */
	private ArrayList<MultiplePlayer> getConnectedControllers() {
		ArrayList<MultiplePlayer> controllers = new ArrayList<MultiplePlayer>();
		for (MultiplePlayer player : connectedPlayers) {
			if (isController(player.getType())) {
				controllers.add(player);
			}
		}
		return controllers;
	}

	/**
	 * Get a local IP address of the server.
	 * 
	 * @return The server's host IP address.
	 */
	public String getLocalIPAddr() {
		String ipAddress = null;
		try {
			Enumeration<NetworkInterface> netEnum = NetworkInterface.getNetworkInterfaces();
			while (netEnum.hasMoreElements()) {
				for (InterfaceAddress interfaceAddr : netEnum.nextElement().getInterfaceAddresses()) {
					if (interfaceAddr.getAddress().isSiteLocalAddress()) {
						ipAddress = interfaceAddr.getAddress().getHostAddress().trim();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipAddress;
	}

	public static void main(String[] args) {
		new Server(3001).start();
	}
}