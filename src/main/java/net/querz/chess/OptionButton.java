package net.querz.chess;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

class OptionButton extends Label {

	OptionButton(String imageResource, EventHandler<? super MouseEvent> mouseClickedEvent) {
		setGraphic(new ImageView(Helper.loadImage(imageResource, 30, 30)));
		setOnMouseClicked(mouseClickedEvent);
		setPadding(new Insets(0, 0, 0, 10));
	}
}
