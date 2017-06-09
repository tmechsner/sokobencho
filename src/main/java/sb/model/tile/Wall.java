package sb.model.tile;

import sb.model.misc.Direction;
import sb.model.moveable.Moveable;

/**
 * A tile that cannot be passed.
 */
public class Wall extends Tile {

	@Override public boolean isPassable(Moveable moveable, Direction movement) {

		return false;
	}

	@Override public boolean walkBy(Moveable moveable) {

		return false;
	}

	@Override public boolean leave(Moveable moveable) {

		return false;
	}
}
