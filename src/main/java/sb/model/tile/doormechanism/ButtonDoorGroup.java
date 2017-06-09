package sb.model.tile.doormechanism;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a group of doors and  the buttons that can open these
 */
public abstract class ButtonDoorGroup {

	protected List<Door> doors;

	protected List<Button> buttons;

	public ButtonDoorGroup() {

		doors = new ArrayList<>();
		buttons = new ArrayList<>();
	}

	/**
	 * The group-type-specific rule of when a door is open.
	 * This method is called by the public method {@link #isDoorOpen(Door)}.
	 *
	 * @param door
	 * @return
	 */
	protected abstract boolean isDoorOpenInternal(Door door);

	/**
	 * Checks if the door is open according to the internal ButtonDoorGroup rule.
	 * Throws an {@link IllegalArgumentException} if the door does not belong to this group.
	 *
	 * @param door
	 * @return
	 */
	public boolean isDoorOpen(Door door) {

		if (doors.contains(door)) {
			return isDoorOpenInternal(door);
		} else {
			throw new IllegalArgumentException("The given door does not belong to this ButtonDoorGroup!");
		}
	}

	public void addDoor(Door door) {

		doors.add(door);
	}

	public void addButton(Button button) {

		buttons.add(button);
	}

	public boolean containsDoor(Door door) {

		return doors.contains(door);
	}

	/**
	 * Returns the number of buttons in this group that are pressed.
	 *
	 * @return
	 */
	protected long getPressedButtonsCount() {

		return buttons.stream().filter(b -> b.isPressed()).collect(Collectors.counting());
	}

	/**
	 * Deletes all doors and buttons in this group
	 */
	public void clear() {

		doors.clear();
		buttons.clear();
	}
}
