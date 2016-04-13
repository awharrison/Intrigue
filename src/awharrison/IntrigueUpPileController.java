package awharrison;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

//public class IntrigueUpPileController extends SolitaireReleasedAdapter {
public class IntrigueUpPileController extends MouseAdapter {
	
	PileView src;
	Intrigue theGame;
	
	public IntrigueUpPileController(Intrigue theGame, PileView v) {
		this.theGame = theGame;
//		super(theGame);
		this.src = v;
	}
	
	
	public void mouseReleased(MouseEvent e) {
		/** set the container */
		Container c = theGame.getContainer();
		
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
		
		/** determine the toPile */
		Pile toPile = (Pile) src.getModelElement();
		
		/** set fromColumn */
		ColumnView fromColumnView = (ColumnView) fromWidget;
		Column fromColumn = (Column) fromColumnView.getModelElement();
		
		/** set card being moved */
		CardView cardBeingMovedView = (CardView) draggingWidget;
		Card cardBeingMoved = (Card) cardBeingMovedView.getModelElement();
		
		/** perform move */
		Move move = new MoveCardUpPile(fromColumn, cardBeingMoved, toPile);
		if (move.doMove(theGame)) {
			theGame.pushMove (move);     // Successful Move has been Move
		} else {
			fromWidget.returnWidget (draggingWidget);
		}
		
		/** release object and repaint the game view */
		c.releaseDraggingObject();    // also releases dragSource
		c.repaint();
	}
}

