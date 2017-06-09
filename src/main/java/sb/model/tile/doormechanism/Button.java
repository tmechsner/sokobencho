package sb.model.tile.doormechanism;

import sb.model.moveable.Box;
import sb.model.moveable.Moveable;
import sb.model.moveable.Rock;
import sb.model.tile.Passage;

/**
 * A button tile that can open doors
 */
public class Button extends Passage {

	private boolean pressed;

	public boolean isPressed() {

		return pressed;
	}

	/**
	 * Press the button if a rock or box moves onto it
	 *
	 * @param moveable
	 * @return
	 */
	@Override public boolean walkBy(Moveable moveable) {

		if (moveable.getClass() == Rock.class || moveable.getClass() == Box.class) {
			pressed = true;
			return true;
		}
		return false;
	}

	/**
	 * Release the button if a rock or box leaves it
	 *
	 * @param moveable
	 * @return
	 */
	@Override public boolean leave(Moveable moveable) {

		if (moveable.getClass() == Rock.class || moveable.getClass() == Box.class) {
			pressed = false;
			return true;
		}
		return false;
	}
}
