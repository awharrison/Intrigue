package awharrison;

import ks.tests.KSTestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;

public class TestMoves extends KSTestCase {
	Column tab1, tab2;
	Pile up1, up2, down1, down2;
	AwharrisonIntrigue game;
	GameWindow gw;
	Card movingCard;
	
	@Override 
	protected void setUp() {
		game = new AwharrisonIntrigue();
		gw = IntrigueLauncher.generateWindow(game, MultiDeck.OrderBySuit);
	}
	
	@Override
	protected void tearDown() {
		gw.setVisible(false);
		gw.dispose();
	}
	
	public void testMoveUpPile() {
		for(int i = 0; i < 8; i++) {
			if(game.tableau[i].peek().getRank() == 7) {
				tab1 = game.tableau[i];
				movingCard = game.tableau[i].get();
				break;
			}
		}
		
		for(int i = 0; i < 8; i++) {
			if(game.upPile[i].peek().getRank() == 6) {
				up1 = game.upPile[i];
				break;
			}
		}
	}
	
	public void testMoveDownPile() {
		
	}
	
	public void testTabToTab() {
		
	}
	
	public void testUIMoves () {
		
	}
}
