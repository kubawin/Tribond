package pl.tb.client;

import java.util.ArrayList;

import pl.tb.server.ServerClient;
import pl.tb.server.ServerConn;

public class test {
	public static void main(String[] args) {
		
		ServerConn sc = new ServerConn(8989);
		int i=1;
		ArrayList<ServerClient> clients = sc.getClientList();
		
		for (ServerClient a : clients) 
			System.out.println(a.getMessage() + i++);
		
		System.out.println("ju¿");
	}

}
