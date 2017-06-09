package sb.model.moveable;

import sb.model.misc.Vector;

/**
 * A moveable object
 */
public abstract class Moveable {

	/**
	 * First position in the map
	 */
	private final Vector initialPosition;

	/**
	 * Current position
	 */
	protected Vector position;

	public Moveable(Vector position) {

		this.initialPosition = this.position = position;
	}

	public void setPosition(Vector newPosition) {

		this.position = newPosition;
	}

	public Vector getPosition() {

		return position;
	}

	public Vector getInitialPosition() {

		return initialPosition;
	}

	/**
	 * Reset this Moveable to it's initial state right after loading.
	 */
	public void reset() {

		setPosition(initialPosition);
	}
}
