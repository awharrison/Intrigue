package awharrison;

import ks.common.games.Solitaire;
import ks.common.model.*;

/**
 * Represents the moving of a card from one pile to another.  
 */
public class MoveCardDownPile extends ks.common.model.Move {
	/** From & To piles. */
	Column from;
	Pile to;

	/** The card being moved (if any). */
	Card movingCard;

	/**
	 * MoveCardMove constructor.
	 * <p>
	 * @param from         The source Pile from which the move originates
	 * @param movingCard   If the move is in progress, this is the card
	 *                     being dragged from Pile <code>from</code>.
	 * @param to           The target Pile for the move.
	 */
	public MoveCardDownPile (Column from, Card movingCard, Pile to) {
		super();

		this.from = from;
		this.movingCard = movingCard;
		this.to = to;
	}

	/**
	 * Each move should knows how to execute itself.
	 * <p>
	 * @param ks.common.game.Solitaire   the game being played.
	 * @return boolean
	 */
	public boolean doMove (Solitaire theGame) {
		// VALIDATE:
		if (valid (theGame) == false) {
			System.out.println("invalid move");
			return false;
		}

		// EXECUTE:
		System.out.println("valid move");
		to.add (movingCard);

		return true;
	}

	/**
	 * To undo this move, we move the cards from the toPile back to the fromPile
	 * @param theGame ks.common.games.Solitaire
	 */
	public boolean undo(Solitaire game) {
		// VALIDATE:
		if (to.empty()) return false;

		// UNDO: move back
		from.add (to.get());
		return true;
	}

	/**
	 * Validate MoveCardMove.
	 * <p>
	 * @param game 
	 */
	public boolean valid (Solitaire game) {
		// VALIDATION:
		boolean validation = false;
		
		return true;
	}
}