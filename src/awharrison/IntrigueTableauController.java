package awharrison;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;

public class IntrigueTableauController extends MouseAdapter {
	
	ColumnView src;
	Intrigue game;
	
	
	public IntrigueTableauController(Intrigue g, ColumnView v) {
		this.game = g;
		this.src = v;
	}
	
	public void mousePressed(MouseEvent me) {
		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = game.getContainer();
		
		/** Return if there is no card to be chosen or the chosen card is a queen. */
		Column test = (Column) src.getModelElement();
		if ((test.count() == 0) || (test.rank() == 12)) {
			c.releaseDraggingObject();
			return;
		}
	
		// get the top card from the source column 
		CardView cardView = src.getCardViewForTopCard (me);
		
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
			System.err.println ("Intrigue::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}
	
		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (cardView, me);
		
		// Tell container which source widget initiated the drag
		c.setDragSource (src);
	
		// The only widget that could have changed is ourselves. If we called refresh, there
		// would be a flicker, because the dragged widget would not be redrawn. We simply
		// force the WastePile's image to be updated, but nothing is refreshed on the screen.
		// This is patently OK because the card has not yet been dragged away to reveal the
		// card beneath it.  A bit tricky and I like it!
		src.redraw();
	}
	
	public void mouseReleased(MouseEvent me) {
		// if the mouse is released on a column, just return to the widget to its origin
		Container c = game.getContainer();
		
		/** set the dragging object, return if no card being dragged */
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println ("IntrigueUpPileController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();		
			return;
		}

		/** get the from column */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("IntrigueUpPileController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}
		
		/** determine the toColumn */
		Column toColumn = (Column) src.getModelElement();
		
		/** set fromColumn */
		ColumnView fromColumnView = (ColumnView) fromWidget;
		Column fromColumn = (Column) fromColumnView.getModelElement();
		
		/** set card being moved */
		CardView cardBeingMovedView = (CardView) draggingWidget;
		Card cardBeingMoved = (Card) cardBeingMovedView.getModelElement();
		
		/** perform move */
		Move move = new MoveTableauToTableau(fromColumn, cardBeingMoved, toColumn);
		if (move.doMove(game)) {
			game.pushMove (move);     // Successful Move has been Move
		} else {
			fromWidget.returnWidget (draggingWidget);
		}
		
		/** release object and repaint the game view */
		c.releaseDraggingObject();
		c.repaint();
		return;
	}
	
}