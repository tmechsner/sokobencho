package sb.view;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * A view for a extra window to inform the player about controls etc.
 */
public class ControlsInfoView extends Region {

	/**
	 * The info text
	 */
	private Text controlsInfo;

	public ControlsInfoView() {

		ObservableList<Node> myChildren = getChildren();

		controlsInfo = new Text("Welcome to Sokobencho!\n" + "Place all boxes on a target to get to the next level.\n" + "\nControls:\n" + "Arrow keys / W, A, S, D - Move\n" + "R - Reset the level\n" + "Q - Quit\n" + "Have fun!");

		myChildren.add(controlsInfo);
	}

	/**
	 * Return the preferred width of this JavaFX region.
	 *
	 * @param height
	 * @return
	 */
	@Override protected double computePrefWidth(double height) {

		return 350;
	}

	/**
	 * Return the preferred height of this JavaFX region.
	 *
	 * @param width
	 * @return
	 */
	@Override protected double computePrefHeight(double width) {

		return 150;
	}

	/**
	 * Set the position of the text
	 */
	@Override protected void layoutChildren() {

		super.layoutChildren();
		controlsInfo.relocate(8, 8);
	}

}
