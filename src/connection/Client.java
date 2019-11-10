package connection;

import java.io.*;
import java.net.*;

/**
 * Client creates a TCP socket to Server.
 * 
 * @author Issaree Srisomboon
 *
 */
public class Client {
	private final int remoteServerPort = 3001;

	private String serverIP = "192.168.0.232";


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
		objectOutputStream.writeObject(new ParseObject("Connect", typeSpecify(), "has connected."));
		objectOutputStream.flush();
	}

	public void sendDisconnect() throws Exception {
		objectOutputStream.writeObject(new ParseObject("Disconnect", typeSpecify(), "has disconnected."));
		objectOutputStream.flush();

//		objectOutputStream.close();
//		objectInputStream.close();
//		serverSocket.close();
	}

	public void sendKeyInput(int keyInput, float speed) throws Exception {
		objectOutputStream.writeObject(new ParseObject("KeyInput", typeSpecify(), keyInput + ":" + speed));
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
		ParseObject object;
		try {
			while ((object = (ParseObject) objectInputStream.readObject()) != null) {

				switch (object.getSendType()) {
				case "Connect":
				case "Disconnect":
					client.printConnection(object.getClientType() + " " + object.getMessage());
					break;
				case "KeyInput":
					client.move(object.getMessage());
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
