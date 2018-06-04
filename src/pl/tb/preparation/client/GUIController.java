package pl.tb.preparation.client;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GUIController {
	@FXML
	Button btnConnect;
	@FXML
	Button btnSend;
	@FXML
	TextField txtLogin;
	@FXML
	TextField txtMessage;
	@FXML
	Label lblMessage;
	@FXML
	Button btnRefresh;
	
	BufferedReader bufferedReaderFromServer;
	PrintWriter printWriter;
	BufferedReader bufferedReader;
	Socket socket;
	String name;
	InetAddress myHost;
	
	@FXML
	public void connectClient() throws UnknownHostException, IOException {
		name = txtLogin.getText();
		myHost = InetAddress.getLocalHost();
		System.out.println(myHost);
		socket = new Socket(myHost, 8989);
		lblMessage.setText("");
		
		bufferedReaderFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printWriter = new PrintWriter(socket.getOutputStream(), true);
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		UpdateClientGUI update = new UpdateClientGUI(socket);
		lblMessage.textProperty().bind(update.messageProperty());
		
		new Thread(update).start();
		
		
	}
	
	@FXML
	public void sendMessage() throws IOException {
		String readerInput = txtMessage.getText();
		printWriter.println(name + ": " + readerInput);
		//lblMessage.setText(bufferedReaderFromServer.readLine() + "\n");	

	}
	
	@FXML
	public Integer updateGUI() {
//		String msg = null;
//		try {
//		while ((msg = bufferedReaderFromServer.readLine()) != null)
//			lblMessage.setText(bufferedReaderFromServer.readLine() + "\n");
//	} catch (IOException e) {e.printStackTrace();}
		
		String msg = null;
		try {
		if ((msg = bufferedReaderFromServer.readLine()) != null)
			lblMessage.setText(bufferedReaderFromServer.readLine());
		else 
			lblMessage.setText("Brak nowych wiadomoœci.");
	} catch (IOException e) {e.printStackTrace();}
		return 0;
	}
	
	

	}
	
	


