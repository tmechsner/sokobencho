package sb.model.tile.doormechanism;

/**
 * A {@link ButtonDoorGroup} whose doors are open if an even number of two or more buttons are pressed.
 */
public class ParityGroup extends ButtonDoorGroup {

	public ParityGroup() {

		super();
	}

	/**
	 * The given door is open if an even number of two or more connected buttons are pressed.
	 *
	 * @param door
	 * @return
	 */
	@Override protected boolean isDoorOpenInternal(Door door) {

		long buttonsPressed = getPressedButtonsCount();
		return (buttonsPressed >= 2) && ((buttonsPressed % 2) == 0);
	}

}
