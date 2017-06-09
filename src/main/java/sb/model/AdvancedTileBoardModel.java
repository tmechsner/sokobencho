package sb.model;

import sb.model.misc.Vector;
import sb.model.moveable.Event;
import sb.view.AdvancedTileBoardView;

import java.util.List;

/**
 * Interface for a model of a tile based game
 */
public interface AdvancedTileBoardModel<T, M> {

	int getLevelWidth();

	int getLevelHeight();

	Vector getPlayerPos();

	List<M> getMoveables();

	String getMessage();

	/**
	 * Register a new view for displaying the game
	 *
	 * @param view
	 */
	void registerView(AdvancedTileBoardView view);

	/**
	 * Unregister a previously registered view
	 *
	 * @param view
	 */
	void unregisterView(AdvancedTileBoardView view);

	/**
	 * Load the next level.
	 */
	void nextLevel();

	/**
	 * Returns the tile at the given position
	 *
	 * @param pos
	 * @return
	 */
	T getTile(Vector pos);

	/**
	 * Returns the latest event containing information about a moved object
	 *
	 * @return
	 */
	Event<M> getLatestMoveableEvent();

	/**
	 * Gets the number of the current level
	 *
	 * @return
	 */
	int getCurrentLevelIndex();
}
