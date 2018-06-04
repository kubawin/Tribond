package pl.tb.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	
	
	public Questions() {
		//Listing files with questions.
		//Each file contains questions of a different category.
		questionFile = new File("QUESTIONS2.txt");
		this.loadQuestions();
				
	}
	
	public void loadQuestions() {
			
		try {
			Scanner in = new Scanner(new FileInputStream(questionFile.getPath()));
						
			while (in.hasNext()) {
				questions.push(in.nextLine());	
				amountOfQuestions++;
			}
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
