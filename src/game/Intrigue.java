package game;

import java.awt.Dimension;

import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.MultiDeck;
import ks.common.model.Pile;
import ks.common.view.ColumnView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;

public class Intrigue extends Solitaire {
	private static final int MAX = 8;
	MultiDeck multideck;
	Pile upPile[];
	Pile downPile[];
	Column tableau[];
	
	PileView upPileView;
	PileView downPileView;
	ColumnView tableauView;
	
	IntegerView scoreView;
	
	@Override
	public String getName() {
		return "awharrison-Intrigue";
	}
	

	@Override
	public boolean hasWon() {
		return false; // TODO determine what a win is
	}
	
	@Override
	public void initialize() {
		// initialize model
		// TODO this will be a pretty large part of the game
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension (800, 300);
	}
}
