package net.querz.chess.io.bits;

import net.querz.chess.ChessBoard;
import net.querz.chess.ChessIO;
import net.querz.chess.FigureFactory;
import net.querz.chess.figure.Color;
import net.querz.chess.figure.Figure;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ChessLoader implements ChessIO {

	//current turn: 16 bits unsigned short
	//50-move-rule: 16 bits unsigned short
	//number of black figures: 6 bits unsigned byte
	//first turn: 16 bits unsigned short
	//pos-x: 3 bits unsigned byte
	//pos-y: 3 bits unsigned byte
	//type: 3 bits unsigned byte
	//...
	//number of white figures: 6 bits unsigned byte
	//...

	@Override
	public void load(byte[] data, ChessBoard board) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		try (BitReader br = new BitReader(bais)) {
			board.setCurrenTurn(br.readUnsignedShort());
			board.set50MoveRuleTurns(br.readUnsignedShort());
			loadFigures(Color.BLACK, br, board);
			loadFigures(Color.WHITE, br, board);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void loadFigures(Color color, BitReader br, ChessBoard board) throws IOException {
		int i = br.readBits(6);
		for (int j = 0; j < i; j++) {
			int firstTurn = (int) br.readBits(8) << 8 | (int) br.readBits(8);
			int x = br.readBits(3);
			int y = br.readBits(3);
			int id = br.readBits(3);
			Figure figure = FigureFactory.createFigure(color, id, x, y, firstTurn);
			board.setFigure(figure);
		}
	}

	@Override
	public byte[] save(ChessBoard board) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (BitWriter bw = new BitWriter(baos)) {
			bw.writeShort(board.getCurrentTurn());
			bw.writeShort(board.get50MoveRuleTurns());
			writeFigures(board.getFigures(Color.BLACK), bw);
			writeFigures(board.getFigures(Color.WHITE), bw);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return baos.toByteArray();
	}

	private void writeFigures(List<Figure> figures, BitWriter bw) throws IOException {
		bw.writeBits((byte) figures.size(), 6);
		for (Figure f : figures) {
			int firstTurn = f.getFirstTurn();
			bw.writeBits((byte) (firstTurn >>> 8), 8);
			bw.writeBits((byte) (firstTurn & 0xFF), 8);
			bw.writeBits((byte) f.getX(), 3);
			bw.writeBits((byte) f.getY(), 3);
			bw.writeBits((byte) FigureFactory.getFigureID(f), 3);
		}
	}

	@Override
	public String getFileTypeDescription() {
		return "Chess data files (*.chess)";
	}

	@Override
	public String getFileExtension() {
		return "chess";
	}
}
