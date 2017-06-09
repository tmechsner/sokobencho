package sb.model.tile;

import sb.model.misc.Direction;
import sb.model.moveable.Moveable;

/**
 * Superclass of the essential building blocks of a sokobencho level.
 */
public abstract class Tile {

	/**
	 * Is this tile passable by the given moveable?
	 *
	 * @param moveable
	 * @return
	 */
	public abstract boolean isPassable(Moveable moveable, Direction movement);

	/**
	 * The given moveable moves onto this tile.
	 *
	 * @param moveable
	 * @return True, if this tile changed, false, if not.
	 */
	public abstract boolean walkBy(Moveable moveable);

	/**
	 * The given moveable leaves this tile.
	 *
	 * @param moveable
	 * @return True, if this tile changed, false, if not.
	 */
	public abstract boolean leave(Moveable moveable);
}
