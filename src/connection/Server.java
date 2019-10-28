package connection;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Server acts as the central clearing house of all interactions.
 * Server will start listening on the default port as 3001, waiting for a connection.
 * 
 * @author boranorben
 *
 */
public class Server implements Runnable {
	private final int remoteServerPort = 3001;
	private ServerSocket serverSocket;
	private ArrayList<PrintWriter> clients;
	
	public Server() {
		clients = new ArrayList<PrintWriter>();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(remoteServerPort);
			System.out.println("Waiting for connections...");
			
			while (true) {
				Socket clientSocket = serverSocket.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clients.add(writer);
//				System.out.println("Connect with client: " + writer);
				
				Thread listener = new Thread(new ClientHandler(this, clientSocket, writer));
				listener.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized ArrayList<PrintWriter> getClients() {
		return this.clients;
	}
	
	public static void main(String[] args) {
		Thread thread = new Thread(new Server());
		thread.start();
	}

}

/**
 * ClientHandler handles each one of clients connecting to the server.
 * 
 * @author boranorben
 *
 */
class ClientHandler implements Runnable {
	private Server server;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public ClientHandler(Server server, Socket clientSocket, PrintWriter writer) {
		this.server = server;
		this.writer = writer;
		try {
			this.socket = clientSocket;
			InputStreamReader isReader = new InputStreamReader(this.socket.getInputStream());
			this.reader = new BufferedReader(isReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String stream = null, messageType = null, client = null, message = null;
		String[] messageArray = null;
		
		try {
			// modify here
			while ((stream = reader.readLine()) != null) {
				messageArray = stream.split(":");
				
				messageType = messageArray[0];
				client = messageArray[1];
				message = messageArray[2];
				System.out.printf("Received: [%s] %s %s \n", messageType, client, message);	
				
				broadcast(stream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void broadcast(String message) {
		Iterator<PrintWriter> iterator = server.getClients().iterator();
		
		// modify logic send-back here
		while (iterator.hasNext()) {
			try {
				writer = (PrintWriter) iterator.next();
				writer.println(message);
				writer.flush();
				
				System.out.println("Sending: " + message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
