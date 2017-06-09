package sb.view;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import sb.controller.AdvancedTileBoardController;
import sb.model.AdvancedTileBoardModel;
import sb.model.misc.Direction;
import sb.model.misc.Vector;
import sb.model.moveable.Event;
import sb.model.moveable.Moveable;
import sb.model.tile.Tile;

import java.util.List;

/**
 * A view for sokobencho displaying the game with JavaFX
 */
public class SokobenchoView extends Region implements AdvancedTileBoardView {

	/**
	 * The width and height of the square tiles
	 */
	private final int TILE_SIZE = 48;

	/**
	 * The layer index of the moveable graphic in the map graphics
	 */
	private final int LAYER_MOVEABLE = 1;

	/**
	 * The layer index of the tile graphic in the map graphics
	 */
	private final int LAYER_TILE = 0;

	/**
	 * The underlying game model
	 */
	private AdvancedTileBoardModel<Tile, Moveable> model;

	/**
	 * The attached controller for user input
	 */
	private AdvancedTileBoardController controller;

	/**
	 * Number of tiles in horizontal
	 */
	private int levelWidth;

	/**
	 * Number of tiles in vertical
	 */
	private int levelHeight;

	/**
	 * An array of image groups, each consisting of a tile graphic and an optional moveable graphic
	 */
	private Group[][] map;

	/**
	 * A displayed status message
	 */
	private Text message;

	/**
	 * Information about which level is played currently
	 */
	private Text levelInfo;

	/**
	 * The current player position
	 */
	private Vector playerPos;

	/**
	 * Creates a new view, wires the given model and controller and registers the handlers.
	 *
	 * @param model
	 * @param controller
	 */
	public SokobenchoView(AdvancedTileBoardModel<Tile, Moveable> model, AdvancedTileBoardController controller) {

		this.model = model;
		this.controller = controller;

		model.registerView(this);
		setOnKeyPressed(new KeyPressedHandler());
		setOnMouseClicked(new MouseClickedHandler());
	}

	/**
	 * Called once after a new level is loaded to the model to initialize the view.
	 */
	private void initLevelDisplay() {

		levelWidth = model.getLevelWidth();
		levelHeight = model.getLevelHeight();
		map = new Group[levelHeight][levelWidth];
		playerPos = model.getPlayerPos();

		ObservableList<Node> myChildren = getChildren();
		myChildren.clear();

		for (int y = 0; y < levelHeight; y++) {
			for (int x = 0; x < levelWidth; x++) {
				Vector curPos = new Vector(x, y);
				Tile tile = model.getTile(curPos);
				Group group = new Group();
				group.getChildren().add(LAYER_TILE, GraphicsMaker.makeTileGraphic(tile));
				if (curPos.equals(playerPos)) {
					group.getChildren().add(LAYER_MOVEABLE, GraphicsMaker.makePlayerGraphic());
				}
				map[y][x] = group;
				myChildren.add(group);
			}
		}

		List<Moveable> moveables = model.getMoveables();
		moveables.forEach(pushable -> {
			Vector pos = pushable.getInitialPosition();
			map[pos.getY()][pos.getX()].getChildren().add(LAYER_MOVEABLE, GraphicsMaker.makeMoveableGraphic(pushable));
		});

		message = new Text(model.getMessage());
		levelInfo = new Text("Level " + model.getCurrentLevelIndex());
		myChildren.add(message);
		myChildren.add(levelInfo);

		getScene().getWindow().sizeToScene();
	}

	/**
	 * A new level has been loaded: Display it!
	 */
	@Override public void repaintLevel() {

		initLevelDisplay();
	}

	/**
	 * Incoming information about a changed tile.
	 * Retrieve the new tile from model and display it.
	 *
	 * @param pos
	 */
	@Override public void updateTile(Vector pos) {

		List<Node> layers = map[pos.getY()][pos.getX()].getChildren();
		Tile tile = model.getTile(pos);
		layers.set(LAYER_TILE, GraphicsMaker.makeTileGraphic(tile));
	}

	/**
	 * Incoming information about a repositioned player.
	 * Retrieve the new position and repaint the player graphic.
	 */
	@Override public void updatePlayer() {

		List<Node> layers = map[playerPos.getY()][playerPos.getX()].getChildren();
		layers.remove(LAYER_MOVEABLE);

		playerPos = model.getPlayerPos();

		layers = map[playerPos.getY()][playerPos.getX()].getChildren();
		layers.add(LAYER_MOVEABLE, GraphicsMaker.makePlayerGraphic());
	}

	/**
	 * Incoming information about a new moveable movement event.
	 * Retrieve the latest event and move the graphics according to it.
	 */
	@Override public void updateMoveable() {

		Event event = model.getLatestMoveableEvent();

		Vector oldPos = event.getOldPosition();
		List<Node> layers = map[oldPos.getY()][oldPos.getX()].getChildren();
		layers.remove(LAYER_MOVEABLE);

		Vector newPos = event.getNewPosition();
		layers = map[newPos.getY()][newPos.getX()].getChildren();
		layers.add(LAYER_MOVEABLE, GraphicsMaker.makeMoveableGraphic((Moveable) event.getMoveable()));
	}

	/**
	 * Incoming information that the level has been completed.
	 * Forward the information to the controller and display a text.
	 */
	@Override public void levelCompleted() {

		controller.handleCompleted();
		message.setText("Level complete! Press any key to continue. ");
	}

	/**
	 * The last level has been finished.
	 * Forward this info to the controller and show congrats message.
	 */
	@Override public void gameFinished() {

		getChildren().clear();
		controller.handleCompleted();
		levelInfo = new Text("Congratulations, you finished the last level!");
		message = new Text("Press any key to quit.");
		getChildren().add(levelInfo);
		getChildren().add(message);
		levelHeight = 0;
		getScene().getWindow().sizeToScene();
	}

	/**
	 * Close the window and quit the game
	 */
	@Override public void quit() {

		getScene().getWindow().hide();
	}

	/**
	 * Incoming information about a new message for the player.
	 * Retrieve the new message and display it.
	 */
	@Override public void updateMessage() {

		message.setText(model.getMessage());
	}

	/**
	 * Calculate the preferred width of this JavaFX region.
	 * In this case this equals the level width (number of horizontal tiles) times tile width.
	 *
	 * @param height
	 * @return
	 */
	@Override protected double computePrefWidth(double height) {

		return levelWidth * TILE_SIZE;
	}

	/**
	 * Calculate the preferred height of this JavaFX region.
	 * In this case this equals the level height (number of vertical tiles) times tile height plus a small region for the player message.
	 *
	 * @param width
	 * @return
	 */
	@Override protected double computePrefHeight(double width) {

		return levelHeight * TILE_SIZE + 42;
	}

	/**
	 * Reposition all map graphics and the player message.
	 */
	@Override protected void layoutChildren() {

		super.layoutChildren();
		for (int y = 0; y < levelHeight; y++) {
			for (int x = 0; x < levelWidth; x++) {
				map[y][x].relocate(x * TILE_SIZE, y * TILE_SIZE);
			}
		}
		levelInfo.relocate(4, levelHeight * TILE_SIZE + 2);
		message.relocate(4, levelHeight * TILE_SIZE + 22);
	}

	/**
	 * Handles key press events by inducing a player movement
	 */
	public class KeyPressedHandler implements EventHandler<KeyEvent> {

		@Override public void handle(KeyEvent event) {

			controller.handleAnyKey();

			KeyCode key = event.getCode();
			Direction dir = null;
			switch (key) {
				case UP:
				case W:
					dir = Direction.N;
					break;
				case DOWN:
				case S:
					dir = Direction.S;
					break;
				case LEFT:
				case A:
					dir = Direction.W;
					break;
				case RIGHT:
				case D:
					dir = Direction.O;
					break;
				case R:
					controller.handleReset();
					break;
				case Q:
					quit();
			}
			if (dir != null) {
				controller.handleMovement(dir);
				event.consume();
			}
		}
	}

	/**
	 * Handles mouse click events by inducing a player movement
	 */
	public class MouseClickedHandler implements EventHandler<MouseEvent> {

		@Override public void handle(MouseEvent event) {

			int x = ((int) event.getX()) / TILE_SIZE;
			int y = ((int) event.getY()) / TILE_SIZE;
			if ((y >= 0) && (y < levelHeight) &&
				(x >= 0) && (x < levelWidth)) {
				controller.handleClick(new Vector(x, y));
				event.consume();
			}
		}
	}

}
