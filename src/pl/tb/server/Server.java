package pl.tb.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static final int PORT = 8989;
	
	
	public static void main(String[] args) throws IOException {
		ServerConn serverConn = new ServerConn(PORT);
		serverConn.start();
	
	
	}

}
