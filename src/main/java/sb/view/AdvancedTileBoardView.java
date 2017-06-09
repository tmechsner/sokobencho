package sb.view;

import sb.model.misc.Vector;

/**
 * Created by timo on 11.09.15.
 */
public interface AdvancedTileBoardView {

	/**
	 * Send information that the tile at the given position has changed
	 *
	 * @param pos
	 */
	void updateTile(Vector pos);

	/**
	 * Send information that the player has moved
	 */
	void updatePlayer();

	/**
	 * Send information that a moveable other than the player has moved
	 */
	void updateMoveable();

	/**
	 * Send information that the level is finished
	 */
	void levelCompleted();

	/**
	 * Send information that there is a new message to display
	 */
	void updateMessage();

	/**
	 * Send information that the whole model has to be repainted
	 */
	void repaintLevel();

	/**
	 * Send information that the last level has been finished
	 */
	void gameFinished();

	/**
	 * Quit the game
	 */
	void quit();
}
