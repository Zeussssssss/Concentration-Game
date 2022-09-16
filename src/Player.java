/*
 * Author: Aditya Kumar 
 * Course: CSC 335
 * File: Player.java
 * 
 * This is simple class the is used to model 
 * a player. It has only two states associated with it,
 * and the functions in the this class are mainly 
 * getters and setters
 */
public class Player {
	int score;
	int name;

	public Player(int identifier) {
		name = identifier;
		score = 0;
	}

	public int getScore() {
		return score;
	}

	public void incrementScore() {
		score++;
	}

	public String getName() {
		return String.valueOf(name + 1);
	}

}
