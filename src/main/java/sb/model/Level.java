package sb.model;

import sb.model.misc.Direction;
import sb.model.misc.Vector;
import sb.model.moveable.Box;
import sb.model.moveable.Event;
import sb.model.moveable.Moveable;
import sb.model.moveable.Player;
import sb.model.moveable.Pushable;
import sb.model.moveable.Rock;
import sb.model.tile.CrackedFloor;
import sb.model.tile.EmptyPassage;
import sb.model.tile.Rutting;
import sb.model.tile.Target;
import sb.model.tile.Teleporter;
import sb.model.tile.Tile;
import sb.model.tile.Wall;
import sb.model.tile.doormechanism.Button;
import sb.model.tile.doormechanism.ButtonDoorGroupManager;
import sb.model.tile.doormechanism.ButtonDoorGroupType;
import sb.model.tile.doormechanism.Door;
import sb.view.AdvancedTileBoardView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * The model of the sokobencho game
 */
public class Level implements AdvancedTileBoardModel {

	/**
	 * Upper limit of rows a level can have
	 */
	private final int MAX_ROWS = 20;

	/**
	 * Upper limit of columns a level can have
	 */
	private final int MAX_COLS = 30;

	/**
	 * A ordered list of levelfile paths
	 */
	List<URL> levels;

	/**
	 * The number of the currently loaded level from the list
	 */
	int curLevelIndex;

	/**
	 * Actual width of the level
	 */
	private int levelWidth;

	/**
	 * Actual height of the level
	 */
	private int levelHeight;

	/**
	 * A list of attached views to display the game
	 */
	private List<AdvancedTileBoardView> views;

	/**
	 * The one and only player controllable object
	 */
	private Player player;

	/**
	 * This array of tiles represents one level
	 */
	private Tile[][] map;

	/**
	 * A list of all targets that have to be filled by boxes in order to finish this level
	 */
	private List<Target> targets;

	/**
	 * All pushables of this level mapped onto their positions
	 */
	private Map<Vector, Pushable> pushables;

	/**
	 * The door manager is used to open and close doors
	 */
	private ButtonDoorGroupManager doorManager;

	/**
	 * A list of events containing information about movements of moveables.
	 * The latest event is always the first element in the list.
	 */
	private LinkedList<Event> moveableEvents;

	/**
	 * A message for the player
	 */
	private String message;

	/**
	 * Create a new empty model.
	 */
	public Level() {

		views = new ArrayList<>();
		doorManager = ButtonDoorGroupManager.getInstance();
		moveableEvents = new LinkedList<>();
		targets = new ArrayList<>();
		pushables = new HashMap<>();

		// Load the first level
		levels = initLevelList();
		curLevelIndex = 0;
	}

	/**
	 * Loads the level list from the resources and extracts the level paths.
	 *
	 * @return A list of URLs to the level files in playing order
	 */
	private List<URL> initLevelList() {

		InputStream levelList = this.getClass().getClassLoader().getResourceAsStream("levels.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(levelList));
		String line;
		LinkedList<URL> levels = new LinkedList<>();
		try {
			while ((line = reader.readLine()) != null) {
				URL levelUrl = this.getClass().getClassLoader().getResource(line);
				if (levelUrl != null) {
					levels.addLast(levelUrl);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not read level file!");
		}
		return levels;
	}

	/**
	 * Load a new level from file into this model. The file must contain a rectangular 2D-representation of the map.
	 *
	 * @param stream InputStream of the file to load
	 */
	private void loadLevel(String fileName, InputStream stream) throws Exception {

		doorManager.clear();
		moveableEvents.clear();
		targets.clear();
		pushables.clear();
		Teleporter.clear();
		if (stream != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				Tile[][] tmpMap = new Tile[MAX_ROWS][MAX_COLS];
				int maxLineWidth = 0;
				int iLine = 0;
				String line;
				while ((line = reader.readLine()) != null) {
					int lineWidth = MAX_COLS < line.length() ? MAX_COLS : line.length();
					if (lineWidth > maxLineWidth) {
						maxLineWidth = lineWidth;
					}
					for (int i = 0; i < lineWidth; i++) {
						char c = line.charAt(i);

						// Wall
						if (c == '#')
							addWall(tmpMap, i, iLine);
							// Passage
						else if (c == ' ')
							addPassage(tmpMap, i, iLine);
							// Target
						else if (c == '.')
							addTarget(tmpMap, i, iLine);
							// Box
						else if (c == '$')
							addBox(i, iLine);
							// Player
						else if (c == '@')
							addPlayer(i, iLine);
							// Target with box
						else if (c == '*')
							addTargetWithBox(tmpMap, i, iLine);
							// Target with player
						else if (c == '+')
							addTargetWithPlayer(tmpMap, i, iLine);
							// Rock
						else if (c == 'R')
							addRock(i, iLine);
							// Cracked floor
						else if (Character.isDigit(c))
							addCrackedFloor(tmpMap, i, iLine, c);
							// Rutting
						else if (c == 'N' || c == 'O' || c == 'S' || c == 'W')
							addRutting(tmpMap, i, iLine, c);
							// OR-Type door
						else if (c == 'A' || c == 'B' || c == 'C')
							addDoor(tmpMap, i, iLine, c, ButtonDoorGroupType.OR);
							// OR-Type button
						else if (c == 'a' || c == 'b' || c == 'c')
							addButton(tmpMap, i, iLine, c, ButtonDoorGroupType.OR);
							// PARITY-Type door
						else if (c == 'D' || c == 'E' || c == 'F')
							addDoor(tmpMap, i, iLine, c, ButtonDoorGroupType.PARITY);
							// PARITY-Type button
						else if (c == 'd' || c == 'e' || c == 'f')
							addButton(tmpMap, i, iLine, c, ButtonDoorGroupType.PARITY);
							// Player teleporter
						else if (c == 'T' || c == 'U' || c == 'V')
							addTeleporter(tmpMap, iLine, i, c, Teleporter.Type.PLAYER);
							// Object teleporter
						else if (c == 'X' || c == 'Y' || c == 'Z')
							addTeleporter(tmpMap, iLine, i, c, Teleporter.Type.OBJECT);
					}
					iLine++;
					if (iLine >= MAX_ROWS) {
						break;
					}
				}
				reader.close();
				stream.close();

				levelWidth = maxLineWidth;
				levelHeight = iLine;

				if (!Teleporter.checkPairs()) {
					throw new Exception("Not all teleporters do have a counterpart!");
				}

				// Copy tmpMap to map to fill empty fields with walls and reduce size.
				map = new Tile[levelHeight][levelWidth];
				for (int x = 0; x < levelWidth; x++) {
					for (int y = 0; y < levelHeight; y++) {
						map[y][x] = tmpMap[y][x];
						if (map[y][x] == null) {
							addPassage(map, x, y);
						}
					}
				}
			} catch (IOException e) {
				throw new Exception("Could not read from file " + fileName + "!");
			}
		}
	}

	private void addWall(Tile[][] map, int x, int y) {

		map[y][x] = new Wall();
	}

	private void addPassage(Tile[][] map, int x, int y) {

		map[y][x] = new EmptyPassage();
	}

	private void addTarget(Tile[][] map, int x, int y) {

		Target target = new Target(false);
		map[y][x] = target;
		targets.add(target);
	}

	private void addBox(int x, int y) {

		Box box = new Box(new Vector(x, y));
		pushables.put(box.getPosition(), box);
	}

	private void addPlayer(int x, int y) {

		player = new Player(new Vector(x, y));
	}

	private void addTargetWithBox(Tile[][] map, int x, int y) {

		addBox(x, y);

		Target target = new Target(true);
		map[y][x] = target;
		targets.add(target);
	}

	private void addTargetWithPlayer(Tile[][] map, int x, int y) {

		addPlayer(x, y);

		addTarget(map, x, y);
	}

	private void addRock(int x, int y) {

		Rock rock = new Rock(new Vector(x, y));
		pushables.put(rock.getPosition(), rock);
	}

	private void addCrackedFloor(Tile[][] map, int x, int y, char c) {

		map[y][x] = new CrackedFloor(Integer.valueOf(String.valueOf(c)));
	}

	private void addRutting(Tile[][] map, int x, int y, char c) {

		map[y][x] = new Rutting(Direction.getDirection(String.valueOf(c)));
	}

	private void addDoor(Tile[][] map, int x, int y, char c, ButtonDoorGroupType parity) {

		Door door = new Door(new Vector(x, y));
		map[y][x] = door;
		doorManager.addDoor(c, door, parity);
	}

	private void addButton(Tile[][] map, int x, int y, char c, ButtonDoorGroupType type) {

		Button button = new Button();
		map[y][x] = button;
		doorManager.addButton(c, button, type);
	}

	private void addTeleporter(Tile[][] map, int y, int x, char c, Teleporter.Type type) {

		map[y][x] = new Teleporter(c, type, new Vector(x, y));
	}

	/**
	 * Load the next level from the level list.
	 * End the game if there is no level left or if there was an error:
	 * <ol>
	 * <li>If the finished level was the last one, broadcast this info.</li>
	 * <li>If this method is called again afterwards, broadcast quit signal.</li>
	 * </ol>
	 */
	public void nextLevel() {

		if (curLevelIndex == levels.size()) {
			broadcastGameFinished();
			curLevelIndex++;
			return;
		}

		if (curLevelIndex > levels.size()) {
			broadcastQuit();
			return;
		}

		URL levelUrl = levels.get(curLevelIndex);
		try {
			loadLevel(levelUrl.getFile(), levelUrl.openStream());

			message = "Good luck!";
			curLevelIndex++;

			broadcastRepaintLevel();
		} catch (Exception e) {
			System.out.println("Could not read level '" + levelUrl.getFile() + "'!");

			broadcastGameFinished();
		}

	}

	/**
	 * Reload the current level in order to reset it
	 */
	public void resetLevel() {

		URL levelUrl = levels.get(curLevelIndex - 1);
		try {
			loadLevel(levelUrl.getFile(), levelUrl.openStream());

			message = "Good luck!";

			broadcastRepaintLevel();
		} catch (Exception e) {
			System.out.println("Could not read level '" + levelUrl.getFile() + "'!");

			broadcastGameFinished();
		}
	}

	/**
	 * Move the player one tile into movement direction
	 *
	 * @param movement
	 */
	public void movePlayer(Direction movement) {

		Vector oldPos = player.getPosition();
		Vector newPos = oldPos.add(movement.getCoords());
		Vector pushPos = oldPos.add(movement.getCoords().mul(2));

		// Is there a pushable at the new position?
		if (pushables.containsKey(newPos) && getTile(newPos).isPassable(player, movement)) {
			if (pushables.containsKey(pushPos)) {
				// Two pushables cannot be pushed
				broadcastMessageUpdate("I am not strong enough to push more than one!");
				return;
			} else {
				// No second pushable behind the first. Can it be pushed?
				if (getTile(pushPos).isPassable(pushables.get(newPos), movement)) {

					// Move the moveable and broadcast movement and tile changes
					pushables.get(newPos).push(movement);
					Pushable pushable = pushables.remove(newPos);

					if (getTile(newPos).leave(pushable))
						broadcastTileUpdate(newPos);
					if (getTile(pushPos).walkBy(pushable))
						broadcastTileUpdate(pushPos);

					pushables.put(pushable.getPosition(), pushable);
					broadcastNewMoveableEvent(newPos, pushable.getPosition(), pushable);

					// Move the player and broadcast movement and tile changes
					player.setPosition(newPos);
					if (getTile(oldPos).leave(player))
						broadcastTileUpdate(oldPos);
					if (getTile(newPos).walkBy(player))
						broadcastTileUpdate(newPos);

					broadcastPlayerUpdate();
					broadcastMessageUpdate("");
				} else {
					broadcastMessageUpdate("Something is blocking the way!");
					return;
				}
			}
		} else {
			if (getTile(newPos).isPassable(player, movement)) {

				// Move player and broadcast tile changes
				player.setPosition(newPos);
				if (getTile(oldPos).leave(player))
					broadcastTileUpdate(oldPos);
				if (getTile(newPos).walkBy(player))
					broadcastTileUpdate(newPos);

				broadcastPlayerUpdate();
				broadcastMessageUpdate("");
			} else {
				broadcastMessageUpdate("I can't walk there!");
			}
		}

		// Update doors and broadcast changes
		updateDoors().forEach(updatedDoorPos -> broadcastTileUpdate(updatedDoorPos));

		// Level complete? Broadcast it!
		if (checkTargets()) {
			broadcastLevelComplete();
		}

	}

	/**
	 * Are all targets filled with boxes?
	 *
	 * @return
	 */
	private boolean checkTargets() {

		boolean allFinished = targets.stream().allMatch(target -> target.isFilled());
		return allFinished;
	}

	/**
	 * Check all door mechanisms to open or close doors
	 *
	 * @return A list of positions of the doors that changed their state
	 */
	private List<Vector> updateDoors() {

		List<Vector> updatedDoorsPos = doorManager.updateDoors();
		return updatedDoorsPos;
	}

	/**
	 * Informs all registered views about a tile update.
	 *
	 * @param pos Position of the updated tile
	 */
	private void broadcastTileUpdate(Vector pos) {

		views.forEach(view -> view.updateTile(pos));
	}

	/**
	 * Registers a new moveable event and informs all registered views about it
	 */
	private void broadcastNewMoveableEvent(Vector oldPos, Vector newPos, Moveable moveable) {

		moveableEvents.addFirst(new Event(oldPos, newPos, moveable));
		views.forEach(view -> view.updateMoveable());
	}

	/**
	 * Informs all registered views about a player movement.
	 */
	private void broadcastPlayerUpdate() {

		views.forEach(view -> view.updatePlayer());
	}

	/**
	 * Changes the current message and informs all registered views about it.
	 */
	private void broadcastMessageUpdate(String newMessage) {

		message = newMessage;
		views.forEach(view -> view.updateMessage());
	}

	/**
	 * Informs all registered views that they have to render a new level.
	 */
	private void broadcastRepaintLevel() {

		views.forEach(view -> view.repaintLevel());
	}

	/**
	 * Informs all registered views that the last level has been finished.
	 */
	private void broadcastGameFinished() {

		views.forEach(view -> view.gameFinished());
	}

	/**
	 * Tells all views to quit the game
	 */
	private void broadcastQuit() {

		views.forEach(view -> view.quit());
	}

	/**
	 * Informs all registered views that the level is finished.
	 */
	private void broadcastLevelComplete() {

		views.forEach(view -> view.levelCompleted());
	}

	@Override public void registerView(AdvancedTileBoardView view) {

		views.add(view);
	}

	@Override public void unregisterView(AdvancedTileBoardView view) {

		views.remove(view);
	}

	@Override public int getLevelWidth() {

		return levelWidth;
	}

	@Override public int getLevelHeight() {

		return levelHeight;
	}

	public Tile getTile(Vector pos) {

		return map[pos.getY()][pos.getX()];
	}

	@Override public Vector getPlayerPos() {

		return player.getPosition();
	}

	@Override public List<Moveable> getMoveables() {

		return new ArrayList<>(pushables.values());
	}

	@Override public Event getLatestMoveableEvent() {

		return moveableEvents.getFirst();
	}

	@Override public String getMessage() {

		return message;
	}

	@Override public int getCurrentLevelIndex() {

		return curLevelIndex;
	}
}
