package sb.model.tile;

import sb.model.moveable.Box;
import sb.model.moveable.Moveable;

/**
 * Targets have to be filled with boxes in order to complete a level.
 */
public class Target extends Passage {

	/**
	 * Is there a box on this target?
	 */
	private boolean filled;

	/**
	 * Creates a new Target. It is either filled with a box or not.
	 *
	 * @param filled
	 */
	public Target(boolean filled) {

		this.filled = filled;
	}

	public boolean isFilled() {

		return filled;
	}

	/**
	 * Fill this target if a box moves onto it.
	 *
	 * @param moveable
	 * @return
	 */
	@Override public boolean walkBy(Moveable moveable) {

		if (moveable.getClass() == Box.class) {
			filled = true;
			return true;
		}
		return false;
	}

	/**
	 * Un-fill this target if a box leaves it.
	 *
	 * @param moveable
	 * @return
	 */
	@Override public boolean leave(Moveable moveable) {

		if (moveable.getClass() == Box.class) {
			filled = false;
			return true;
		}
		return false;
	}
}
