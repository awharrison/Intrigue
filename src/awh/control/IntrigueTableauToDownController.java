package awh.control;

import game.Intrigue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;
import awh.move.MoveCardDownPile;

public class IntrigueTableauToDownController extends MouseAdapter{
	Intrigue game;
	ColumnView view;
	
	public IntrigueTableauToDownController(Intrigue g, ColumnView v) {
		this.game = g;
		this.view = v;
	}
	
	public void MousePressed(MouseEvent e) {
		
		Container c = game.getContainer();

		/** Return if there is no card to be chosen. */
		Column col = (Column) view.getModelElement();
		if (col.count() == 0) {
			c.releaseDraggingObject();
			return;
		}
		
		// Could be something! Verify that the user clicked on the TOP card in the Column.
				// Note that this method will alter the model for columnView if the condition is met.
		CardView cardView = view.getCardViewForTopCard(e);
		if (cardView == null) {
			return;
		}
		
		// If we get here, then the user has indeed clicked on the top card in the ColumnView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("Intrigue::pressCardController(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}

		// Tell container which object is being dragged, and where in that widget the mouse was clicked.
		c.setActiveDraggingObject (cardView, e);

		// Have container remember who initiated the drag
		c.setDragSource (view);

		// we simply redraw our source pile to avoid flicker,
		// rather than refreshing all widgets...
		view.redraw();

	}
	
	public void MouseReleased(MouseEvent e) {
		Container c = game.getContainer();

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
		Column fromColumn = (Column) fromWidget.getModelElement();

		Pile toPile = (Pile) view.getModelElement();

		// Try to make the move
		Move m = new MoveCardDownPile (fromColumn, theCard, toPile);
		if (m.doMove (game)) {
			// Successful move!  
			// add move to our set of moves
			game.pushMove (m);
		} else {
			// Invalid move. Restore dragging widget to source
			fromWidget.returnWidget (w);
		}

		c.releaseDraggingObject();    // also releases dragSource

		c.repaint();
	}
}

