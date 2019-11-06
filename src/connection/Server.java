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
	private boolean running;

	public Server() {
		clients = new ArrayList<ObjectOutputStream>();
		System.out.println("Waiting for connections...");

		try {
			serverSocket = new ServerSocket(remoteServerPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		running = true;

		while (running) {
			try {
				Socket clientSocket = serverSocket.accept();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
				clients.add(objectOutputStream);

				Thread listener = new Thread(new ClientHandler(this, clientSocket, objectOutputStream));
				listener.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		shutdown();
	}

	public synchronized ArrayList<ObjectOutputStream> getClients() {
		return this.clients;
	}

	public synchronized void removeClient(ObjectOutputStream ObjectOutputStream) {
		this.clients.remove(ObjectOutputStream);
	}

	public void shutdown() {
		running = false;

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			this.objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Packet object;
		try {
			while ((object = (Packet) objectInputStream.readObject()) != null) {
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

	private void broadcast(Packet object) {
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
