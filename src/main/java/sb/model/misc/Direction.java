package sb.model.misc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds the four cardinal directions
 */
public enum Direction {
	N(new Vector(0, -1)),
	O(new Vector(1, 0)),
	S(new Vector(0, 1)),
	W(new Vector(-1, 0)),
	NONE(new Vector(0, 0));

	private final Vector coords;

	Direction(Vector coords) {

		this.coords = coords;
	}

	public Vector getCoords() {

		return coords;
	}

	/**
	 * Returns the opposite direction
	 *
	 * @return
	 */
	public Direction getInverse() {

		return getDirection(coords.negate());
	}

	/**
	 * Returns the direction a movable had to move into when moving from position "from" to position "to".
	 * Only N, O, S, W are allowed, otherwise NONE is returned.
	 *
	 * @param from
	 * @param to
	 * @return
	 */
	public static Direction getDirection(Vector from, Vector to) {

		Vector dir = to.sub(from).normalize();
		return getDirection(dir);
	}

	/**
	 * Checks whether the given vector is north, south, east or west oriented and return the corresponding enum entry.
	 * If it is none of the above, return "NONE".
	 *
	 * @param dir
	 * @return
	 */
	public static Direction getDirection(Vector dir) {

		Vector dirNorm = dir.normalize();
		List<Direction> matches = Arrays.stream(Direction.values()).filter(d -> d.getCoords().equals(dirNorm)).collect(Collectors.toList());
		if (!matches.isEmpty()) {
			return matches.get(0);
		}
		return NONE;
	}

	/**
	 * Returns the Direction with name equal to c.
	 *
	 * @param c
	 * @return
	 */
	public static Direction getDirection(String c) {

		List<Direction> result = Arrays.stream(Direction.values()).filter(d -> d.name().equals(c)).collect(Collectors.toList());
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		} else {
			return NONE;
		}
	}
}
