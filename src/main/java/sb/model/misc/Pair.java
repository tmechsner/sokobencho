package sb.model.misc;

import java.util.Optional;

/**
 * A Pair represents an association between two objects, like an entry of a bidirectional map. It is useful in
 * situations where only one of two associated objects is present but the other one is needed.
 */
public class Pair<T> {

	private Optional<T> one = Optional.empty();

	private Optional<T> other = Optional.empty();

	public Pair(T one) {

		this.one = Optional.ofNullable(one);
	}

	public Pair(T one, T other) {

		this.one = Optional.ofNullable(one);
		this.other = Optional.ofNullable(other);
	}

	/**
	 * Get the other object associated to the given one.
	 *
	 * @param one One of the two associated objects
	 * @return The associated object or null if the given object is not part of this pair
	 */
	public Optional<T> getOther(T one) {

		if (this.one.isPresent() && this.one.get().equals(one)) {
			return this.other;
		} else if (this.other.isPresent() && this.other.get().equals(one)) {
			return this.one;
		}
		return Optional.empty();
	}

	/**
	 * Set 'one' or 'other' if unset
	 *
	 * @param other
	 */
	public void setOther(T other) {

		if (this.one.isPresent()) {
			this.other = Optional.ofNullable(other);
		} else if (this.other.isPresent()) {
			this.one = Optional.ofNullable(other);
		}
	}

	/**
	 * Set a new associated object for one of the pair elements.
	 *
	 * @param one   The object to stay in the pair. No action if it is not in the pair
	 * @param other The object that should be newly associated in this pair
	 */
	public void setOther(T one, T other) {

		if (this.one.isPresent() && this.one.get().equals(one)) {
			this.other = Optional.ofNullable(other);
		} else if (this.other.isPresent() && this.other.get().equals(one)) {
			this.one = Optional.ofNullable(other);
		}
	}

	/**
	 * Checks for presence of the association
	 *
	 * @return true, if both objects are present, false, otherwise
	 */
	public boolean isPaired() {

		return one.isPresent() && other.isPresent();
	}
}
