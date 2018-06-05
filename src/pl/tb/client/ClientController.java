package pl.tb.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.tb.classes.Questions;
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
	
	private int diceResult, thisPlayerNumber, playerByTheTable;
	private String[] questionSet;
	private String thisNick;
	private Questions questions;
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private ArrayList<String> players;
	
	private ClientContrUpdate controllersUpdater;

	
	
	@FXML
	public void initialize() throws UnknownHostException, IOException {
		//Initializig catgory images
//		Image artIcon = new Image(getClass().getResourceAsStream("/art.png"));
//		Image sportIcon = new Image(getClass().getResourceAsStream("/sport.png"));
//		Image scienceIcon = new Image(getClass().getResourceAsStream("/science.png"));
//		Image stuffIcon = new Image(getClass().getResourceAsStream("/stuff.png"));
//		
//		lblImgSport.setGraphic(new ImageView(sportIcon));
//		lblImgArt.setGraphic(new ImageView(artIcon));
//		lblImgScience.setGraphic(new ImageView(scienceIcon));
//		lblImgMisc.setGraphic(new ImageView(stuffIcon));
		
		playerLogIn(); //If the player input correct nick, will be connected to the server.
		connectClientToServer();  //Establishes connection to the server.
	}

	@FXML
	private void playerLogIn() {
		//It opens a new dialog window where a new player has to input its nick
		//After it's done a method connectClientToServer() will be invoked.
		TextInputDialog inputDialog = new TextInputDialog();
		inputDialog.setContentText("Nick");
		inputDialog.setHeaderText("Podaj nick");
		inputDialog.getDialogPane().setPrefWidth(300);
		inputDialog.showAndWait();
		thisNick = inputDialog.getResult();
		
		if (thisNick.equals("") || thisNick.isEmpty() || thisNick.equals(null)) {
			System.out.println("Nie podano ¿adnego nicka.");
			System.exit(0);
		}
		
	}
	
	@FXML
	public void rollDice() {
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
		String opponent = lblPlayerByTheTable.getText();
		Boolean approval = true;
		printWriter.println("APPROVAL;"+opponent+";"+approval+";"+this.thisNick);
	}
	
	@FXML
	public void disapprovalForAnswer() {
		String opponent = lblPlayerByTheTable.getText();
		Boolean approval = false;
		printWriter.println("APPROVAL;"+opponent+";"+approval+";"+this.thisNick);
	}
	
	public void connectClientToServer() throws UnknownHostException, IOException {
		/**Establishes connection to the server. 
		 * Also invokes another method, that binds controllers with a method from class ClientContrUpdate,
		 * which runs a new thread. 
		 */
		
		String myHost = InetAddress.getLocalHost().getHostName();
		socket = new Socket(myHost, 8989);
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
					progbPlayer1.setProgress((double) (Double.parseDouble(tokens[2]) / controllersUpdater.MAX_POINTS));
					lblPlayer1Points.setText(tokens[2]);
					
					//Enable/disable buttons TRUE and FALSE depending on who answering and who judges
					if (this.thisNick.equals(tokens[8])) {
						btnTrue.setDisable(true);
						btnFalse.setDisable(true);
					}
					else {
						btnTrue.setDisable(false);
						btnFalse.setDisable(false);
					}
					
				}
				else {
					lblPlayer2.setText(tokens[1]);
					lblAnswer.setText(tokens[4]);
					progbPlayer2.setProgress((double) (Double.parseDouble(tokens[2]) / controllersUpdater.MAX_POINTS));
					lblPlayer2Points.setText(tokens[2]);
					
					//Enable/disable buttons TRUE and FALSE depending on who answering and who judges
					if (this.thisNick.equals(tokens[8])) {
						btnTrue.setDisable(true);
						btnFalse.setDisable(true);
					}
					else {
						btnTrue.setDisable(false);
						btnFalse.setDisable(false);
					}
				}
			
				//Common controllers
				lblQuestion.setText(tokens[3]);
				lblPlayerByTheTable.setText(tokens[8]);
				lblDiceCategory.setText("Dice: " + tokens[5] ); //+ "\nKategoria: " + tokens[4]);
				

			
			} else if (tokens[7].equals("CHAT")) {
				txtChatMonitor.appendText(tokens[1] + ": " + tokens[6] + "\n");
			}
		});
	}
}
