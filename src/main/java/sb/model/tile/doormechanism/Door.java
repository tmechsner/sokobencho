package sb.model.tile.doormechanism;

import sb.model.misc.Direction;
import sb.model.misc.Vector;
import sb.model.moveable.Moveable;
import sb.model.tile.Tile;

/**
 * A door tile that can be opened by buttons
 */
public class Door extends Tile {

	/**
	 * Is this door open?
	 */
	private boolean open;

	/**
	 * The position of this door
	 */
	private final Vector pos;

	public Door(Vector pos) {

		this.pos = pos;
	}

	/**
	 * Opens or closes this door.
	 *
	 * @param open
	 * @return True, if the state changed, false, if not.
	 */
	public boolean setOpen(boolean open) {

		boolean wasOpen = this.open;
		this.open = open;
		return open != wasOpen;
	}

	public boolean isOpen() {

		return open;
	}

	public Vector getPos() {

		return pos;
	}

	/**
	 * A door is only passable if it is open.
	 *
	 * @param moveable
	 * @param movement
	 * @return
	 */
	@Override public boolean isPassable(Moveable moveable, Direction movement) {

		return isOpen();
	}

	@Override public boolean walkBy(Moveable moveable) {

		return false;
	}

	@Override public boolean leave(Moveable moveable) {

		return false;
	}
}
