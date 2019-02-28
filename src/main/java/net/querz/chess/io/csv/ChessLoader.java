package net.querz.chess.io.csv;

import net.querz.chess.ChessBoard;
import net.querz.chess.ChessIO;
import net.querz.chess.FigureFactory;
import net.querz.chess.figure.Color;
import net.querz.chess.figure.Figure;
import java.util.List;

public class ChessLoader implements ChessIO {
	//current_turn
	//50_moves_rule_turn
	//color,type,pos,first_turn

	@Override
	public void load(byte[] data, ChessBoard board) {
		String[] lines = new String(data).split("\n");
		board.setCurrenTurn(Integer.parseInt(lines[0]));
		board.set50MoveRuleTurns(Integer.parseInt(lines[1]));
		for (int i = 2; i < lines.length; i++) {
			String[] parts = lines[i].split(",");
			Color color = parts[0].equals("black") ? Color.BLACK : Color.WHITE;
			int firstTurn = Integer.parseInt(parts[3]);
			board.setFigure(FigureFactory.createFigure(color, parts[1], parts[2], firstTurn));
		}
	}

	@Override
	public byte[] save(ChessBoard board) {
		StringBuilder sb = new StringBuilder();
		sb.append(board.getCurrentTurn()).append('\n');
		sb.append(board.get50MoveRuleTurns()).append('\n');
		List<Figure> figures = board.getFigures();
		for (int i = 0; i < figures.size(); i++) {
			sb.append(figures.get(i).getColor().getName()).append(',');
			sb.append(figures.get(i).getName()).append(',');
			sb.append(figures.get(i).getPos()).append(',');
			sb.append(figures.get(i).getFirstTurn()).append(i < figures.size() ? "\n" : "");
		}
		return sb.toString().getBytes();
	}

	@Override
	public String getFileTypeDescription() {
		return "CSV files (*.csv)";
	}

	@Override
	public String getFileExtension() {
		return "csv";
	}
}
