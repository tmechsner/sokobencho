package sb.view;

import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import sb.model.moveable.Box;
import sb.model.moveable.Moveable;
import sb.model.tile.CrackedFloor;
import sb.model.tile.Rutting;
import sb.model.tile.Target;
import sb.model.tile.Teleporter;
import sb.model.tile.Tile;
import sb.model.tile.Wall;
import sb.model.tile.doormechanism.Button;
import sb.model.tile.doormechanism.Door;

/**
 * Creates all images for
 */
public class GraphicsMaker {

	/**
	 * Creates graphics for boxes and rocks
	 *
	 * @param moveable
	 * @return
	 */
	public static Node makeMoveableGraphic(Moveable moveable) {

		if (moveable instanceof Box) {
			return makeBoxGraphic();
		} else {
			return makeRockGraphic();
		}
	}

	/**
	 * Creates graphics for all kind of tiles
	 *
	 * @param tile
	 * @return
	 */
	public static Node makeTileGraphic(Tile tile) {

		if (tile instanceof Wall) {
			return makeWallGraphic();
		} else if (tile instanceof CrackedFloor) {
			return makeCrackedFloorGraphic((CrackedFloor) tile);
		} else if (tile instanceof Rutting) {
			return makeRuttingGraphic((Rutting) tile);
		} else if (tile instanceof Target) {
			return makeTargetGraphic((Target) tile);
		} else if (tile instanceof Teleporter) {
			return makeTeleporterGraphic((Teleporter) tile);
		} else if (tile instanceof Door) {
			return makeDoorGraphic((Door) tile);
		} else if (tile instanceof Button) {
			return makeButtonGraphic();
		} else {
			return makePassageGraphic();
		}
	}

	/**
	 * Creates the player graphic
	 *
	 * @return
	 */
	public static Node makePlayerGraphic() {

		return makeCircle(Color.CORNFLOWERBLUE, Color.DARKBLUE, 18);
	}

	//
	// Utility methods
	//

	private static Node makeBoxGraphic() {

		return makeRect(Color.SANDYBROWN, Color.SADDLEBROWN, 40, 2);
	}

	private static Node makeRockGraphic() {

		Node base = makeRect(Color.WHITE, null);
		Polygon rock = new Polygon();
		Double[] points = new Double[] { 15.0, 12.0, 20.0, 8.0, 22.0, 3.0, 17.0, -7.0, 2.0, -19.0, -10.0, -18.0, -21.0, -10.0, -18.0, 7.0 };
		rock.getPoints().addAll(points);
		rock.setFill(Color.LIGHTGRAY);
		rock.setStroke(Color.DARKGRAY);
		return new Group(base, rock);
	}

	private static Node makeButtonGraphic() {

		Node bigRect = makeRect(Color.WHITE, null);
		Node smallRect = makeRect(Color.MEDIUMVIOLETRED, Color.INDIANRED, 30, 2);
		return new Group(bigRect, smallRect);
	}

	private static Node makeDoorGraphic(Door tile) {

		Node node;
		if (tile.isOpen()) {
			node = makeRect(Color.WHITE, Color.INDIANRED);
		} else {
			Node rect = makeRect(Color.DIMGRAY, Color.INDIANRED);
			Line line = new Line(0, -24, 0, 24);
			line.setStroke(Color.DARKGRAY);
			node = new Group(rect, line);
		}
		return node;
	}

	private static Group makeTeleporterGraphic(Teleporter tile) {

		Node base;
		if (tile.getType() == Teleporter.Type.PLAYER)
			base = makeCircle(Color.LIGHTBLUE, null, 20);
		else
			base = makeRect(Color.LIGHTBLUE, null);

		Text id = new Text();
		id.setText(String.valueOf(tile.getPairId()));
		id.setTextOrigin(VPos.CENTER);

		return new Group(base, id);
	}

	private static Group makeTargetGraphic(Target tile) {

		Node rect;
		if (tile.isFilled()) {
			rect = makeRect(Color.WHITE, Color.GREEN, 48, 2);
		} else {
			rect = makeRect(Color.WHITE, Color.RED, 48, 2);
		}

		Node circle = makeCircle(Color.WHITE, Color.BLACK, 7);

		Line lineHori = new Line(-15, 0, 15, 0.0);
		lineHori.setStroke(Color.BLACK);

		Line lineVert = new Line(0, -15, 0, 15);
		lineVert.setStroke(Color.BLACK);

		Group cross = new Group(lineHori, lineVert);

		return new Group(rect, circle, cross);
	}

	private static Node makeRuttingGraphic(Rutting tile) {

		Node rect = makeRect(Color.WHITE, Color.LIGHTGRAY);
		Polygon triangle = new Polygon();
		Double[] points = new Double[0];
		switch (tile.getAllowedDirection()) {
			case N:
				points = new Double[] { 0.0, 0.0, -24.0, -24.0, 24.0, -24.0 };
				break;
			case O:
				points = new Double[] { 0.0, 0.0, 24.0, -24.0, 24.0, 24.0 };
				break;
			case S:
				points = new Double[] { 0.0, 0.0, -24.0, 24.0, 24.0, 24.0 };
				break;
			case W:
				points = new Double[] { 0.0, 0.0, -24.0, -24.0, -24.0, 24.0 };
				break;
		}
		triangle.getPoints().addAll(points);
		triangle.setFill(Color.BEIGE);
		return new Group(rect, triangle);
	}

	private static Node makeWallGraphic() {

		return makeRect(Color.DIMGRAY, null);
	}

	private static Node makePassageGraphic() {

		return makeRect(Color.WHITE, null);
	}

	private static Node makeCrackedFloorGraphic(CrackedFloor tile) {

		Node rect = makeRect(Color.WHITE, null);
		Text steadyness = new Text(String.valueOf(tile.getSteadyness()));

		Line l1 = new Line(-20, -20, -10, -15);
		Line l2 = new Line(-10, -15, -5, -5);
		Line l3 = new Line(-5, -5, 15, 10);
		Line l4 = new Line(15, 10, 20, 20);

		Line l5 = new Line(-15, -10, -8, 5);
		Line l6 = new Line(-8, 5, 5, 10);
		Line l7 = new Line(5, 10, 10, 20);

		return new Group(rect, steadyness, l1, l2, l3, l4, l5, l6, l7);
	}

	private static Node makeCircle(Color color, Color stroke, int radius) {

		Circle circle = new Circle(radius);
		circle.setFill(color);
		if (stroke != null) {
			circle.setStroke(stroke);
			circle.setStrokeType(StrokeType.INSIDE);
		}
		return circle;
	}

	private static Rectangle makeRect(Color fill, Color stroke) {

		return makeRect(fill, stroke, 48);
	}

	private static Rectangle makeRect(Color fill, Color stroke, double size) {

		return makeRect(fill, stroke, size, 1);
	}

	private static Rectangle makeRect(Color fill, Color stroke, double size, double strokeWidth) {

		Rectangle r = new Rectangle(-(size / 2.0), -(size / 2.0), size, size);
		r.setFill(fill);
		if (stroke != null) {
			r.setStroke(stroke);
			r.setStrokeWidth(strokeWidth);
			r.setStrokeType(StrokeType.INSIDE);
		}
		return r;
	}

}
