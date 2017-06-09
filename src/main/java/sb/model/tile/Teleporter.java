package sb.model.tile;

import sb.model.misc.Pair;
import sb.model.misc.Vector;
import sb.model.moveable.Box;
import sb.model.moveable.Moveable;
import sb.model.moveable.Player;
import sb.model.moveable.Rock;

import java.util.HashMap;
import java.util.Map;

/**
 * A teleporter is always linked to another teleporter.
 * It is only functional for either the player or for boxes and rocks.
 * If such an object moves onto this tile it is teleported to it's linked teleporter.
 * This does not work if there is something standing on the target teleporter.
 * This tile acts like an {@link EmptyPassage} for non-affected objects and if the target teleporter is blocked.
 */
public class Teleporter extends Passage {

	/**
	 * Association between the identifying char and the two linked teleporters.
	 */
	private static Map<Character, Pair<Teleporter>> connections = new HashMap<>();

	/**
	 * The position of this teleporter
	 */
	private final Vector position;

	/**
	 * Teleporter only for OBJECTS or only for PLAYER?
	 */
	private final Type type;

	/**
	 * The char identifying this teleporter and it's counterpart
	 */
	private final char pairId;

	/**
	 * Is there something standing on this tile?
	 */
	private boolean blocked;

	/**
	 * All possible types of teleporter
	 */
	public enum Type {
		PLAYER, OBJECT;
	}

	/**
	 * Creates a new teleporter and links it to it's counterpart if it's already existant.
	 *
	 * @param pairId
	 * @param type
	 * @param position
	 */
	public Teleporter(char pairId, Type type, Vector position) {

		this.type = type;
		this.position = position;
		this.pairId = pairId;
		Pair<Teleporter> pair = connections.get(pairId);
		if (pair == null) {
			pair = new Pair(this);
			connections.put(pairId, pair);
		} else {
			pair.setOther(this);
		}
	}

	public boolean isBlocked() {

		return blocked;
	}

	public void setBlocked(boolean blocked) {

		this.blocked = blocked;
	}

	public Vector getPosition() {

		return position;
	}

	public Type getType() {

		return type;
	}

	public char getPairId() {

		return pairId;
	}

	/**
	 * Checks if there are exactly two teleporters for each pairId.
	 *
	 * @return True, if there are exactly two teleporters for each pairId, false, if not.
	 */
	public static boolean checkPairs() {

		return connections.entrySet().stream().map(e -> e.getValue().isPaired()).reduce(true, Boolean::logicalAnd);
	}

	/**
	 * Returns the position of the linked teleporter. Throws NoSuchElementException if it doesn't have one.
	 *
	 * @return
	 */
	public Teleporter getTarget() {

		return connections.get(pairId).getOther(this).get();
	}

	/**
	 * Teleports the moveable to this teleporter's target if it is not blocked and if the moveable matches this teleporter's type.
	 * Block the target after a successfull teleport.
	 *
	 * @param moveable
	 * @return
	 */
	@Override public boolean walkBy(Moveable moveable) {

		if (((type == Type.PLAYER) && (moveable.getClass() == Player.class)) || ((type == Type.OBJECT) && (moveable.getClass() == Rock.class || moveable.getClass() == Box.class))) {

			Teleporter target = getTarget();
			if (!target.isBlocked()) {
				target.setBlocked(true);
				moveable.setPosition(target.getPosition());
			}
		}
		return false;
	}

	/**
	 * Unblock this teleporter if it is left.
	 *
	 * @param moveable
	 * @return
	 */
	@Override public boolean leave(Moveable moveable) {

		blocked = false;
		return false;
	}

	/**
	 * Delete all registered teleporter associations
	 */
	public static void clear() {

		connections.clear();
	}

}
