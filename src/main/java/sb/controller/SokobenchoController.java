package sb.controller;

import sb.model.Level;
import sb.model.misc.Direction;
import sb.model.misc.Vector;

/**
 * Created by timo on 12.09.15.
 */
public class SokobenchoController implements AdvancedTileBoardController {

	/**
	 * The game model
	 */
	private Level model;

	/**
	 * Is the level finished?
	 */
	private boolean finished;

	/**
	 * Ignore the next incoming movement order, e.g. to prevent the "any key to continue" to trigger a movement.
	 */
	private boolean ignoreNextMovement;

	public SokobenchoController(Level model) {

		this.model = model;
		finished = false;
		ignoreNextMovement = false;
	}

	/**
	 * Check if the clicked tile is a neighbour of the current player position.
	 * If so, move the player
	 *
	 * @param clickedPos
	 */
	public void handleClick(Vector clickedPos) {

		Vector playerPos = model.getPlayerPos();
		if (playerPos.isNeighbour(clickedPos)) {
			handleMovement(Direction.getDirection(playerPos, clickedPos));
		}
	}

	/**
	 * Move the player into the given direction
	 *
	 * @param direction
	 */
	public void handleMovement(Direction direction) {

		if (!ignoreNextMovement) {
			if (!finished)
				model.movePlayer(direction);
		} else {
			ignoreNextMovement = false;
		}
	}

	/**
	 * Deactivate the game if it is finished
	 */
	public void handleCompleted() {

		finished = true;
	}

	/**
	 * A key press triggers the loading of the next level if the current level is finished.
	 */
	public void handleAnyKey() {

		if (finished) {
			finished = false;
			ignoreNextMovement = true;
			model.nextLevel();
		}
	}

	/**
	 * Reset the level
	 */
	public void handleReset() {

		model.resetLevel();
	}
}
