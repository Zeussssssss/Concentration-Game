
/*
 * Author: Aditya Kumar
 * Course: CSC 335
 * File: Card.java
 * 
 * This is the class that represents a
 * card or a game tile in this game. It is 
 * directly displaying anything on the screen, 
 * it has its image component that is being changed
 * everytime the card gets flipped
 */
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class Card {
	Display display;
	private String name;
	// x and y co-ords for the card
	private int[] pos = new int[2];
	// image side up
	private boolean flipped;
	private Image img;

	public Card(Display display, String name, int x, int y) {
		this.name = name;
		this.flipped = false;
		pos[0] = x;
		pos[1] = y;
		this.display = display;
		img = new Image(display, "blank.jpg");
	}

	public int[] getPos() {
		return pos;
	}

	public Image getImage() {
		return img;
	}

	public void setImage(Image img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	// flips the card regardless of whether it has been flipped before
	// or not
	public void flip() {
		flipped = !flipped;
		if (flipped == false)
			img = new Image(display, "blank.jpg");
		else
			img = new Image(display, name);
	}

	public boolean isFlipped() {
		return flipped;
	}

}
