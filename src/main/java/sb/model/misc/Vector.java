package sb.model.misc;

/**
 * An immutable int-2-tuple that can e.g. be interpreted as a position or a movement.
 */
public class Vector {

	private final int x;
	private final int y;

	public Vector(int x, int y) {

		this.x = x;
		this.y = y;
	}

	public Vector(Vector vec) {

		this.x = vec.x;
		this.y = vec.y;
	}

	public int getX() {

		return x;
	}

	public int getY() {

		return y;
	}

	/**
	 * Adds another vector to this vector, component-by-component.
	 *
	 * @param vec
	 * @return The sum of vectors
	 */
	public Vector add(Vector vec) {

		return add(vec.getX(), vec.getY());
	}

	/**
	 * See {@link #add(sb.model.misc.Vector)}.
	 *
	 * @param dx
	 * @param dy
	 * @return
	 */
	public Vector add(int dx, int dy) {

		return new Vector(x + dx, y + dy);
	}

	/**
	 * Subtracts another vector from this vector, component-by-component.
	 *
	 * @param vec
	 * @return The difference of vectors
	 */
	public Vector sub(Vector vec) {

		return sub(vec.getX(), vec.getY());
	}

	/**
	 * See {@link #sub(Vector)}.
	 *
	 * @param dx
	 * @param dy
	 * @return
	 */
	public Vector sub(int dx, int dy) {

		return new Vector(x - dx, y - dy);
	}

	/**
	 * Multiplies this vector by s, component-by-component
	 *
	 * @param s
	 * @return
	 */
	public Vector mul(int s) {

		return new Vector(x * s, y * s);
	}

	/**
	 * "Normalizes" this vector by dividing it's components by their absolute value.
	 * This "normalization" is not mathematically correct and is just for finding out whether it's orientation
	 * is north, east, south or west.
	 *
	 * @return
	 */
	public Vector normalize() {

		int ax = Math.abs(x);
		int ay = Math.abs(y);
		int nx = x / (ax != 0 ? ax : 1);
		int ny = y / (ay != 0 ? ay : 1);
		return new Vector(nx, ny);
	}

	/**
	 * Returns a new vector with negated components
	 *
	 * @return
	 */
	public Vector negate() {

		return new Vector(-x, -y);
	}

	/**
	 * Check if the given vector (interpreted as position) is a direct horizontal or vertical neighbour.
	 * More precise: Check if the distance is equal to 1.
	 *
	 * @param toCheck Position as vector to check
	 * @return
	 */
	public boolean isNeighbour(Vector toCheck) {

		return (Math.abs(x - toCheck.getX()) + Math.abs(y - toCheck.getY())) == 1;
	}

	/**
	 * Checks if the given Object is equal to this vector component-by-component
	 *
	 * @param o
	 * @return
	 */
	@Override public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Vector vector = (Vector) o;

		if (x != vector.x)
			return false;
		if (y != vector.y)
			return false;

		return true;
	}

	@Override public int hashCode() {

		int result = x;
		result = 31 * result + y;
		return result;
	}
}
