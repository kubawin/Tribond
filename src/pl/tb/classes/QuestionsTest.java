package pl.tb.classes;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QuestionsTest {
	
//	private File questionFile;
	private LinkedList<String> questions = new LinkedList<>();
	private int amountOfQuestions = 0;

	Scanner in;
	File questionFile = new File("QUESTIONS2.txt");
	
	@Before
	public void setUp() throws Exception {
		
		try {
			in = new Scanner(new FileInputStream(questionFile.getPath()));
						
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

	@After
	public void tearDown() throws Exception {
		in.close();
	}



	@Test
	public void testLoadQuestions() {
		assertTrue(amountOfQuestions > 0);
		assertTrue(questionFile != null);
	}

	@Test
	public void testGetQuestionSet() {
		int questionNumber = 1;
		int tmp = 0;
		
		questions.remove(questionNumber);
		tmp = amountOfQuestions;
		amountOfQuestions--;
		
		assertTrue(amountOfQuestions + 1 == tmp);

	}


}
