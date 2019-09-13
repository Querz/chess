package net.querz.chess.io.bits;

import net.querz.chess.ChessBoard;
import net.querz.chess.ChessIO;
import net.querz.chess.FigureFactory;
import net.querz.chess.figure.Bishop;
import net.querz.chess.figure.Color;
import net.querz.chess.figure.Figure;
import net.querz.chess.figure.Knight;
import net.querz.chess.figure.Pawn;
import net.querz.chess.figure.Rook;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ChessLoader implements ChessIO {

	/*
	* 0      --> empty  1 bit
	* 1c0    --> pawn   3 bits
	* 1c100  --> rook   5 bits
	* 1c101  --> knight 5 bits
	* 1c110  --> bishop 5 bits
	* 1c1110 --> queen  6 bits
	* 1c1111 --> king   6 bits
	*
	* c is either 1 or 0 for the color of the figure
	*
	* 1 bit: turn
	* 7 bits: 50-move-rule
	* 1 bit to signal the following 6 bits exist
	* 3 bits for x-coordinate of en-passant pawn
	* 1 bit for y-row (an en-passant pawn is always either on row 4 or 5)
	* 64 times the following:
	*   - 1-6 bits for figure type (see figure ids above)
	*   - 1 bit if the figure is a rook for castling
	*
	* ------------------------------------------
	* result: 181 bits for base setup (23 bytes)
	*         77 bits for empty board (10 bytes)
	*         397 bits for 64 kings   (50 bytes)
	* ------------------------------------------
	*
	* Warning: This is not a lossless format. It will work with all chess rules
	*          this program can handle, but might not work correctly with
	*          custom setups.
	* */

	@Override
	public void load(byte[] data, ChessBoard board) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		try (BitReader br = new BitReader(bais)) {
			byte turn = br.readBits(1);
			board.set50MoveRuleTurns(br.readBits(7));
			board.setCurrenTurn(turn + 2); // 1 --> white, 0 --> black
			byte hasEnPassant = br.readBits(1);
			byte enPassantX = br.readBits(3);
			byte enPassantY = (byte) (br.readBits(1) == 0 ? 3 : 4);
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					readFigureHuffman(x, y, br, board);
				}
			}
			if (hasEnPassant == 1) {
				Figure figure = board.getField(enPassantX, enPassantY).getFigure();
				figure.setFirstTurn(turn + 1);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void readFigureHuffman(int x, int y, BitReader br, ChessBoard board) throws IOException {
		if (br.readBits(1) == 0) {
			return;
		}
		Color color =  br.readBits(1) == 1 ? Color.BLACK : Color.WHITE;
		int msb = br.readBits(1);
		if (msb != 0) {
			msb = msb << 2 | br.readBits(2);
			if (msb == 7) {
				msb = msb << 1 | br.readBits(1);
			}
		}
		int turn = 0;
		if (msb == 0b100) {
			turn = br.readBits(1);
		}
		if (turn == 0 && msb != 0b0) {
			turn = -1;
		}
		if (msb == 0b0 && (color == Color.BLACK && y == 1 || color == Color.WHITE && y == 6)) {
			// we will set all pawns that are on the base position to -1 and all others to 0
			turn = -1;
		}
		Figure figure = FigureFactory.createFigure(color, msb, x, y, turn);
		board.setFigure(figure);
	}

	@Override
	public byte[] save(ChessBoard board) {
		List<Figure> figures = board.getFigures();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (BitWriter bw = new BitWriter(baos)) {
			bw.writeBits((byte) (board.getCurrentTurn() % 2), 1);
			int fifty = board.get50MoveRuleTurns();
			bw.writeBits((byte) (fifty > 100 ? 100 : fifty), 7);
			Figure enPassantPawn = null;
			for (Figure figure : figures) {
				if (figure instanceof Pawn && board.getCurrentTurn() - figure.getFirstTurn() == 1) {
					if (figure.getColor() == Color.BLACK && figure.getY() == 3
							|| figure.getColor() == Color.WHITE && figure.getY() == 4) {
						enPassantPawn = figure;
						break;
					}
				}
			}
			if (enPassantPawn == null) {
				bw.writeBits((byte) 0, 5);
			} else {
				bw.writeBits((byte) 1, 1);
				bw.writeBits((byte) enPassantPawn.getX(), 3);
				// only write one bit, en-passant pawns can only stand on either row 4 or 5
				bw.writeBits((byte) (enPassantPawn.getY() == 3 ? 0 : 1), 1);
			}
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					writeFigureHuffman(board.getField(x, y).getFigure(), bw);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return baos.toByteArray();
	}

	private void writeFigureHuffman(Figure figure, BitWriter bw) throws IOException {
		if (figure == null) {
			bw.writeBits((byte) 0, 1);
			return;
		}
		bw.writeBits((byte) 1, 1);
		bw.writeBits((byte) (figure.getColor() == Color.BLACK ? 1 : 0), 1);
		if (figure instanceof Pawn){
			bw.writeBits((byte) FigureFactory.getFigureID(figure), 1);
		} else if (figure instanceof Rook || figure instanceof Knight || figure instanceof Bishop) {
			bw.writeBits((byte) FigureFactory.getFigureID(figure), 3);
		} else {
			bw.writeBits((byte) FigureFactory.getFigureID(figure), 4);
		}
		if (figure instanceof Rook) {
			if (figure.getField().getBoard().getKing(figure.getColor()).getFirstTurn() != -1) {
				bw.writeBits((byte) 1, 1);
			} else {
				bw.writeBits((byte) (figure.getFirstTurn() != -1 ? 1 : 0), 1);
			}
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
