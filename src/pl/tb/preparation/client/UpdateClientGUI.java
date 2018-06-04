package pl.tb.preparation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Label;

public class UpdateClientGUI extends Task<Integer>{
	Socket socket;
	BufferedReader bufferedReader;
	Label lblMessage;
	
	public UpdateClientGUI(Socket socket) throws IOException {
		this.socket = socket;
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	@Override
	protected Integer call() throws Exception {
		String msg = null;
		try {
			
		while(true) {	
		
		if ((msg = bufferedReader.readLine()) != null)
			updateMessage(bufferedReader.readLine());
		else 
			updateMessage("Brak nowych wiadomoœci.");
		
		} 
		
		
	} catch (IOException e) {e.printStackTrace();}
		return null;
	}

}
