package sb.model.tile;

import sb.model.misc.Direction;
import sb.model.moveable.Box;
import sb.model.moveable.Moveable;
import sb.model.moveable.Rock;

/**
 * A tile that can only be passed a few times by heavy moveables
 */
public class CrackedFloor extends Tile {

	/**
	 * The remaining number of heavy moveables that can pass this tile
	 */
	private int steadyness;

	public CrackedFloor(int steadyness) {

		this.steadyness = steadyness;
	}

	public int getSteadyness() {

		return steadyness;
	}

	/**
	 * Only passable by heavy moveables if steadyness is greater than zero.
	 * Always passable by the player.
	 *
	 * @param moveable
	 * @param movement
	 * @return
	 */
	@Override public boolean isPassable(Moveable moveable, Direction movement) {

		if (moveable.getClass() == Box.class || moveable.getClass() == Rock.class) {
			return steadyness > 0;
		} else {
			return true;
		}
	}

	/**
	 * Reduce steadyness if a heavy moveable is moved onto this tile.
	 *
	 * @param moveable
	 * @return
	 */
	@Override public boolean walkBy(Moveable moveable) {

		if (moveable.getClass() == Box.class || moveable.getClass() == Rock.class) {
			steadyness--;
			return true;
		} else {
			return false;
		}
	}

	@Override public boolean leave(Moveable moveable) {

		return false;
	}
}
