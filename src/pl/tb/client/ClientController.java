package pl.tb.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pl.tb.classes.Questions;
import pl.tb.client.login.ClientLogInControler;
import pl.tb.preparation.client.UpdateClientGUI;

public class ClientController {
	@FXML
	Button btnRollDice;
	@FXML
	Button btnTrue;
	@FXML
	Button btnFalse;
	@FXML
	Button btnSendMsg;
	@FXML
	Label lblDiceCategory;
	@FXML
	Label lblQuestion;
	@FXML
	Label lblAnswer;
	@FXML
	Label lblPlayer;
	@FXML
	Label lblPlayer1;
	@FXML
	Label lblPlayer2;
	@FXML
	Label lblPlayer1Points;
	@FXML
	Label lblPlayer2Points;
	@FXML
	Label lblPlayerByTheTable;
	@FXML
	Label lblPlayerByTheTable_comment; //Does nothing.
	@FXML
	Label lblImgArt;
	@FXML
	Label lblImgSport;
	@FXML
	Label lblImgScience;
	@FXML
	Label lblImgMisc;
	@FXML
	ProgressBar progbPlayer1;
	@FXML
	ProgressBar progbPlayer2;
	@FXML
	TextArea txtChatMessage;
	@FXML
	TextArea txtChatMonitor;
	@FXML
	ImageView iv;
	
	private int diceResult, thisPlayerNumber, playerByTheTable;
	private String[] questionSet;
	private String thisNick;
	private Questions questions;
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private ArrayList<String> players;
	private Image imgBackground;
	private InputStream inputImage;
	private int port;
	private String host;
	
	private ClientContrUpdate controllersUpdater;
	
	public ClientController() throws FileNotFoundException {
		inputImage = ClientController.class.getClassLoader().getResourceAsStream("resources/background.jpg");
		imgBackground = new Image(inputImage);
	}
	
	@FXML
	public void initialize() throws UnknownHostException, IOException {
		iv.setImage(imgBackground);
		iv.setPreserveRatio(true);
		iv.fitHeightProperty();
		iv.setFitHeight(700);
		lblDiceCategory.setText("");
		lblPlayer2.setText("awaiting");
		lblPlayerByTheTable.setText("");
		
		
		txtChatMonitor.setEditable(false);
		
		playerLogIn(); //If the player input correct nick, will be connected to the server.
		connectClientToServer();  //Establishes connection to the server.
		
	}

	@FXML
	private void playerLogIn() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("login/ClientLogInWindow.fxml"));
			Parent root = (Parent) loader.load();
			root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
			ClientLogInControler cli = loader.getController();
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.showAndWait();
			stage.toBack();
			
			thisNick = cli.getLogin();
			port = cli.getPort();
			host = cli.getHost();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		if (thisNick.equals("") || thisNick.isEmpty() || thisNick.equals(null)) {
			System.out.println("Nie podano ¿adnego nicka.");
			System.exit(0);
		}
		
	}
	
	@FXML
	public void rollDice() {
		if (lblPlayerByTheTable.getText().equals(thisNick) || lblPlayerByTheTable.getText().equals("Roll a dice"))
			if (!lblPlayer2.getText().equals("awaiting"))
				if (lblDiceCategory.getText().equals("Dice: 0"))
					printWriter.println("QUESTION;"+this.thisNick);
	}
	
	@FXML
	public void sendChatMessage() {
		if (!txtChatMessage.getText().equals(""))
			printWriter.println("CHAT;"+txtChatMessage.getText()+";"+this.thisNick);
		
		txtChatMessage.clear();
	}
	
	@FXML
	public void approvalForAnswer() {
		if (!lblDiceCategory.getText().equals("Dice: 0")) {
			String opponent = lblPlayerByTheTable.getText();
			Boolean approval = true;
			printWriter.println("APPROVAL;"+opponent+";"+approval+";"+this.thisNick);
		}
	}
	
	@FXML
	public void disapprovalForAnswer() {
		if (!lblDiceCategory.getText().equals("Dice: 0")) {
			String opponent = lblPlayerByTheTable.getText();
			Boolean approval = false;
			printWriter.println("APPROVAL;"+opponent+";"+approval+";"+this.thisNick);
		}
	}
	
	public void connectClientToServer() throws UnknownHostException, IOException {
		/**Establishes connection to the server. 
		 * Also invokes another method, that binds controllers with a method from class ClientContrUpdate,
		 * which runs a new thread. 
		 */
		
		socket = new Socket(host, port);
		controllersUpdater = new ClientContrUpdate(socket);
		
		//Binding controllers
		bindControllersToThread();
		new Thread(controllersUpdater).start();

		printWriter = new PrintWriter(socket.getOutputStream(), true);
		printWriter.println("LOGIN;"+thisNick);
	}
	
	private void bindControllersToThread() throws IOException {
		/**This method is responsible for updating controllers on each GUI.
		 * For object controllersUpdater a new ChangeListeres is being set up.
		 * Thanks to this listener we are able to separate messages for a particular clients.
		 */
		controllersUpdater.messageProperty().addListener((obs, oldMsg, newMsg) -> {
			String tokens[] = newMsg.split(";"); //It splits message from the server into parts. The separator is ";"
			System.out.println("OD BINDA: " + newMsg);
			
			if (tokens[7].equals("CLIENT")) { //Check if the message is for updating CLIENTs details or just CHAT.
				if (tokens[1].equals(this.thisNick)) { //This checks if the information should update this client or the another one
					lblPlayer1.setText(tokens[1]);
					lblPlayer.setText("Hi, " + lblPlayer1.getText() + "!");
					lblAnswer.setText("Answer: " + tokens[4]);
					progbPlayer1.setProgress((double) (Double.parseDouble(tokens[2]) / controllersUpdater.MAX_POINTS));
					lblPlayer1Points.setText(tokens[2]);
					
					//Enable/disable buttons TRUE and FALSE depending on who answering and who judges
					if (this.thisNick.equals(tokens[8])) {
						btnTrue.setDisable(true);
						btnFalse.setDisable(true);
						lblAnswer.setVisible(false);
					}
					else {
						btnTrue.setDisable(false);
						btnFalse.setDisable(false);
						lblAnswer.setVisible(true);
					}
					
				}
				else {
					
					lblPlayer2.setText(tokens[1]);
					lblAnswer.setText("Answer: " + tokens[4]);
					progbPlayer2.setProgress((double) (Double.parseDouble(tokens[2]) / controllersUpdater.MAX_POINTS));
					lblPlayer2Points.setText(tokens[2]);
					
					//Enable/disable buttons TRUE and FALSE depending on who answering and who judges
					if (this.thisNick.equals(tokens[8])) {
						btnTrue.setDisable(true);
						btnFalse.setDisable(true);
						lblAnswer.setVisible(false);
					}
					else {
						btnTrue.setDisable(false);
						btnFalse.setDisable(false);
						lblAnswer.setVisible(true);

					}
				}
			
				//Common controllers
				lblQuestion.setText("What those three things have in common?\n" + tokens[3]);
				lblPlayerByTheTable.setText(tokens[8]);
				lblDiceCategory.setText("Dice: " + tokens[5] ); //+ "\nKategoria: " + tokens[4]);
				

			
			} else if (tokens[7].equals("CHAT")) {
				txtChatMonitor.appendText(tokens[1] + ": " + tokens[6] + "\n");
			}
		});
	}
}
