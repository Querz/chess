package net.querz.chess;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.querz.chess.json.ChessLoader;
import java.io.File;

public class ChessGame extends Application {

	private Label status;
	private ChessBoard board;
	private static ChessGame instance;
	private boolean godmode = false;

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		godmode = getParameters().getUnnamed().contains("--godmode");

		primaryStage.setTitle("Chess");
		primaryStage.getIcons().add(Helper.loadImage("images/icon.png", 16, 16));

		//main BorderPane
		BorderPane pane = new BorderPane();

		//chess board with column and row markings
		GridPane table = new GridPane();
		for (int i = 0; i < 8; i++) {
			table.add(newRowLabel(i), 0, i + 1, 1, 1);
			table.add(newRowLabel(i), 9, i + 1, 1, 1);
			table.add(newColLabel(i), i + 1, 0, 1, 1);
			table.add(newColLabel(i), i + 1, 9, 1, 1);
		}
		table.add(board = new ChessBoard(), 1, 1, 8, 8);
		table.setAlignment(Pos.CENTER);
		pane.setCenter(table);

		//menu on the bottom with status text and option buttons
		BorderPane menu = new BorderPane();
		menu.setPadding(new Insets(10, 10, 10, 0));

		//option buttons
		HBox options = new HBox();
		options.setAlignment(Pos.BOTTOM_RIGHT);

		//secret option buttons
		if (godmode) {
			options.getChildren().add(new OptionButton(
					"images/clear.png",
					e -> board.clear())
			);
			options.getChildren().add(new OptionButton(
					"images/swap.png",
					e -> board.nextTurn()
			));
		}
		options.getChildren().add(new OptionButton(
				"images/reset.png",
				e -> {
					board.clear();
					board.loadFromResource("init.json");
				}
		));
		options.getChildren().add(new OptionButton(
				"images/save.png",
				e -> {
					File file = createFileChooser().showSaveDialog(primaryStage);
					if (file != null) {
						board.save(file);
					}
				}
		));
		options.getChildren().add(new OptionButton(
				"images/load.png",
				e -> {
					File file = createFileChooser().showOpenDialog(primaryStage);
					if (file != null) {
						board.load(file);
					}
				}
		));
		menu.setRight(options);

		//status text
		status = new Label();
		status.setAlignment(Pos.BOTTOM_LEFT);
		status.setPadding(new Insets(10, 0, 10, 10));
		menu.setLeft(status);

		pane.setBottom(menu);

		//scene
		Scene scene = new Scene(pane, 440, 490);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setMinWidth(primaryStage.getWidth());
		primaryStage.setMinHeight(primaryStage.getHeight());

		//set IO and load initial setup
		board.setIO(new ChessLoader());
		board.loadFromResource("init.json");
	}

	private Label newRowLabel(int i) {
		Label l = new Label(8 - i + "");
		l.setMinSize(20, 50);
		l.setAlignment(Pos.CENTER);
		return l;
	}

	private Label newColLabel(int i) {
		Label l = new Label((char) (i + 65) + "");
		l.setMinSize(50, 20);
		l.setAlignment(Pos.CENTER);
		return l;
	}

	private FileChooser createFileChooser() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter extFilter = new ExtensionFilter("JSON files (*.json)", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		return fileChooser;
	}

	static void displayStatusText(String text) {
		instance.status.setText(text);
	}

	static boolean isGodmode() {
		return instance.godmode;
	}

	public static ChessBoard getBoard() {
		return instance.board;
	}
}
