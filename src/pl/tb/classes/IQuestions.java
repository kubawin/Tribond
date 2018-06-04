package pl.tb.classes;

public interface IQuestions {
	public void loadQuestions();
	public String[] getQuestionSet(int questionIndex);
	public int getAmountOfQuestions();

}
