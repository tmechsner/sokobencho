package sb.model.tile;

import sb.model.misc.Direction;
import sb.model.moveable.Box;
import sb.model.moveable.Moveable;
import sb.model.moveable.Rock;

/**
 * This tile can only be passed by heavy moveables if they are moved in the right direction.
 */
public class Rutting extends Tile {

	/**
	 * The direction from which heavy moveables can be pushed onto this tile.
	 */
	private final Direction allowedDirection;

	public Rutting(Direction allowedDirection) {

		this.allowedDirection = allowedDirection;
	}

	public Direction getAllowedDirection() {

		return allowedDirection;
	}

	/**
	 * Can only be passed by heavy moveables if they enter this tile from the right direction.
	 * Always passable for the player.
	 *
	 * @param moveable
	 * @param movement
	 * @return
	 */
	@Override public boolean isPassable(Moveable moveable, Direction movement) {

		if ((moveable.getClass() == Box.class || moveable.getClass() == Rock.class)) {
			return movement.getInverse().equals(allowedDirection);
		} else {
			return true;
		}
	}

	@Override public boolean walkBy(Moveable moveable) {

		return false;
	}

	@Override public boolean leave(Moveable moveable) {

		return false;
	}
}
