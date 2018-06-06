package pl.tb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import pl.tb.classes.Questions;

public class ServerClient extends Thread {
	Socket socket;
	ServerConn serverConn;
	PrintWriter printWriter;
	String message = null;
	String[] actionToken;
	HashMap<String, Integer> playerPointsTable;
	int numberOfClients = 1;
	
	Questions questions;
	String[] questionSet;
	private int dice = 0;
	
	public ServerClient(ServerConn serverConn, Socket socket) {
		this.socket = socket;
		this.serverConn = serverConn;
		this.questions = new Questions();
		playerPointsTable = new HashMap<>();

	}
	
	public void run() {
		try {
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			ArrayList<ServerClient> clientList = serverConn.getClientList();
			
			//Player detail
			int clientNumber = clientList.size();
			String playerNick = " ";
			String question = " ";
			String answer = " ";
			String token = " ";
			String chatMessage = " ";
			String playerByTheTable = " ";
			int points = 0;
			

			
			while ((message = bufferReader.readLine()) != null) {
				System.out.println("Message from client: " + message);
				
				actionToken = message.split(";");
				
				if (actionToken[0].equals("LOGIN")) { //Check players
					playerNick = actionToken[1];
					playerPointsTable.put(playerNick, 0);
					token = "CLIENT";
					playerByTheTable = "Roll a dice";					
				} else if (actionToken[0].equals("QUESTION")) {
					token = "CLIENT";
					playerByTheTable = actionToken[1];
					
					dice = (int)(Math.random()*5 + 1);
					
					if (questions.getAmountOfQuestions() ==0) questions.loadQuestions(); //Reloading questions
					int questionNumber = (int) (Math.random() * questions.getAmountOfQuestions());
					questionSet = questions.getQuestionSet(questionNumber);
					answer = questionSet[3];
					question = questionSet[0] + "," + questionSet[1] + "," + questionSet[2] ;
				} else if (actionToken[0].equals("CHAT")) {
					token = "CHAT";
					question = actionToken[1]; 
					chatMessage = actionToken[1];
					playerNick = actionToken[2];
				} else if (actionToken[0].equals("APPROVAL")) {
					token = "CLIENT";
					String opponent = actionToken[1];
					Boolean approved = Boolean.parseBoolean(actionToken[2]);
					question = "Roll a dice and answer question.";
					clientNumber = 1;
					if (approved)
						 playerByTheTable = actionToken[1];
					else
						playerByTheTable = actionToken[3]; 
					
					
					//For each client we check its points and if the answer was correct we add 4 point otherwise points remains the same
					//In this class we have HashMap object playerPointsTable which can be returned in method getPlayerPoints.
					//The below for loop works on a ServerClient object, so thank to that we can check each client points by invokin getPlayerPoints
					for (ServerClient sc : clientList) {
						if (sc.getPlayerPoints().get(opponent) != null) {
							if (approved == true) {
								sc.getPlayerPoints().compute(opponent, (k,v) -> v += sc.dice);
								points = sc.getPlayerPoints().get(opponent);
								playerNick = opponent;
								break;
							}
							else {
								playerNick = opponent;
								points = sc.getPlayerPoints().get(opponent);
								++clientNumber;
								break;
							}
						}
//						else 
								
					}
					
				}
				//printWriter.println("SERVER response: " + message);
				
				//Sending messages to other players
				message = clientNumber+";"+playerNick+";"+points+";"+question+";"+answer+";"+dice+";"+chatMessage+";"
							+token+";"+playerByTheTable;
				
				System.out.println("MESSAGE FROM SERVER: " + message);
				
				//This part of the code send the message to all connected clients
				clientList = serverConn.getClientList();
				for (ServerClient sc : clientList) {
						sc.sendMsg(message);
				}
				
			}
			
			socket.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	private void sendMsg(String msg) throws IOException {
		//To jeszcze rozkminiæ
		//for (int i = 1; i <=2; i++) {
			printWriter.println(msg);
		//}
	}
	
	public HashMap<String, Integer> getPlayerPoints() {
		
		return playerPointsTable;
	} 
	
	
	public String getMessage() {
		return message;
	}
	

}
