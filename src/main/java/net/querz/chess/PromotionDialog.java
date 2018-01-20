package net.querz.chess;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import net.querz.chess.figure.*;

public class PromotionDialog extends Dialog<Figure> {

	private Figure selectedFigure;

	public PromotionDialog(Pawn pawn) {
		setTitle("Promote Pawn " + pawn.getColor().getName());
		setResultConverter(f -> selectedFigure);
		HBox hbox = new HBox();
		hbox.getChildren().add(new PromotionCandidateLabel(new Queen(pawn.getColor(), null)));
		hbox.getChildren().add(new PromotionCandidateLabel(new Knight(pawn.getColor(), null)));
		hbox.getChildren().add(new PromotionCandidateLabel(new Rook(pawn.getColor(), null)));
		hbox.getChildren().add(new PromotionCandidateLabel(new Bishop(pawn.getColor(), null)));
		getDialogPane().setContent(hbox);
	}

	private class PromotionCandidateLabel extends Label {

		Figure figure;

		PromotionCandidateLabel(Figure figure) {
			setGraphic(figure.getImageView());
			this.figure = figure;
			setOnMouseReleased(this::onMouseReleased);
		}

		private void onMouseReleased(MouseEvent e) {
			selectedFigure = figure;
			getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
			close();
			e.consume();
		}
	}
}
