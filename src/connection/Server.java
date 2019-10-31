package connection;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Server acts as the central clearing house of all interactions. Server will
 * start listening on the default port as 3001, waiting for a connection.
 * 
 * @author Issaree Srisomboon
 *
 */
public class Server implements Runnable {
	private final int remoteServerPort = 3001;
	private ServerSocket serverSocket;
	private ArrayList<ObjectOutputStream> clients;

	public Server() {
		clients = new ArrayList<ObjectOutputStream>();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(remoteServerPort);
			System.out.println("Waiting for connections...");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
				clients.add(objectOutputStream);

				Thread listener = new Thread(new ClientHandler(this, clientSocket, objectOutputStream));
				listener.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized ArrayList<ObjectOutputStream> getClients() {
		return this.clients;
	}

	public synchronized void removeClient(ObjectOutputStream ObjectOutputStream) {
		this.clients.remove(ObjectOutputStream);
	}

	public static void main(String[] args) {
		Thread thread = new Thread(new Server());
		thread.start();
	}

}

/**
 * ClientHandler handles each one of clients connecting to the server.
 * 
 * @author Issaree Srisomboon
 *
 */
class ClientHandler implements Runnable {
	private Server server;
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;

	public ClientHandler(Server server, Socket clientSocket, ObjectOutputStream objectOutputStream) {
		this.server = server;
		this.objectOutputStream = objectOutputStream;
		try {
			this.socket = clientSocket;
			InputStream inputStream = this.socket.getInputStream();
			this.objectInputStream = new ObjectInputStream(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		TestObject object;
		try {
			while ((object = (TestObject) objectInputStream.readObject()) != null) {
				System.out.printf("Received: [%s] %s %s\n", object.getSendType(), object.getClientType(),
						object.getMessage());

				if (object.getSendType().equals("Disconnect")) {
					server.removeClient(objectOutputStream);
				}
				broadcast(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void broadcast(TestObject object) {
		Iterator<ObjectOutputStream> iterator = server.getClients().iterator();
		while (iterator.hasNext()) {
			try {
				objectOutputStream = iterator.next();
				objectOutputStream.writeObject(object);
				objectOutputStream.flush();

				System.out.println("Sent: " + object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
