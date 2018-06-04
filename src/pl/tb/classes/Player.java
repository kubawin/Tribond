package pl.tb.classes;

public class Player implements IPlayer{
	
	private String firstPlayer = "Player1";
	private String secondPlayer = "Player2";

	@Override
	public void setFirstPlayer(String playerName) {
		this.firstPlayer = playerName;
		
	}

	@Override
	public void setSecondPlayer(String playerName) {
		this.secondPlayer = playerName;
		
	}

	@Override
	public String playerByTheTable() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getFirstPlayer() {
		return firstPlayer;
	}
	
	public String getSecondPlayer() {
		return secondPlayer;
	}

}
