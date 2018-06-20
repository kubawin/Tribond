package pl.tb.classes;

import java.io.IOException;

public interface IQuestions {
	public void loadQuestions() throws IOException;
	public String[] getQuestionSet(int questionIndex);
	public int getAmountOfQuestions();

}
