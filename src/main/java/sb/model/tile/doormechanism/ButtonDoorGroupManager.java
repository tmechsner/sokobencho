package sb.model.tile.doormechanism;

import sb.model.misc.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A singleton managing the {@link ButtonDoorGroup}s
 */
public class ButtonDoorGroupManager {

	/**
	 * The one and only instance
	 */
	private static ButtonDoorGroupManager instance;

	/**
	 * All groups mapped onto their identifying char
	 */
	private Map<Character, ButtonDoorGroup> groups;

	private ButtonDoorGroupManager() {

		this.groups = new HashMap<>();
	}

	/**
	 * Get the one and only instance of this class
	 *
	 * @return
	 */
	public static ButtonDoorGroupManager getInstance() {

		if (instance == null) {
			instance = new ButtonDoorGroupManager();
		}
		return instance;
	}

	/**
	 * Adds a door to a given {@link ButtonDoorGroup} identified by 'c'.
	 * If there is no group for 'c' yet, create a new one.
	 * If there is a group but it does not match the required type, do nothing.
	 *
	 * @param c    Identifier for group
	 * @param door
	 * @param type Required type of the group
	 */
	public void addDoor(char c, Door door, ButtonDoorGroupType type) {

		ButtonDoorGroup group = getOrCreateGroup(c, type);

		if (group.getClass().equals(type.getGroupClass())) {
			group.addDoor(door);
		}
	}

	/**
	 * Adds a button to a given {@link ButtonDoorGroup} identified by 'c'.
	 * If there is no group for 'c' yet, create a new one.
	 * If there is a group but it does not match the required type, do nothing.
	 *
	 * @param c      Identifier for group
	 * @param button
	 * @param type   Required type of the group
	 */
	public void addButton(char c, Button button, ButtonDoorGroupType type) {

		ButtonDoorGroup group = getOrCreateGroup(c, type);

		if (group.getClass().equals(type.getGroupClass())) {
			group.addButton(button);
		}
	}

	/**
	 * Check all door mechanisms to open and close doors..
	 *
	 * @return A list of positions of the doors that were opened or closed
	 */
	public List<Vector> updateDoors() {

		return groups.values().stream().flatMap(group -> group.doors.stream()).filter(door -> door.setOpen(isDoorOpen(door))).map(door -> door.getPos()).collect(Collectors.toList());
	}

	/**
	 * Gets the group identified by 'c'. If none is present, create one.
	 *
	 * @param c    Group identifier
	 * @param type Group type to create if no group is identified by 'c'.
	 * @return
	 */
	private ButtonDoorGroup getOrCreateGroup(char c, ButtonDoorGroupType type) {

		ButtonDoorGroup group = groups.get(Character.toLowerCase(c));

		if (group == null) {
			group = type.createGroup();
			groups.put(Character.toLowerCase(c), group);
		}

		return group;
	}

	/**
	 * Checks if the given door is open according to the rules of the {@link ButtonDoorGroup} it belongs to.
	 * If it does not belong to a group the default result is false.
	 * If it belongs to more than one group it is open if at least one of these groups' rules say so.
	 *
	 * @param door
	 * @return
	 */
	private boolean isDoorOpen(Door door) {

		return groups.values().stream().anyMatch(group -> group.containsDoor(door) && group.isDoorOpen(door));
	}

	/**
	 * Delete all groups and the doors and buttons in the groups
	 */
	public void clear() {

		groups.values().forEach(group -> group.clear());
		groups.clear();
	}
}
