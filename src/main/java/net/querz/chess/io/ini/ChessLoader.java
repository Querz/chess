package net.querz.chess.io.ini;

import net.querz.chess.ChessBoard;
import net.querz.chess.ChessIO;
import net.querz.chess.FigureFactory;
import net.querz.chess.figure.Color;
import net.querz.chess.figure.Figure;
import java.util.Map;

public class ChessLoader implements ChessIO {

	@Override
	public void load(byte[] data, ChessBoard board) {
		Ini ini = new IniParser(new String(data)).read();
		board.setCurrenTurn(ini.getAsInt("current_turn"));
		board.set50MoveRuleTurns(ini.getAsInt("50_move_rule_turns"));
		for (Map.Entry<String, Map<String, String>> entry : ini.getSectionElements().entrySet()) {
			String pos = entry.getKey();
			String type = ini.get(pos, "type");
			int firstTurn = ini.getAsInt(pos, "first_turn");
			Color color = "white".equals(ini.get(pos, "color")) ? Color.WHITE : Color.BLACK;
			Figure figure = FigureFactory.createFigure(color, type, pos, firstTurn);
			board.setFigure(figure);
		}
	}

	@Override
	public byte[] save(ChessBoard board) {
		Ini ini = new Ini();
		ini.set("current_turn", board.getCurrentTurn());
		ini.set("50_move_rule_turns", board.get50MoveRuleTurns());
		for (Figure figure : board.getFigures()) {
			String pos = figure.getPos();
			ini.set(pos, "type", figure.getName());
			ini.set(pos, "first_turn", figure.getFirstTurn());
			ini.set(pos, "color", figure.getColor().getName());
		}
		return new IniWriter(ini).write().getBytes();
	}

	@Override
	public String getFileTypeDescription() {
		return "Ini files (*.ini)";
	}

	@Override
	public String getFileExtension() {
		return "ini";
	}
}
