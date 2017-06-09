package sb;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sb.controller.SokobenchoController;
import sb.model.Level;
import sb.view.ControlsInfoView;
import sb.view.SokobenchoView;

/**
 * The game's executable main class.
 */
public class Sokobencho extends Application {

	@Override public void start(Stage primaryStage) throws Exception {

		// Wire the MVC structure
		Level level = new Level();
		SokobenchoController controller = new SokobenchoController(level);
		SokobenchoView view = new SokobenchoView(level, controller);

		// Create the scene and init model (and view implicitly)
		Scene scene = new Scene(view);
		primaryStage.setScene(scene);
		level.nextLevel();

		// Show the window
		primaryStage.setTitle("Sokobencho");
		primaryStage.show();

		// Show info window
		Stage controls = new Stage();
		controls.setScene(new Scene(new ControlsInfoView()));
		controls.show();

		view.requestFocus();
	}

	public static void main(String[] args) {

		launch(args);
	}
}
