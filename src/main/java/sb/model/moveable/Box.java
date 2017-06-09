package sb.model.moveable;

import sb.model.misc.Vector;

/**
 * Boxes have to be pushed onto the targets by the player in order to win the game.
 */
public class Box extends Pushable {

	public Box(Vector position) {

		super(position);
	}

}
