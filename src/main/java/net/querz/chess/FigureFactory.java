package net.querz.chess;

import net.querz.chess.figure.*;

public class FigureFactory {

	private FigureFactory() {}

	public static Figure createFigure(Color color, String type, String pos, int firstTurn) {
		if (pos.length() != 2) {
			throw new IllegalArgumentException("Invalid position: \"" + pos + "\"");
		}
		int x = pos.toLowerCase().charAt(0) - 97;
		int y = 7 - (pos.charAt(1) - 49);
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			throw new IllegalArgumentException("Position not on board: \"" + pos + "\"");
		}
		Figure f;
		switch (type.toLowerCase()) {
			case "rook":
				f = new Rook(color, ChessGame.getBoard().getField(x, y));
				break;
			case "knight":
				f = new Knight(color, ChessGame.getBoard().getField(x, y));
				break;
			case "bishop":
				f = new Bishop(color, ChessGame.getBoard().getField(x, y));
				break;
			case "queen":
				f = new Queen(color, ChessGame.getBoard().getField(x, y));
				break;
			case "king":
				f = new King(color, ChessGame.getBoard().getField(x, y));
				break;
			case "pawn":
				f = new Pawn(color, ChessGame.getBoard().getField(x, y));
				break;
			default:
				throw new IllegalArgumentException("Unknown Figure: \"" + type + "\"");
		}
		f.setFirstTurn(firstTurn);
		return f;
	}

	public static Figure createFigure(Color color, int type, int x, int y, int firstTurn) {
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			throw new IllegalArgumentException("Position not on board: \"" + x + "/" + y + "\"");
		}
		Figure f;
		switch (type) {
			case 1:
				f = new Rook(color, ChessGame.getBoard().getField(x, y));
				break;
			case 2:
				f = new Knight(color, ChessGame.getBoard().getField(x, y));
				break;
			case 3:
				f = new Bishop(color, ChessGame.getBoard().getField(x, y));
				break;
			case 4:
				f = new Queen(color, ChessGame.getBoard().getField(x, y));
				break;
			case 5:
				f = new King(color, ChessGame.getBoard().getField(x, y));
				break;
			case 0:
				f = new Pawn(color, ChessGame.getBoard().getField(x, y));
				break;
			default:
				throw new IllegalArgumentException("Unknown Figure: \"" + type + "\"");
		}
		f.setFirstTurn(firstTurn);
		return f;
	}

	public static int getFigureID(Figure f) {
		if (f instanceof Pawn)
			return 0;
		if (f instanceof Rook)
			return 1;
		if (f instanceof Knight)
			return 2;
		if (f instanceof Bishop)
			return 3;
		if (f instanceof Queen)
			return 4;
		if (f instanceof King)
			return 5;
		return 0;
	}
}
