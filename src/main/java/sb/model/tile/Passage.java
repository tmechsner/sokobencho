package sb.model.tile;

import sb.model.misc.Direction;
import sb.model.moveable.Moveable;

/**
 * Superclass of passable tiles.
 */
public abstract class Passage extends Tile {

	@Override public boolean isPassable(Moveable moveable, Direction movement) {

		return true;
	}

}
