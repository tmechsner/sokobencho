package sb.model.moveable;

import sb.model.misc.Vector;

/**
 * Carries information about an position update of a moveable.
 */
public class Event<M> {

	private final Vector oldPosition;
	private final Vector newPosition;
	private final M moveable;

	public Event(Vector oldPosition, Vector newPosition, M moveable) {

		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
		this.moveable = moveable;
	}

	public Vector getOldPosition() {

		return oldPosition;
	}

	public Vector getNewPosition() {

		return newPosition;
	}

	public M getMoveable() {

		return moveable;
	}
}
