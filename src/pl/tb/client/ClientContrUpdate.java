package pl.tb.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Label;

public class ClientContrUpdate extends Task<Integer>{
	Socket socket;
	BufferedReader bufferedReader;
	Label lblMessage;
	static final int MAX_POINTS = 100;
	
	public ClientContrUpdate(Socket socket) throws IOException {
		this.socket = socket;
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	@Override
	protected Integer call() throws IOException {
		String message;
		String tokens[];
		while (true) {
			if ((message = bufferedReader.readLine()) != null) {
				tokens = message.split(";");
				updateProgress(Integer.parseInt(tokens[2]), MAX_POINTS);
				updateMessage(message);
						
			}
		}
		
	}
}
