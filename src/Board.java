/*
 * Author: Aditya Kumar 
 * Course: CSC 335
 * File: Board.java
 * 
 * This class handles the board of the game, which is the 
 * the array of the cards or game tiles that appear after 
 * the the user clicks on the the play game button. 
 * This is mostly a backend class, and doesnt have any code that 
 * directly affects the GUI
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.widgets.Display;

public class Board {

	private Display dis;
	// List of image links for the project
	private String[] animals = { "cow.jpg", "cow.jpg", "chicken.jpg", "dog.jpg", "duck.jpg", "duck.jpg", "chicken.jpg",
			"duck.jpg", "duck.jpg", "chicken.jpg", "dog.jpg", "chicken.jpg" };
	private String[] fruits = { "peach.jpg", "avocado.jpg", "peach.jpg", "pear.jpg", "peach.jpg", "avocado.jpg",
			"peach.jpg", "pineapple.jpg", "pear.jpg", "greenapple.jpg", "greenapple.jpg", "pineapple.jpg" };
	private String[] chosenArray = fruits;
	private List<Card> cardDeck = new ArrayList<>();

	// Constructor
	public Board(String optionChosen) {
		if (optionChosen.equals("Animals"))
			chosenArray = animals;
		this.createCards();
	}

	/*
	 * Most important function of this class that is used to make the card Deck,
	 * which is the collection of the instances of the Card class that serve as the
	 * game tiles on the main game
	 * 
	 */

	private void createCards() {
		// initial co-ordinated
		int index = 0;
		int orig_x = 30;
		int orig_y = 17 - 120;
		int x = orig_x;
		int y = orig_y;

		// Code to shuffle the contents of the array
		Random rand = new Random();

		for (int i = 0; i < chosenArray.length; i++) {
			int randomIndexToSwap = rand.nextInt(chosenArray.length);
			String temp = chosenArray[randomIndexToSwap];
			chosenArray[randomIndexToSwap] = chosenArray[i];
			chosenArray[i] = temp;
		}

		for (String name : chosenArray) {
			if (index % 4 == 0) {
				x = orig_x;
				y += 120;
			} else
				x += 120;
			cardDeck.add(new Card(dis, name, x, y));
			index++;
		}
	}

	public Card getCard(int i) {
		return cardDeck.get(i);
	}

	public int getDeckSize() {
		return cardDeck.size();
	}

	public void removeAll() {
		cardDeck.clear();
	}
	
	public String [] getChosenArray() {
		//for debug purposes only
		return chosenArray; 
	}
}
