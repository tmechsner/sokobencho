package sb.model.tile.doormechanism;

/**
 * This enum should hold all implementations of {@link ButtonDoorGroup}.
 * Each enum entry can create a new instance of it's corresponding group type class.
 */
public enum ButtonDoorGroupType {
	OR(OrGroup.class), PARITY(ParityGroup.class);

	private Class<? extends ButtonDoorGroup> groupClass;

	<T extends ButtonDoorGroup> ButtonDoorGroupType(Class<T> groupClass) {

		this.groupClass = groupClass;
	}

	/**
	 * Create a new instance of this group type's class.
	 *
	 * @return
	 */
	public ButtonDoorGroup createGroup() {

		try {
			return groupClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Class<? extends ButtonDoorGroup> getGroupClass() {

		return groupClass;
	}
}