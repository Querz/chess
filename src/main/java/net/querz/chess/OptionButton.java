package net.querz.chess;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

class OptionButton extends Label {

	private ImageView icon;

	OptionButton(String imageResource, EventHandler<? super MouseEvent> mouseClickedEvent, String tooltip) {
		setAlignment(Pos.CENTER);
		icon = new ImageView(Helper.loadImage(imageResource, 70, 70));
		resizeIcon(30);
		setGraphic(icon);
		setOnMouseClicked(mouseClickedEvent);
		setOnMouseEntered(this::onMouseEntered);
		setOnMouseExited(this::onMouseExited);
		setMinHeight(35);
		setMaxHeight(35);
		setMinWidth(40);
		setMaxWidth(40);
		setTooltip(new Tooltip(tooltip));
	}

	private void onMouseEntered(MouseEvent e) {
		resizeIcon(35);
		e.consume();
	}

	private void onMouseExited(MouseEvent e) {
		resizeIcon(30);
		e.consume();
	}

	private void resizeIcon(int width) {
		icon.setPreserveRatio(true);
		icon.setFitWidth(width);
	}
}
