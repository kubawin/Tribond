package pl.tb.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Questions implements IQuestions {
	
	private File questionFile;
	private LinkedList<String> questions = new LinkedList<>();
	private int amountOfQuestions = 0;
	private InputStream is;
	
	
	public Questions() {
		//Listing files with questions.
		//Each file contains questions of a different category.
		
		//questionFile = new File("resources/QUESTIONS2.txt");
		//this.loadQuestions();
				
	}
	
	public void loadQuestions() throws IOException {
			
		try {
			//Scanner in = new Scanner(new FileInputStream(questionFile.getPath()));
			is = Questions.class.getClassLoader().getResourceAsStream("resources/QUESTIONS2.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null) {
				questions.push(line);
				amountOfQuestions++;
				
			}
						
//			while (in.hasNext()) {
//				questions.push(in.nextLine());	
//				amountOfQuestions++;
//			}
		}
		
		catch (FileNotFoundException e) {
			System.out.println("B³¹d! Nie znaleziono pliku");
			return;
		}
		
	}
	
	public String[] getQuestionSet(int questionNumber) {
		String[] questionSet = questions.get(questionNumber).split(";");
		questions.remove(questionNumber);
		amountOfQuestions--;
		return questionSet;
	}
	
	public int getAmountOfQuestions() {
		return amountOfQuestions;
	}



}
