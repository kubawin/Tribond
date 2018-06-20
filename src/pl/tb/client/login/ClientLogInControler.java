package pl.tb.client.login;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pl.tb.server.ServerConn;

public class ClientLogInControler {
	@FXML
	TextField txtLogIn;
	@FXML
	TextField txtPort;
	@FXML
	TextField txtHost;
	@FXML
	Button btnLogIn;
	@FXML
	Button tbnRunServer;
	@FXML
	ImageView ivImage;
	@FXML
	Label label;
	@FXML
	Label lblServer;
	@FXML
	Label lblPortHost;

	
	private int port;
	private Image imgBackground;
	private InputStream inputImage;
	ServerConn serverConn;
	boolean thisIsServer = false;
	
	public ClientLogInControler() throws IOException {
		inputImage = ClientLogInControler.class.getClassLoader().getResourceAsStream("resources/background.jpg");
		imgBackground = new Image(inputImage);

	}
	
	@FXML
	public void initialize() {
			ivImage.setImage(imgBackground);
			tbnRunServer.setText("Run server");
			txtPort.setText("8989");
			label.setVisible(false);
			lblServer.setVisible(false);
			lblPortHost.setVisible(false);
		
			
	}
	
	
	public String getLogin() {
		return txtLogIn.getText();
	}
	
	public String getHost() {
		return txtHost.getText();
	}
	
	public int getPort() {
		return Integer.parseInt(txtPort.getText());
	}
	
	@FXML
	public void login() throws Throwable {
		Stage stage = (Stage) btnLogIn.getScene().getWindow();
		
		if (txtLogIn.getText().equals("") || txtLogIn.getText().equals(null)) {
			label.setText("Please type your nick name");
			label.setVisible(true);
		} else if (txtHost.getText().equals("") || txtHost.getText().equals(null)) {
			label.setVisible(false);
			lblPortHost.setText("Wprowadz nazwê hosta");
			lblPortHost.setVisible(true);
		} else if (txtPort.getText().equals("") || txtPort.getText().equals(null)) {
			label.setVisible(false);
			lblPortHost.setText("Podaj numer portu");
			lblPortHost.setVisible(true);
		} else {
	
			if (thisIsServer == true) {
				stage.hide();
			} else {
				System.out.println("exiting");
				stage.close();
			}
		}
		
	}
	
	@FXML
	public void runServer() throws Throwable {
		
		
		if (txtPort.getText().equals("") || txtPort.getText().equals(null)) {
			lblPortHost.setText("Podaj numer portu");
			lblPortHost.setVisible(true);
		} else if (tbnRunServer.getText().equals("Run server")) {
			port = Integer.parseInt(txtPort.getText());
			
			//Running server
			serverConn = new ServerConn(port);
			serverConn.start();
			
			//Putting information about the server into the text fields, also turning off editing
			txtPort.setText(Integer.toString(port));
			txtHost.setText(InetAddress.getLocalHost().getHostName());
			
			txtPort.setEditable(false);
			txtHost.setEditable(false);
			
			thisIsServer = true;
			
			lblPortHost.setVisible(false);
			
			lblServer.setText("Server is running");
			lblServer.setVisible(true);
			tbnRunServer.setDisable(true);
			
		}
		
		
		
	}

}
