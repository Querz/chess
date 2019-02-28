package net.querz.chess.io.json;

import com.google.gson.*;
import net.querz.chess.ChessBoard;
import net.querz.chess.ChessIO;
import net.querz.chess.FigureFactory;
import net.querz.chess.figure.Color;
import net.querz.chess.figure.Figure;

public class ChessLoader implements ChessIO {

	@Override
	public void load(byte[] json, ChessBoard board) {
		JsonObject element = new JsonParser().parse(new String(json)).getAsJsonObject();
		loadFigures(Color.BLACK, element.getAsJsonArray("black"), board);
		loadFigures(Color.WHITE, element.getAsJsonArray("white"), board);
		board.setCurrenTurn(element.get("current_turn").getAsInt());
		board.set50MoveRuleTurns(element.get("50_move_rule_turns").getAsInt());
	}

	private void loadFigures(Color color, JsonArray jsonFigures, ChessBoard board) {
		for (JsonElement jsonFigure : jsonFigures) {
			String type = jsonFigure.getAsJsonObject().get("type").getAsString();
			String pos = jsonFigure.getAsJsonObject().get("pos").getAsString();
			int firstTurn = jsonFigure.getAsJsonObject().get("first_turn").getAsInt();
			Figure figure = FigureFactory.createFigure(color, type, pos, firstTurn);
			board.setFigure(figure);
		}
	}

	@Override
	public byte[] save(ChessBoard board) {
		JsonObject main = new JsonObject();
		main.addProperty("current_turn", board.getCurrentTurn());
		main.addProperty("50_move_rule_turns", board.get50MoveRuleTurns());
		JsonArray black = new JsonArray();
		JsonArray white = new JsonArray();
		for (Figure figure : board.getFigures()) {
			JsonObject jsonFigure = new JsonObject();
			jsonFigure.addProperty("type", figure.getName());
			jsonFigure.addProperty("pos", figure.getPos());
			jsonFigure.addProperty("first_turn", figure.getFirstTurn());
			if (figure.getColor() == Color.BLACK) {
				black.add(jsonFigure);
			} else {
				white.add(jsonFigure);
			}
		}
		main.add("black", black);
		main.add("white", white);
		return new GsonBuilder().create().toJson(main).getBytes();
	}

	@Override
	public String getFileTypeDescription() {
		return "JSON files (*.json)";
	}

	@Override
	public String getFileExtension() {
		return "json";
	}
}
