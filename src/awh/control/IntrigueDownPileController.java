package awh.control;

import game.Intrigue;

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
import awh.move.MoveCardDownPile;

public class IntrigueDownPileController extends SolitaireReleasedAdapter{
	
	PileView view;
	
	public IntrigueDownPileController(Intrigue g, PileView v) {
		super(g);
		this.view = v;
	}
	
	
	public void MouseReleased(MouseEvent e) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget w = c.getActiveDraggingObject();
		if (w == Container.getNothingBeingDragged()) return;

		/** Must be the CardView widget. */
		CardView cardView = (CardView) w;
		Card theCard = (Card) cardView.getModelElement();
		if (theCard == null) {
			System.err.println ("Idiot::releaseCardController(): somehow CardView model element is null.");
			return;
		}

		/** Recover the From Column */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("Idiot::releaseCardController(): somehow fromWidget is null.");
			return;
		}
		ColumnView fromColumnView = (ColumnView) c.getDragSource();
		Column fromColumn = (Column) fromColumnView.getModelElement();

		Pile toPile = (Pile) view.getModelElement();

		// Try to make the move
		Move m = new MoveCardDownPile (fromColumn, theCard, toPile);
		if (m.doMove (theGame)) {
			// Successful move!  
			// add move to our set of moves
			theGame.pushMove (m);
		} else {
			// Invalid move. Restore dragging widget to source
			fromWidget.returnWidget (w);
		}

		c.releaseDraggingObject();    // also releases dragSource

		c.repaint();
	}
}

