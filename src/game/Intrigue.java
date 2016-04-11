package game;

import java.awt.Dimension;

import awh.control.IntrigueTableauToDownController;
import awh.control.IntrigueTableauToUpController;
import awh.control.testController;
import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;

public class Intrigue extends Solitaire {
	/**
	 * this determines the number of piles and columns, Intrigue has 8 of each
	 */
	private static final int MAX = 8;
	
	/**
	 * Game attributes and views
	 */
	MultiDeck multideck;
	Pile upPile[];
	Pile downPile[];
	Column tableau[];
	
	PileView upPileView[];
	PileView downPileView[];
	ColumnView tableauView[];
	
	IntegerView scoreView;
	IntegerView numLeftView;
	
	/**
	 * get method for the name of the game
	 */
	@Override
	public String getName() {
		return "awharrison-Intrigue";
	}
	
	/**
	 * player wins a game of Intrigue by placing all cards from tableau piles to 
	 * upPiles an downPiles (a total of 80 cards)
	 */
	@Override
	public boolean hasWon() {
		return getScoreValue() == 80;
	}
	
	/**
	 * initializes the game at start up
	 */
	@Override
	public void initialize() {
		// initialize model
		initializeModel(getSeed());
		initializeView();
		initializeControllers();

		// Game has been initialized, prepare game for play
		
		// set indexes for the piles and columns
		int up, down, tab;
		up = down = tab = 0;
		
		
		// draw cards from the deck until all bases are set
		
		while(true) {
			Card temp = multideck.get();
			switch(temp.getRank()) {
			// if a 6 is drawn, set it as an upPile foundation
			case 6:
				upPile[up].add(temp);
				up++;
				break;
			// if a 5 is drawn, set it as a downPile foundation
			case 5:
				downPile[down].add(temp);
				down++;
				break;
			// if a queen is drawn, set it as a tableau foundation
			case 12:
				tableau[tab].add(temp);
				tab++;
				break;
			// if none of the above...
			default:
				// if a tableau foundation has been set, place the drawn card on it
				if(tab > 0) {
					tableau[tab-1].add(temp);
				// otherwise return the card to the deck and shuffle with a new seed (no return to bottom of the deck method)
				} else {
					multideck.add(temp);
					multideck.shuffle(getSeed()+1);
				}
				break;
			}
			if(!upPile[MAX-1].empty() && !downPile[MAX-1].empty() && !tableau[MAX-1].empty()){
				break;
			}
		}

		updateNumberCardsLeft (-3*8);
		
	}
	
	/**
	 * sets the size of the game window
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension (800, 1000);
	}
	
	/**
	 * initializes the controllers for the game at start up
	 */
	private void initializeControllers() {
		// initialize adapters for each tableau column
		for (int i = 0; i < MAX; i++) {
			tableauView[i].setMouseAdapter(new testController (this, tableauView[i]));
		}
		
		// initialize adapters for each upPile
		
		// initialize adapters for each downPile
		
		// initialize adapters for updating the number of cards left and the score
	}
	
	/**
	 * initializes the view classes for the game at start up
	 */
	private void initializeView() {
		// get a card to determine it's height and width
		CardImages ci = getCardImages();
		// initialize the size of the arrays for the up and down piles as well as the tableau columns
		upPileView = new PileView[MAX];
		downPileView = new PileView[MAX];
		tableauView = new ColumnView[MAX];
		
		// for loop to initialize each individual upPileView 
		for(int i = 0; i < MAX; i++) {
			upPileView[i] = new PileView(upPile[i]);
			upPileView[i].setBounds((i+1)*20 + i*ci.getWidth(), 80, ci.getWidth(), ci.getHeight());
			addViewWidget(upPileView[i]);
		}
		
		// for loop to initialize each individual downPileView
		for(int i = 0; i < MAX; i++) {
			downPileView[i] = new PileView(downPile[i]);
			downPileView[i].setBounds((i+1)*20 + i*ci.getWidth(), 100 + ci.getHeight(), ci.getWidth(), ci.getHeight());
			addViewWidget(downPileView[i]);
		}
		
		// for loop to initialize each individual tableauView
		for(int i = 0; i < MAX; i++) {
			tableauView[i] = new ColumnView(tableau[i]);
			tableauView[i].setBounds((i+1)*20 + i*ci.getWidth(), 120 + 2*ci.getHeight(), ci.getWidth(), 8*ci.getHeight());
			addViewWidget(tableauView[i]);
		}
		
		// initialze placement for the game score view
		scoreView = new IntegerView(getScore());
		scoreView.setBounds(2*ci.getWidth(), 10, 100, 60);
		addViewWidget(scoreView);
		
		// initialize placement for the number of cards left view
		numLeftView = new IntegerView(getNumLeft());
		numLeftView.setBounds(6*ci.getWidth(), 10, 100, 60);
		addViewWidget(numLeftView);
	}
	
	private void initializeModel(int seed) {
		// multideck for intrigue consists of 2 decks
		multideck = new MultiDeck("mydeck", 2);
		
		// initialize the multideck and add it to the game
		multideck.create(seed);
		addModelElement(multideck);
		
		// create the pile and tableau column models
		upPile = new Pile[MAX];
		downPile = new Pile[MAX];
		tableau = new Column[MAX];
		
		// initialize and add the upPile models to the game
		for(int i = 0; i < MAX; i++) {
			String str = "upPile" + i;
			upPile[i] = new Pile(str);
			addModelElement(upPile[i]);
		}
		
		// initialize and add the downPile models to the game
		for(int i = 0; i < MAX; i++) {
			String str = "downPile" + i;
			downPile[i] = new Pile(str);
			addModelElement(downPile[i]);
		}
		
		// initialize and add the tableau column models to the game
		for(int i = 0; i < MAX; i++) {
			String str = "tableau" + i;
			tableau[i] = new Column(str);
			addModelElement(tableau[i]);
		}
		
		// initialize the number of cards left -- 2 decks = 104 cards
		numLeft = getNumLeft();
		numLeft.setValue(104); 
		
		// initialize the score to 0
		score = getScore();
		score.setValue(0);
		
	}
	
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		GameWindow gw = IntrigueMain.generateWindow(new Intrigue(), 11230);
		gw.setVisible(true);
	}
}
