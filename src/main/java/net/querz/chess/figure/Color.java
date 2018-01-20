package net.querz.chess.figure;

import java.io.Serializable;

public enum Color implements Serializable {

	BLACK("black"),
	WHITE("white");

	private String name;

	Color(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getFancyName() {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public Color revert() {
		return this == Color.BLACK ? Color.WHITE : Color.BLACK;
	}
}
