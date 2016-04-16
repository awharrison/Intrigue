package awharrison;

import ks.client.gamefactory.GameWindow;
import ks.tests.KSTestCase;

public class TestIntrigue extends KSTestCase{
	
	AwharrisonIntrigue game;
	GameWindow gw;
	
	@Override
	public void setUp() {
		game = new AwharrisonIntrigue();
		gw = IntrigueLauncher.generateWindow(game, 11230);
	}
	
	protected void tearDown() {
		gw.setVisible(false);
		gw.dispose();
	}
	
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
		assertEquals(upPileCards + downPileCards + tableauCards, 104);
	}
	
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
