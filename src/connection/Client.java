package connection;

import java.io.*;
import java.net.*;

//import view.ControllerController;
//import view.SimulatorController;

public class Client {
	private final int remoteServerPort = 3001;
	private String serverIP = "192.168.0.246";

	private Socket serverSocket;
	private BufferedReader reader;
	private PrintWriter writer;
	private ClientType clientType;

	public Client(ClientType type) {
		this.clientType = type;
	}

	public void connect() {
		try {
			InetAddress remoteServerInetAddress = InetAddress.getByName(serverIP);
			serverSocket = new Socket(remoteServerInetAddress, remoteServerPort);

			InputStreamReader streamReader = new InputStreamReader(serverSocket.getInputStream());
			reader = new BufferedReader(streamReader);

			writer = new PrintWriter(serverSocket.getOutputStream());

			writer.println("Connect:" + typeSpecify() + ":has connected.");
			writer.flush();

			Thread thread = new Thread(new IncomingReader(reader, clientType));
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String typeSpecify() {
		String type = clientType.toString();
		return type.charAt(5) == 'C' ? type.substring(5, 15) : type.substring(5, 14);
	}

	public void sendCommand(String command) {
		writer.println("Command:" + typeSpecify() + ":" + command);
		writer.flush();
	}

	public void sendResponse(String response) {
		writer.println("Response:" + typeSpecify() + ":" + response);
		writer.flush();
	}

	public void disconnect() {
		try {
			writer.println("Disconnect:" + typeSpecify() + ":has disconnected.");
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class IncomingReader implements Runnable {
	private BufferedReader reader;
	private ClientType client;

	public IncomingReader(BufferedReader reader, ClientType client) {
		this.reader = reader;
		this.client = client;
	}

	@Override
	public void run() {
		String stream, messageType, clientType, message;
		String[] messageArray = null;
		try {

			// modify here
			while ((stream = reader.readLine()) != null) {
				System.out.println(stream);
				messageArray = stream.split(":");

				messageType = messageArray[0];
				clientType = messageArray[1];
				message = messageArray[2];

				switch (messageType) {
				case "Connect":
				case "Disconnect":
					client.write(clientType + " " + message);
					break;
				case "Command":
//					if (client.getClass().equals(SimulatorController.class)) {
//						client.write(message);
//					}
//					break;
				case "Response":
//					if (client.getClass().equals(ControllerController.class)) {
//						client.write(message);
//					}
//					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
