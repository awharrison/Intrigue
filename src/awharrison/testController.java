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
import ks.common.view.Widget;

public class testController extends SolitaireReleasedAdapter {
	
	ColumnView view;
	
	public testController(Intrigue g, ColumnView v) {
		super(g);
		this.view = v;
	}
	
	public void mousePressed(MouseEvent me) {
		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();
		
		/** Return if there is no card to be chosen. */
		Column test = (Column) view.getModelElement();
		if (test.count() == 0) {
			c.releaseDraggingObject();
			return;
		}
	
		// Get a card to move from PileView. Note: this returns a CardView.
		// Note that this method will alter the model for BuildablePileView if the condition is met.
		CardView cardView = view.getCardViewForTopCard (me);
		
		// an invalid selection of some sort.
		if (cardView == null) {
			c.releaseDraggingObject();
			return;
		}
		
		// If we get here, then the user has indeed clicked on the top card in the PileView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("WastePileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}
	
		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (cardView, me);
		
		// Tell container which source widget initiated the drag
		c.setDragSource (view);
	
		// The only widget that could have changed is ourselves. If we called refresh, there
		// would be a flicker, because the dragged widget would not be redrawn. We simply
		// force the WastePile's image to be updated, but nothing is refreshed on the screen.
		// This is patently OK because the card has not yet been dragged away to reveal the
		// card beneath it.  A bit tricky and I like it!
		view.redraw();
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
		Column fromColumn = (Column) fromWidget.getModelElement();

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


