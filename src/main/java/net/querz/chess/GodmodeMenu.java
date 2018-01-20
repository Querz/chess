package net.querz.chess;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.querz.chess.figure.*;
import java.util.HashMap;
import java.util.Map;

public class GodmodeMenu extends ContextMenu {

	private ChessField field;

	public GodmodeMenu(ChessField field) {
		this.field = field;
		getItems().add(new GodItem("images/remove.png", "Remove Figure", e -> setFigure(null)));
		getItems().add(new GodItem("images/white_queen.png", "Add White Queen", e -> setFigure(new Queen(Color.WHITE, field))));
		getItems().add(new GodItem("images/white_bishop.png", "Add White Bishop", e -> setFigure(new Bishop(Color.WHITE, field))));
		getItems().add(new GodItem("images/white_knight.png", "Add White Knight", e -> setFigure(new Knight(Color.WHITE, field))));
		getItems().add(new GodItem("images/white_rook.png", "Add White Rook", e -> setFigure(new Rook(Color.WHITE, field))));
		getItems().add(new GodItem("images/white_king.png", "Add White King", e -> setFigure(new King(Color.WHITE, field))));
		getItems().add(new GodItem("images/white_pawn.png", "Add White Pawn", e -> setFigure(new Pawn(Color.WHITE, field))));
		getItems().add(new GodItem("images/black_queen.png", "Add Black Queen", e -> setFigure(new Queen(Color.BLACK, field))));
		getItems().add(new GodItem("images/black_bishop.png", "Add Black Bishop", e -> setFigure(new Bishop(Color.BLACK, field))));
		getItems().add(new GodItem("images/black_knight.png", "Add Black Knight", e -> setFigure(new Knight(Color.BLACK, field))));
		getItems().add(new GodItem("images/black_rook.png", "Add Black Rook", e -> setFigure(new Rook(Color.BLACK, field))));
		getItems().add(new GodItem("images/black_king.png", "Add Black King", e -> setFigure(new King(Color.BLACK, field))));
		getItems().add(new GodItem("images/black_pawn.png", "Add Black Pawn", e -> setFigure(new Pawn(Color.BLACK, field))));
	}

	private void setFigure(Figure figure) {
		field.setFigure(figure, true);
		field.getBoard().recalculateAttackedFields();
		field.getBoard().gameStateTest();
	}

	private static class GodItem extends MenuItem {

		static Map<String, Image> cachedIcons = new HashMap<>();

		GodItem(String resourceIcon, String text, EventHandler<ActionEvent> event) {
			if (!cachedIcons.containsKey(resourceIcon)) {
				cachedIcons.put(resourceIcon, Helper.loadImage(resourceIcon, 16, 16));
			}
			setGraphic(new ImageView(cachedIcons.get(resourceIcon)));
			setText(text);
			setOnAction(event);
		}
	}
}
