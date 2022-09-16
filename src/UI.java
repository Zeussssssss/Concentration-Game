/*
 * Author: Aditya Kumar
 * Course: CSC 335
 * File: Card.java
 * 
 * This is the main class of this project, and 
 * it manages all of the GUI of the Concentration game. 
 * It uses SWT as its graphics module, and window builder
 * was used to make the various components used in the
 * game. 
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class UI {
	static Shell shell;
	static Display display;
	static int numberOfPlayers = 0;
	static int index = 0;
	static boolean oneFlip = false;
	static String optionChosen = "Animals";
	static Board board;
	static Set<Card> matchedCards = new HashSet<>();
	static List<Card> currentlyFlipped = new ArrayList<>();
	static Player[] playersList;

	/* 
	 * This function is used to check whether the cards 
	 * that have been clicked by the current user in their 
	 * most recent move are the same or not. 
	 * It also decides which player is gonna play the next move
	 * 
	 */
	private static void checkFlipped(Canvas canvas, CLabel displayPlayer) {
		if (currentlyFlipped.size() != 1) {
			boolean matched = false;
			if (currentlyFlipped.get(0).getName() == currentlyFlipped.get(1).getName()) {
				//match
				matchedCards.add(currentlyFlipped.get(0));
				matchedCards.add(currentlyFlipped.get(1));
				playersList[index % numberOfPlayers].incrementScore();
				matched = true;
			} else {
				//so that the user gets a good look at their selection
				canvas.redraw();
				canvas.update();
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//flips both the cards back
				currentlyFlipped.get(0).flip();
				currentlyFlipped.get(1).flip();
			}
			//to check for the oneflip setting
			if (!matched || oneFlip) {
				index++;
				displayPlayer.setText("Player " + String.valueOf(index % numberOfPlayers + 1) + " is playing");
			}

			currentlyFlipped.remove(0);
			currentlyFlipped.remove(0);
		}
	}
	
	/*
	 * This function is excecuted when the game
	 * gets over and all the cards have been matched. 
	 * It disposes the ccanvas, and displays the name 
	 * of the winner on the screen 
	 * 
	 */
	private static void gameOver(Canvas canvas, CLabel displayPlayer) {
		canvas.dispose();
		displayPlayer.dispose();

		CLabel heading = new CLabel(shell, SWT.BORDER);
		heading.setAlignment(SWT.CENTER);
		heading.setBounds(45, 10, 414, 71);
		heading.setText("GAME OVER");
		heading.setFont(new Font(display, "Arial", 16, SWT.BOLD));

		CLabel tableHeading = new CLabel(shell, SWT.NONE);
		tableHeading.setAlignment(SWT.CENTER);
		tableHeading.setBounds(45, 125, 414, 71);
		tableHeading.setText("THE WINNER IS:");
		tableHeading.setFont(new Font(display, "Arial", 16, SWT.NORMAL));

		//deciding the winner
		int maxScore = 0;
		Player winner = playersList[0];
		for (Player player : playersList) {
			if (player.getScore() > maxScore) {
				maxScore = player.getScore();
				winner = player;
			}
		}

		CLabel winnerName = new CLabel(shell, SWT.NONE);
		winnerName.setAlignment(SWT.CENTER);
		winnerName.setBounds(45, 200, 414, 71);
		winnerName.setText("PLAYER " + winner.getName());
		winnerName.setFont(new Font(display, "Arial", 16, SWT.BOLD));
	}

	/*
	 * The method that is excecuted after the user clicks on the play game button in
	 * the main menu. It clears the canvas, and makes a new quit button and displays
	 * the cards on the screen
	 */
	private static void newGame() {
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setSize(500, 400);
		board = new Board(optionChosen);

		CLabel displayPlayer = new CLabel(shell, SWT.CENTER);
		displayPlayer.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.BOLD));
		displayPlayer.setAlignment(SWT.CENTER);
		displayPlayer.setBounds(250, 400, 227, 47);
		displayPlayer.setText("Player " + String.valueOf(index + 1) + " is playing");
		displayPlayer.setFont(new Font(display, "Arial", 10, SWT.BOLD));

		canvas.addPaintListener(new CanvasPaintListener(display, board));
		canvas.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent event) {
			}

			//In the cases of a click
			public void mouseDown(MouseEvent event) {
				Rectangle rect = shell.getClientArea();
				ImageData data = board.getCard(0).getImage().getImageData();
				int col = event.x / (data.width + 30);
				int row = event.y / (data.height + 30);
				//Index of the tile that was clicked
				int idx = col + row * rect.width / (data.width + 30);
				if (idx < board.getDeckSize() && !matchedCards.contains(board.getCard(idx))) {
					board.getCard(idx).flip();
					if (board.getCard(idx).isFlipped()) {
						currentlyFlipped.add(board.getCard(idx));
						checkFlipped(canvas, displayPlayer);
					} else {
						currentlyFlipped.remove(board.getCard(idx));
					}
				}

				canvas.redraw();
				canvas.update();
				if (matchedCards.size() == board.getDeckSize())
					gameOver(canvas, displayPlayer);
			}

			public void mouseUp(MouseEvent e) {
			}
		});

		Button quitButton = new Button(shell, SWT.PUSH);
		quitButton.setText("Quit");
		quitButton.setBounds(30, 400, 100, 50);
		quitButton.addSelectionListener(new ButtonSelectionListener());
	}

	/* 
	 * Creates the Play game method and defines its
	 * properties and behaviours
	 */

	private static void playGameHandler(Text players) {
		Button start = new Button(shell, SWT.BORDER | SWT.CENTER);
		start.setFont(SWTResourceManager.getFont("Arial", 14, SWT.NORMAL));
		start.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// initializing the player class
				numberOfPlayers = Integer.valueOf(players.getText());
				for (Control child : shell.getChildren()) {
					child.dispose();
				}
				playersList = new Player[numberOfPlayers];
				for (int i = 0; i < numberOfPlayers; i++) {
					playersList[i] = new Player(i);
				}
				newGame();
			}
		});
		start.setBounds(142, 367, 227, 71);
		start.setText("PLAY GAME!");
	}

	/*
	 * a sub method to allow users to select their game in the main menu
	 */
	private static void modeSelector() {
		Label ModeSelectorHeader = new Label(shell, SWT.NONE);
		ModeSelectorHeader.setAlignment(SWT.CENTER);
		ModeSelectorHeader.setBounds(94, 188, 347, 47);
		ModeSelectorHeader.setText("PLEASE SELECT A GAME MODE");
		ModeSelectorHeader.setFont(new Font(display, "Arial", 10, SWT.NORMAL));

		// Radio buttons for the
		Button option1 = new Button(shell, SWT.RADIO);
		option1.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		option1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				optionChosen = "Animals";

			}
		});
		option1.setBounds(183, 236, 133, 25);
		option1.setText("Animals");

		Button option2 = new Button(shell, SWT.RADIO);
		option2.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		option2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				optionChosen = "Fruits";
			}
		});
		option2.setBounds(183, 269, 133, 25);
		option2.setText("Fruits");

	}

	/*
	 * Method to display the initial main menu on the screen
	 * 
	 */

	private static void displayMainMenu() {
		// Heading
		CLabel heading = new CLabel(shell, SWT.BORDER);
		heading.setAlignment(SWT.CENTER);
		heading.setBounds(45, 10, 414, 71);
		heading.setText("CONCENTRATION GAME");
		heading.setFont(new Font(display, "Arial", 16, SWT.BOLD));

		// No of players input
		CLabel PlayerNumber = new CLabel(shell, SWT.CENTER);
		PlayerNumber.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		PlayerNumber.setAlignment(SWT.CENTER);
		PlayerNumber.setBounds(142, 87, 227, 47);
		PlayerNumber.setText("Number of Players");
		PlayerNumber.setFont(new Font(display, "Arial", 10, SWT.NORMAL));

		final Text NoOfPlayers = new Text(shell, SWT.SINGLE | SWT.BORDER);
		NoOfPlayers.setLocation(183, 139);
		NoOfPlayers.setText("1");
		NoOfPlayers.setSize(new Point(139, 31));
		NoOfPlayers.setSize(139, 31);

		// OneFlip mode toggle
		Button oneFlipToggle = new Button(shell, SWT.CHECK);
		oneFlipToggle.setOrientation(SWT.RIGHT_TO_LEFT);
		oneFlipToggle.setAlignment(SWT.CENTER);
		oneFlipToggle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				oneFlip = true;
			}
		});
		oneFlipToggle.setBounds(170, 316, 168, 31);
		oneFlipToggle.setText(" ? ONE FLIP");
		oneFlipToggle.setFont(new Font(display, "Arial", 11, SWT.NORMAL));

		modeSelector();
		playGameHandler(NoOfPlayers);
	}

	public static void main(String args[]) {

		display = new Display();

		shell = new Shell(display);
		shell.setSize(549, 538);

		displayMainMenu();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
}

/*
 * Sub class to draw and repaint the canvas when its instance is called.
 */
class CanvasPaintListener implements PaintListener {
	Image img;
	List<Card> cards = new ArrayList<>();
	Display display;
	Board board;

	public CanvasPaintListener(Display display, Board board) {
		this.board = board;
		this.display = display;
	}

	public void paintControl(PaintEvent event) {
		for (int i = 0; i < board.getDeckSize(); i++) {
			Card current = board.getCard(i);
			// to print all the images at their specified coordinates
			event.gc.drawImage(current.getImage(), current.getPos()[0], current.getPos()[1]);
		}
	}
}

/**
 * Quit button
 * 
 */
class ButtonSelectionListener implements SelectionListener {
	public void widgetSelected(SelectionEvent event) {
		System.exit(0);
	}

	public void widgetDefaultSelected(SelectionEvent event) {
	}
}
