package sb.controller;

import sb.model.misc.Direction;
import sb.model.misc.Vector;

/**
 * Created by timo on 11.09.15.
 */
public interface AdvancedTileBoardController {

	/**
	 * Handles a mouse click at the given position
	 */
	void handleClick(Vector clickedPos);

	/**
	 * Handles a player movement command
	 *
	 * @param direction
	 */
	void handleMovement(Direction direction);

	/**
	 * Handle level completion
	 */
	void handleCompleted();

	/**
	 * A key is pressed
	 */
	void handleAnyKey();

	/**
	 * Reset the current level
	 */
	void handleReset();
}
