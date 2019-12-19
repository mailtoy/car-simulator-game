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
import network.packet.CrashPacket;
import network.packet.DisconnectPacket;
import network.packet.MovePacket;
import network.packet.Packet;
import network.packet.Packet.PacketTypes;

/**
 * Client creates a UDP socket to the server.
 * 
 * @author Issaree Srisomboon
 *
 */
public class Client extends Thread {
	private final String serverIP = "10.223.119.241";

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private WindowDisplay windowDisplay;

	/**
	 * Create a socket object for carrying the data
	 * 
	 * @param windowDisplay An abstract class for each type of client.
	 */
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

	/**
	 * Receive incoming packets from the server.
	 */
	@Override
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

	/**
	 * Organize the packet based on its type.
	 * 
	 * @param data    Data of packet
	 * @param address IP address of sender's packet
	 * @param port    Port of sender's packet
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
		case CRASH:
			packet = new CrashPacket(data);
			handleCrash((CrashPacket) packet);
			break;
		default:
			break;
		}
	}

	/**
	 * Send a data packet to the server.
	 * 
	 * @param data Data that wants to send through the server.
	 */
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 3001);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle a new connection from other clients in the server. Add a new player
	 * (car) to the local map if it is a controller and it still is not added.
	 * 
	 * @param packet  ConnectPacket sent from server
	 * @param address IP address of the new client
	 * @param port    Port of the new client
	 */
	private void handleConnect(ConnectPacket packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((ConnectPacket) packet).getType()
				+ " has joined the server.");

		String packetMap = ((ConnectPacket) packet).getMap();
		if (!packetMap.equals(windowDisplay.getDefaultMap())) {
			windowDisplay.setMap(packetMap);
		}

		String packetType = ((ConnectPacket) packet).getType();
		if (packetType.contains("Controller") && !windowDisplay.isAdded(packetType)) {
			MultiplePlayer multiplePlayer = new MultiplePlayer(packetType, ((ConnectPacket) packet).getCarColor(),
					windowDisplay.getCarModel(), ((ConnectPacket) packet).getPosition(),
					((ConnectPacket) packet).getRotX(), ((ConnectPacket) packet).getRotY(),
					((ConnectPacket) packet).getRotZ(), ((ConnectPacket) packet).getScale(), address, port);
			windowDisplay.addMultiplePlayer(multiplePlayer);
		}
	}

	/**
	 * Handle a disconnection from other clients in the server. If the disconnected
	 * client was a controller then remove its car from the map.
	 * 
	 * @param packet  DisconnectPacket sent from the server
	 * @param address IP address of the disconnected client
	 * @param port    Port of the disconnected client
	 */
	private void handleDisconnect(DisconnectPacket packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((DisconnectPacket) packet).getType()
				+ " has left from the server.");

		String packetType = ((DisconnectPacket) packet).getType();
		if (packetType.contains("Controller")) {
			windowDisplay.removeMultiplePlayer(packetType);
		}
		// If server forces disconnecting
		if (packetType.equals(windowDisplay.getType())) {
			windowDisplay.setKick(true);
			System.exit(0);
		}
	}

	/**
	 * Make the client move the existing car in the map followed by MovePacket.
	 * 
	 * @param packet A packet contained a new position and rotation.
	 */
	private void handleMove(MovePacket packet) {
		windowDisplay.movePlayer(packet.getType(), packet.getPosition(), packet.getRotX(), packet.getRotY(),
				packet.getRotZ());
	}

	/**
	 * Handle a crash situation. Check whether this client is crashing or not.
	 * 
	 * @param packet A packet contained information of two cars crashed.
	 */
	private void handleCrash(CrashPacket packet) {
		String type = windowDisplay.getType();
		if (type.equals(packet.getPlayer1()) || type.equals(packet.getPlayer2())) {
			windowDisplay.setCrash(true);
		}
	}
}
