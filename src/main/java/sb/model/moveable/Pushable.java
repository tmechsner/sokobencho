package sb.model.moveable;

import sb.model.misc.Direction;
import sb.model.misc.Vector;

/**
 * A pushable moveable object
 */
public abstract class Pushable extends Moveable {

	public Pushable(Vector position) {

		super(position);
	}

	/**
	 * Push this moveable into the given direction
	 *
	 * @param dir The direction to move into
	 */
	public void push(Direction dir) {

		setPosition(position.add(dir.getCoords()));
	}

}
