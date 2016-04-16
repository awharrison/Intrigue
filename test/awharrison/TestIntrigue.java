package awharrison;

import java.awt.event.MouseEvent;

import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.view.CardImages;
import ks.common.view.Container;
import ks.common.view.Widget;
import ks.tests.KSTestCase;

public class TestIntrigue extends KSTestCase{
	
	AwharrisonIntrigue game;
	GameWindow gw;
	
	@Override
	public void setUp() {
		game = new AwharrisonIntrigue();
		gw = IntrigueLauncher.generateWindow(game, 50);
	}
	
	protected void tearDown() {
		gw.setVisible(false);
		gw.dispose();
	}
	
	/**
	 * post-initialization...
	 * validate that the total number of cards is 104 (2 full decks)
	 * validate that the upPiles are in ascending order
	 * validate that the downPiles are in descending order
	 * validate that the game's multideck is empty
	 */
	public void testInitialize() {
		
		int upPileCards, downPileCards, tableauCards, i;
		upPileCards = downPileCards = tableauCards = 0;
		
		for(i = 0; i < 8; i++) {
			upPileCards += game.upPile[i].count();
			assertTrue(game.upPile[i].ascending());
			downPileCards += game.downPile[i].count();
			assertTrue(game.downPile[i].descending());
			tableauCards += game.tableau[i].count();
		}
		
		assertTrue(game.multideck.empty());
		assertEquals(104, upPileCards + downPileCards + tableauCards);
	}
	
	/**
	 * Card positions are known for this seed from playing the game, these will not be 
	 * the same if the seed changes
	 * 
	 * I can't test the full functionality with OrderByRank or OrderBySuit as they can fully 
	 * stack the up or down piles, making it very difficult/impossible to test all scenarios
	 */
	public void testTableauController() {
		CardImages ci = game.getCardImages();
		Container c = game.getContainer();
		
		// overlap was set to 15 in game initialization, validate the top card of the first tableau and tableau card count
		assertEquals(ci.getOverlap(), 15);
		assertEquals (new Card(Card.JACK, Card.DIAMONDS), game.tableau[0].peek());
		assertEquals (3, game.tableau[0].count());
		
		// first create a mouse event
		MouseEvent pr = createPressed (game, game.tableauView[0], 0, ci.getOverlap()*game.tableau[0].count());
		game.tableauView[0].getMouseManager().handleMouseEvent(pr);
		
		// validate that the card being dragged is the previous top of the tableau
		assertEquals (new Card(Card.JACK, Card.DIAMONDS), (Card) c.getActiveDraggingObject().getModelElement());
		
		// validate that the card below is different and the number of cards on the tableau is 1 less
		assertEquals (2, game.tableau[0].count());
		assertEquals (new Card(Card.NINE, Card.DIAMONDS), game.tableau[0].peek());
		
		// reset the board
		Widget fromWidget = c.getDragSource();
		fromWidget.returnWidget (c.getActiveDraggingObject());
		c.releaseDraggingObject();
		
		// make a tableau pile empty (with the exception of the base/Queen)
		game.tableau[2].get();
		assertEquals (new Card(Card.QUEEN, Card.CLUBS), game.tableau[2].peek());
		
		// validate that the queen cannot be dragged/selected
		pr = createPressed (game, game.tableauView[2], 0, ci.getOverlap()*game.tableau[2].count());
		game.tableauView[2].getMouseManager().handleMouseEvent(pr);
		assertEquals (null, (Card) c.getActiveDraggingObject().getModelElement());
		
		// verify that a card from another tableau can be moved to the empty tableau
		pr = createPressed (game, game.tableauView[0], 0, ci.getOverlap()*game.tableau[0].count());
		game.tableauView[0].getMouseManager().handleMouseEvent(pr);
		
		MouseEvent rel = createReleased (game, game.tableauView[2], 0, ci.getOverlap()*game.tableau[2].count());
		game.tableauView[2].getMouseManager().handleMouseEvent(rel);
		assertEquals (new Card(Card.JACK, Card.DIAMONDS), game.tableau[2].peek());
	}
	
	/**
	 * 
	 */
	public void testUpPileController() {
		CardImages ci = game.getCardImages();
		Container c = game.getContainer();
		
		// grab a card of rank 11
		MouseEvent pr = createPressed (game, game.tableauView[0], 0, ci.getOverlap()*game.tableau[0].count());
		game.tableauView[0].getMouseManager().handleMouseEvent(pr);
		assertEquals (new Card(Card.JACK, Card.DIAMONDS), (Card) c.getActiveDraggingObject().getModelElement());
		
		// place the card on upPile with top card of rank 10
		assertEquals (new Card(Card.TEN, Card.DIAMONDS), game.upPile[6].peek());
		MouseEvent rel = createReleased (game, game.upPileView[6], 0, 0);
		game.upPileView[6].getMouseManager().handleMouseEvent(rel);
		assertEquals (new Card(Card.JACK, Card.DIAMONDS), game.upPile[6].peek());
		
		// validate that a card that is rank 11 cannot be placed on on upPile with top card of rank 7
		assertTrue(game.undoMove());
		
		pr = createPressed (game, game.tableauView[0], 0, ci.getOverlap()*game.tableau[0].count());
		game.tableauView[0].getMouseManager().handleMouseEvent(pr);
		assertEquals (new Card(Card.JACK, Card.DIAMONDS), (Card) c.getActiveDraggingObject().getModelElement());
		
		assertEquals (new Card(Card.SEVEN, Card.HEARTS), game.upPile[2].peek());
		rel = createReleased (game, game.upPileView[2], 0, 0);
		game.upPileView[2].getMouseManager().handleMouseEvent(rel);
		assertEquals (new Card(Card.SEVEN, Card.HEARTS), game.upPile[2].peek());
	}
	
	/**
	 * 
	 */
	public void testDownPileController() {
		CardImages ci = game.getCardImages();
		Container c = game.getContainer();
		
		// grab a card of rank 3
		MouseEvent pr = createPressed (game, game.tableauView[2], 0, ci.getOverlap()*game.tableau[2].count());
		game.tableauView[2].getMouseManager().handleMouseEvent(pr);
		assertEquals (new Card(Card.THREE, Card.CLUBS), (Card) c.getActiveDraggingObject().getModelElement());
		
		// place the card on downPile with top card of rank 4
		assertEquals (new Card(Card.FOUR, Card.CLUBS), game.downPile[4].peek());
		MouseEvent rel = createReleased (game, game.downPileView[4], 0, 0);
		game.downPileView[4].getMouseManager().handleMouseEvent(rel);
		assertEquals (new Card(Card.THREE, Card.CLUBS), game.downPile[4].peek());
		
		// validate that undoMove works as intended
		assertEquals (new Card(Card.QUEEN, Card.CLUBS), game.tableau[2].peek());
		assertTrue(game.undoMove());
		assertEquals (new Card(Card.THREE, Card.CLUBS), game.tableau[2].peek());
		
		// validate that a card of rank 3 cannot be placed on a card of rank 2
		pr = createPressed (game, game.tableauView[2], 0, ci.getOverlap()*game.tableau[2].count());
		game.tableauView[2].getMouseManager().handleMouseEvent(pr);
		assertEquals (new Card(Card.THREE, Card.CLUBS), (Card) c.getActiveDraggingObject().getModelElement());
		
		assertEquals (new Card(Card.TWO, Card.CLUBS), game.downPile[5].peek());
		rel = createReleased (game, game.downPileView[5], 0, 0);
		game.downPileView[5].getMouseManager().handleMouseEvent(rel);
		assertEquals (new Card(Card.TWO, Card.CLUBS), game.downPile[5].peek());
		
	}
	
	/**
	 * validate that adding the number of cards from the 
	 */
	public void testHasWon() {
		
		int tableauCards, i;
		tableauCards = 0;
		
		for(i = 0; i < 8; i++) {
			tableauCards += game.tableau[i].count();
		}
		game.updateScore((tableauCards - 8));
		assertTrue(game.hasWon());
	}
}
