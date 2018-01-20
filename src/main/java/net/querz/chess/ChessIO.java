package net.querz.chess;

public interface ChessIO {

	void load(String string, ChessBoard board);

	String save(ChessBoard board);
}
