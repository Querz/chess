package net.querz.chess.io.nbt;

import net.querz.chess.ChessBoard;
import net.querz.chess.ChessIO;
import net.querz.chess.FigureFactory;
import net.querz.chess.figure.Color;
import net.querz.chess.figure.Figure;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.Tag;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ChessLoader implements ChessIO {

	@Override
	public void load(byte[] data, ChessBoard board) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		try (DataInputStream dis = new DataInputStream(new GZIPInputStream(bais))) {
			CompoundTag root = (CompoundTag) Tag.deserialize(dis, 0);
			loadFigures(Color.BLACK, root.getListTag("black").asCompoundTagList(), board);
			loadFigures(Color.WHITE, root.getListTag("white").asCompoundTagList(), board);
			board.setCurrenTurn(root.getInt("currentTurn"));
			board.set50MoveRuleTurns(root.getInt("50MoveRuleTurns"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void loadFigures(Color color, ListTag<CompoundTag> nbtFigures, ChessBoard board) {
		for (CompoundTag nbtFigure : nbtFigures) {
			String type = nbtFigure.getString("type");
			String pos = nbtFigure.getString("pos");
			int firstTurn = nbtFigure.getInt("firstTurn");
			Figure figure = FigureFactory.createFigure(color, type, pos, firstTurn);
			board.setFigure(figure);
		}
	}

	@Override
	public byte[] save(ChessBoard board) {
		CompoundTag root = new CompoundTag();
		root.putInt("currentTurn", board.getCurrentTurn());
		root.putInt("50MoveRuleTurns", board.get50MoveRuleTurns());
		ListTag<CompoundTag> black = new ListTag<>();
		ListTag<CompoundTag> white = new ListTag<>();
		for (Figure figure : board.getFigures()) {
			CompoundTag nbtFigure = new CompoundTag();
			nbtFigure.putString("type", figure.getName());
			nbtFigure.putString("pos", figure.getPos());
			nbtFigure.putInt("firstTurn", figure.getFirstTurn());
			if (figure.getColor() == Color.BLACK) {
				black.add(nbtFigure);
			} else {
				white.add(nbtFigure);
			}
		}
		root.put("black", black);
		root.put("white", white);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(baos))) {
			root.serialize(dos, 0);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return baos.toByteArray();
	}

	@Override
	public String getFileTypeDescription() {
		return "NBT file (*.nbt)";
	}

	@Override
	public String getFileExtension() {
		return "nbt";
	}
}
