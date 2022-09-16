/*
 * Author: Aditya Kumar
 * 
 * Test File
 */

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class ConcentrationGameTestClass {

	@Test
	public void checkGetDeckSizeBoard_Test1() {
		Board board = new Board("Animals");
		assertEquals(board.getDeckSize(), 12);
	}

	/*
	 * Checks whether the array is chosen according to the 
	 * user input or not.
	 */
	@Test
	public void checkChosenArrayBoard_Test2() {
		Board board = new Board("Animals");
		String[] actual = board.getChosenArray();
		Arrays.sort(actual);
		String[] expected = new String[] { "cow.jpg", "cow.jpg", "chicken.jpg", "dog.jpg", "duck.jpg", "duck.jpg",
				"chicken.jpg", "duck.jpg", "duck.jpg", "chicken.jpg", "dog.jpg", "chicken.jpg" };
		Arrays.sort(expected);
		assertEquals(actual, expected);
	}

	/*
	 * confirms that the default state of a random card is not flipped at the
	 * beginning of the game
	 */
	@Test
	public void checkIfFlippedAtBeginning_Test3() {
		Board board = new Board("Animals");
		assertEquals(board.getCard(0).isFlipped(), false);
	}

}
