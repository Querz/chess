package net.querz.chess.io.yaml;

import net.querz.chess.ChessBoard;
import net.querz.chess.ChessIO;
import net.querz.chess.FigureFactory;
import net.querz.chess.figure.Color;
import net.querz.chess.figure.Figure;
import org.yaml.snakeyaml.Yaml;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChessLoader implements ChessIO {

	@Override
	@SuppressWarnings("unchecked")
	public void load(byte[] data, ChessBoard board) {
		Yaml yaml = new Yaml();
		Map<String, Object> root = yaml.load(new String(data));
		board.setCurrenTurn(Integer.parseInt(root.get("current_turn").toString()));
		board.setCurrenTurn(Integer.parseInt(root.get("fifty_move_rule_turns").toString()));
		loadFigures(Color.BLACK, (List<Map<String, Object>>) root.get("black"), board);
		loadFigures(Color.WHITE, (List<Map<String, Object>>) root.get("white"), board);
	}

	private void loadFigures(Color color, List<Map<String, Object>> yamlFigures, ChessBoard board) {
		for (Map<String, Object> yamlFigure : yamlFigures) {
			int firstTurn = Integer.parseInt(yamlFigure.get("first_turn").toString());
			String pos = yamlFigure.get("pos").toString();
			String type = yamlFigure.get("type").toString();
			Figure figure = FigureFactory.createFigure(color, type, pos, firstTurn);
			board.setFigure(figure);
		}
	}

	@Override
	public byte[] save(ChessBoard board) {
		Map<String, Object> root = new LinkedHashMap<>();
		root.put("current_turn", board.getCurrentTurn());
		root.put("fifty_move_rule_turns", board.get50MoveRuleTurns());
		List<Map<String, Object>> black = new ArrayList<>();
		List<Map<String, Object>> white = new ArrayList<>();
		for (Figure figure : board.getFigures()) {
			Map<String, Object> mapFigure = new LinkedHashMap<>();
			mapFigure.put("type", figure.getName());
			mapFigure.put("pos", figure.getPos());
			mapFigure.put("first_turn", figure.getFirstTurn());
			if (figure.getColor() == Color.BLACK) {
				black.add(mapFigure);
			} else {
				white.add(mapFigure);
			}
		}
		root.put("black", black);
		root.put("white", white);
		return new Yaml().dump(root).getBytes();
	}

	@Override
	public String getFileTypeDescription() {
		return "YAML file (*.yml)";
	}

	@Override
	public String getFileExtension() {
		return "yml";
	}
}
