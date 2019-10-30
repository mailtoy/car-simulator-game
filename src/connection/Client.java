package connection;

import java.io.*;
import java.net.*;

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
		return type.charAt(5) == 'C' ? type.substring(13, 24) : type.substring(13, 23);
	}

	public void sendConnect() throws Exception {
		objectOutputStream.writeObject(new TestObject("Connect", typeSpecify(), "has connected."));
		objectOutputStream.flush();
	}
}

class IncomingInput implements Runnable {
	private ObjectInputStream objectInputStream;
	private ClientType client;

	public IncomingInput(ObjectInputStream objectInputStream, ClientType client) {
		this.objectInputStream = objectInputStream;
		this.client = client;
	}

	@Override
	public void run() {
		TestObject object;
		try {
			while ((object = (TestObject) objectInputStream.readObject()) != null) {
				switch (object.getSendType()) {
				case "Connect":
					client.write(object.getClientType() + " " + object.getMessage());
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
