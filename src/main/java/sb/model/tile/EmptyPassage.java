package sb.model.tile;

import sb.model.moveable.Moveable;

/**
 * An empty passable passage.
 */
public class EmptyPassage extends Passage {

	@Override public boolean walkBy(Moveable moveable) {

		return false;
	}

	@Override public boolean leave(Moveable moveable) {

		return false;
	}
}
