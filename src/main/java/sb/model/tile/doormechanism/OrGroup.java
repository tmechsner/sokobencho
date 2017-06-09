package sb.model.tile.doormechanism;

/**
 * A {@link ButtonDoorGroup} whose doors are open if one or more buttons are pressed.
 */
public class OrGroup extends ButtonDoorGroup {

	public OrGroup() {

		super();
	}

	/**
	 * The given door is open if at least one of the connected buttons is pressed
	 *
	 * @param door
	 * @return
	 */
	@Override protected boolean isDoorOpenInternal(Door door) {

		long buttonsPressed = getPressedButtonsCount();
		return buttonsPressed >= 1;
	}
}
