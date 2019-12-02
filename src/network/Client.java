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

public class Client extends Thread {
	private final String serverIP = "10.223.119.241";
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

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 3001);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleConnect(ConnectPacket packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((ConnectPacket) packet).getType()
				+ " has joined the server.");

		String packetMap = ((ConnectPacket) packet).getMap();
		if (!packetMap.equals(windowDisplay.getDefaultMap())) {
			windowDisplay.setMap(packetMap);
		}

		String packetType = ((ConnectPacket) packet).getType();
		if (packetType.contains("Controller") && !windowDisplay.isAdded(packetType)) {
			MultiplePlayer multiplePlayer = new MultiplePlayer(packetType, windowDisplay.getCarModel(),
					((ConnectPacket) packet).getPosition(), ((ConnectPacket) packet).getRotX(),
					((ConnectPacket) packet).getRotY(), ((ConnectPacket) packet).getRotZ(),
					((ConnectPacket) packet).getScale(), address, port);
			windowDisplay.addMultiplePlayer(multiplePlayer);
		}
	}

	private void handleDisconnect(DisconnectPacket packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((DisconnectPacket) packet).getType()
				+ " has left from the server.");

		String packetType = ((DisconnectPacket) packet).getType();
		if (packetType.contains("Controller")) {
			windowDisplay.removeMultiplePlayer(packetType);
		}
	}

	private void handleMove(MovePacket packet) {
		windowDisplay.movePlayer(packet.getType(), packet.getPosition(), packet.getRotX(), packet.getRotY(),
				packet.getRotZ());
	}

	private void handleCrash(CrashPacket packet) {
		String type = windowDisplay.getType();
		if (type.equals(packet.getPlayer1()) || type.equals(packet.getPlayer2())) {

		}
	}
}
