package pl.tb.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConn extends Thread {
	int serverPort;
	private ArrayList<ServerClient> clientList = new ArrayList<>();

	public ServerConn(int serverPort) {
		this.serverPort = serverPort;
	}
	
	public ArrayList<ServerClient> getClientList() {
		return clientList;
	}
	
	public void run() {
		try {
			new ServerConn(serverPort).runServer();
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	private void runServer() throws IOException {
		ServerSocket serverSocket = new ServerSocket(serverPort);
		while (true) {
			System.out.println("Server connected. Awaiting for clients");
			Socket socket = serverSocket.accept();
			System.out.println("Client connected");
			ServerClient client = new ServerClient(this, socket);
			clientList.add(client);
			client.start();
		}
	
	
}
	
	public void stopServer() throws Throwable {
		System.exit(0);
		
}


	
	
	

}
