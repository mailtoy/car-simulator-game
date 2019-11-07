package connection;

import java.io.*;
import java.net.*;

import org.lwjgl.util.vector.Vector3f;

/**
 * Client creates a TCP socket to Server.
 * 
 * @author Issaree Srisomboon
 *
 */
public class Client {
	private final int remoteServerPort = 3001;
	private String serverIP = "203.246.112.148";
	private Socket serverSocket;
	private ClientType clientType;

	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;

	public Client(ClientType type) {
		this.clientType = type;
	}

	public void connect() {
		try {
			InetAddress remoteServerInetAddress = InetAddress.getByName(serverIP);
			serverSocket = new Socket(remoteServerInetAddress, remoteServerPort);

			InputStream inputStream = serverSocket.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);

			objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());

			sendConnect();

			Thread thread = new Thread(new IncomingInput(objectInputStream, clientType));
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String typeSpecify() {
		String type = clientType.toString();
		return type.charAt(13) == 'C' ? type.substring(13, 23) : type.substring(13, 22);
	}

	public void sendConnect() throws Exception {
		objectOutputStream.writeObject(new Packet("Connect", clientType.toString(), "has connected."));
		objectOutputStream.flush();
	}

	public void sendDisconnect() throws Exception {
		objectOutputStream.writeObject(new Packet("Disconnect", clientType.toString(), "has disconnected."));
		objectOutputStream.flush();

//		objectOutputStream.close();
//		objectInputStream.close();
//		serverSocket.close();
	}

	public void sendPosition(Vector3f position) throws Exception {
		objectOutputStream.writeObject(new Packet("Position", clientType.toString(),
				position.getX() + ":" + position.getY() + ":" + position.getY()));
		objectOutputStream.flush();
	}
}

/**
 * Handler incoming any inputs echoing from Server.
 * 
 * @author Issaree Srisomboon
 *
 */
class IncomingInput implements Runnable {
	private ObjectInputStream objectInputStream;
	private ClientType client;

	public IncomingInput(ObjectInputStream objectInputStream, ClientType client) {
		this.objectInputStream = objectInputStream;
		this.client = client;
	}

	@Override
	public void run() {
		Packet object;
		try {
			while ((object = (Packet) objectInputStream.readObject()) != null) {

				switch (object.getSendType()) {
				case "Connect":
				case "Disconnect":
					client.printConnection(object.getClientType() + " " + object.getMessage());
					break;
				case "Position":
					client.updatePosition(object);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
