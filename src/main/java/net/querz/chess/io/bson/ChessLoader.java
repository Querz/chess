package net.querz.chess.io.bson;

import net.querz.chess.ChessBoard;
import net.querz.chess.ChessIO;
import net.querz.chess.FigureFactory;
import net.querz.chess.figure.Color;
import net.querz.chess.figure.Figure;
import org.bson.BSONObject;
import org.bson.BasicBSONCallback;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONEncoder;
import org.bson.BasicBSONObject;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.OutputBuffer;
import org.bson.types.BasicBSONList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ChessLoader implements ChessIO {

	@Override
	public void load(byte[] data, ChessBoard board) {
		BSONObject main;
		ByteArrayInputStream baos = new ByteArrayInputStream(data);
		try (GZIPInputStream gzip = new GZIPInputStream(baos)) {
			BasicBSONCallback bsonCallback = new BasicBSONCallback();
			BasicBSONDecoder decoder = new BasicBSONDecoder();
			decoder.decode(gzip, bsonCallback);
			main = (BSONObject) bsonCallback.get();
			board.setCurrenTurn((int) main.get("current_turn"));
			board.set50MoveRuleTurns((int) main.get("50_move_rule_turns"));
			loadFigures(Color.BLACK, (BasicBSONList) main.get("black"), board);
			loadFigures(Color.WHITE, (BasicBSONList) main.get("white"), board);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void loadFigures(Color color, BasicBSONList bsonFigures, ChessBoard board) {
		for (Object objectFigure : bsonFigures) {
			BasicBSONObject bsonFigure = (BasicBSONObject) objectFigure;
			int type = bsonFigure.getInt("type");
			int x = bsonFigure.getInt("x");
			int y = bsonFigure.getInt("y");
			int firstTurn = bsonFigure.getInt("first_turn");
			board.setFigure(FigureFactory.createFigure(color, type, x, y, firstTurn));
		}
	}

	@Override
	public byte[] save(ChessBoard board) {
		BasicBSONObject main = new BasicBSONObject();
		main.append("current_turn", board.getCurrentTurn());
		main.append("50_move_rule_turns", board.get50MoveRuleTurns());
		BasicBSONList white = new BasicBSONList();
		BasicBSONList black = new BasicBSONList();
		for (Figure figure : board.getFigures()) {
			BasicBSONObject bsonFigure = new BasicBSONObject();
			bsonFigure.append("type", FigureFactory.getFigureID(figure));
			bsonFigure.append("x", figure.getX());
			bsonFigure.append("y", figure.getY());
			bsonFigure.append("first_turn", figure.getFirstTurn());
			if (figure.getColor() == Color.BLACK) {
				black.add(bsonFigure);
			} else {
				white.add(bsonFigure);
			}
		}
		main.append("black", black);
		main.append("white", white);
		BasicBSONEncoder encoder = new BasicBSONEncoder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputBuffer outputBuffer = new BasicOutputBuffer();
		encoder.set(outputBuffer);
		encoder.putObject(main);
		encoder.done();
		try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
			outputBuffer.pipe(gzip);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return baos.toByteArray();
	}

	@Override
	public String getFileTypeDescription() {
		return "BSON files (*.bson)";
	}

	@Override
	public String getFileExtension() {
		return "bson";
	}
}
