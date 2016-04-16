package awharrison;

import java.awt.Dimension;

import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;

public class AwharrisonIntrigue extends Solitaire {
	/**
	 * this determines the number of piles and columns, Intrigue has 8 of each
	 */
	private static final int MAX = 8;
	
	/**
	 * Game attributes and views
	 */
	MultiDeck multideck;
	Pile upPile[] = new Pile[MAX];
	Pile downPile[] = new Pile[MAX];
	Column tableau[] = new Column[MAX];
	
	PileView upPileView[] = new PileView[MAX];
	PileView downPileView[] = new PileView[MAX];
	ColumnView tableauView[] = new ColumnView[MAX];
	
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
		int up, down, tab, count;
		count = up = down = tab = 0;

		Card temp = multideck.get();
		Pile tempPile = new Pile("temp Pile");
		boolean loop = true;
		
		
		// draw cards from the deck until all bases are set
		while(loop) {
			
			switch(temp.getRank()) {
			// if a 6 is drawn, set it as an upPile foundation
			case 6:
				upPile[up].add(temp);
				up++;
				count++;
				break;
			// if a 5 is drawn, set it as a downPile foundation
			case 5:
				downPile[down].add(temp);
				down++;
				count++;
				break;
			// if a queen is drawn, set it as a tableau foundation
			case 12:
				tableau[tab].add(temp);
				tab++;
				count++;
				break;
			// if none of the above...
			default:
				// flag for pile placement 
				int placed = 0;
				// if the card can be placed on an up pile, do so
				if (up > 0) {
					for(int i = 0; i < up; i++) {
						if(temp.getRank() == upPile[up - 1].rank() + 1) {
							upPile[up - 1].add(temp);
							count++;
							this.updateScore(1);
							placed = 1;
							break;
						}
					}
				}
				// if the card can be placed on a down pile do so
				if (down > 0) {
					for(int i = 0; i < down; i++) {
						if(temp.getRank() == downPile[down - 1].rank() - 1) {
							downPile[down - 1].add(temp);
							count++;
							this.updateScore(1);
							placed = 1;
							break;
						} else if ((temp.getRank() == 13) && (downPile[down - 1].rank() == 1)) {
							downPile[down - 1].add(temp);
							count++;
							this.updateScore(1);
							placed = 1;
							break;
						}
					}
					
				}
				// if a tableau has been set, place the card on the tableau
				if (placed == 0) {
					if (tab > 0) {
						tableau[tab - 1].add(temp);
					// else set it on a temporary pile to be dealt with at the end
					} else {
						tempPile.add(temp);
					}
				}
				break;
					
				// otherwise return the card to the deck and shuffle with a new seed (no return to bottom of the deck method)
			}
			// terminate loop if there are no cards left to place
			if(!multideck.empty()) {
				temp = multideck.get();
			} else if (!tempPile.empty()){
				temp = tempPile.get();
			} else 
				loop = false;
		}
		updateNumberCardsLeft (-count);
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
			tableauView[i].setMouseAdapter(new IntrigueTableauController (this, tableauView[i]));
			tableauView[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
			tableauView[i].setUndoAdapter(new SolitaireUndoAdapter(this));
		}
		
		// initialize adapters for each upPile
		for (int i = 0; i < MAX; i++) {
			upPileView[i].setMouseAdapter(new IntrigueUpPileController (this, upPileView[i]));
			upPileView[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
			upPileView[i].setUndoAdapter(new SolitaireUndoAdapter(this));
		}
		
		// initialize adapters for each downPile
		for (int i = 0; i < MAX; i++) {
			downPileView[i].setMouseAdapter(new IntrigueDownPileController (this, downPileView[i]));
			downPileView[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
			downPileView[i].setUndoAdapter(new SolitaireUndoAdapter(this));
		}
		
		// initialize adapters for the container
		getContainer().setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		getContainer().setMouseAdapter (new SolitaireReleasedAdapter(this));
		getContainer().setUndoAdapter(new SolitaireUndoAdapter(this));
	}
	
	/**
	 * initializes the view classes for the game at start up
	 */
	private void initializeView() {
		// get a card to determine it's height and width
		CardImages ci = getCardImages();
		ci.setOverlap(15); // default overlap size was too large
		
		// for loop to initialize each individual upPileView 
		for(int i = 0; i < MAX; i++) {
			upPileView[i] = new PileView(upPile[i]);
			upPileView[i].setBounds((i+1)*20 + i*ci.getWidth(), 60, ci.getWidth(), ci.getHeight());
			addViewWidget(upPileView[i]);
		}
		
		// for loop to initialize each individual downPileView
		for(int i = 0; i < MAX; i++) {
			downPileView[i] = new PileView(downPile[i]);
			downPileView[i].setBounds((i+1)*20 + i*ci.getWidth(), 70 + ci.getHeight(), ci.getWidth(), ci.getHeight());
			addViewWidget(downPileView[i]);
		}
		
		// for loop to initialize each individual tableauView
		for(int i = 0; i < MAX; i++) {
			tableauView[i] = new ColumnView(tableau[i]);
			tableauView[i].setBounds((i+1)*20 + i*ci.getWidth(), 80 + 2*ci.getHeight(), ci.getWidth(), 8*ci.getHeight());
			addViewWidget(tableauView[i]);
		}
		
		// initialze placement for the game score view
		scoreView = new IntegerView(getScore());
		scoreView.setBounds(2*ci.getWidth(), 0, 100, 60);
		addViewWidget(scoreView);
		
		// initialize placement for the number of cards left view
		numLeftView = new IntegerView(getNumLeft());
		numLeftView.setBounds(6*ci.getWidth(), 0, 100, 60);
		addViewWidget(numLeftView);
	}
	
	private void initializeModel(int seed) {
		// multideck for intrigue consists of 2 decks
		multideck = new MultiDeck("mydeck", 2);
		
		// initialize the multideck and add it to the game
		multideck.create(seed);
		addModelElement(multideck);
		
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
		GameWindow gw = IntrigueLauncher.generateWindow(new AwharrisonIntrigue(), 11230);
		gw.setVisible(true);
	}
}
