package awharrison;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class testController extends MouseAdapter {
	
	PileView view;
	Intrigue game;
	
	public testController(Intrigue g, PileView v) {
		this.game = g;
		this.view = v;
	}
	
	
	public void MouseReleased(MouseEvent e) {
		System.out.println("Congrats, mouse released");
	}
}


