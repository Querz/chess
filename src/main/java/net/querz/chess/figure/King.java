package net.querz.chess.figure;

import net.querz.chess.ChessField;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class King extends Figure {

	public King(Color color, ChessField field) {
		super(color, "king", field);
	}

	@Override
	public List<ChessField> getAccessibleFields() {
		List<ChessField> fields = new ArrayList<>();
		Set<ChessField> attackedFields = field.getBoard().getAllAccessibleFields(color.revert());
		//check for fields that are not attacked and not occupied by own figures or enemy king
		for (int kx = -1; kx <= 1; kx++) {
			for (int ky = -1; ky <= 1; ky++) {
				ChessField f;
				if ((f = field.getBoard().getField(field.getX() + kx, field.getY() + ky)) != null
						&& f != field
						&& !attackedFields.contains(f)
						&& (f.getFigure() == null
						|| f.getFigure() != null && f.getFigure().color != color)) {
					fields.add(f);
				}
			}
		}
		//check for castling
		if (getFirstTurn() < 0) {
			ChessField rookField = field.getBoard().getField(7, y);
			if (rookField.getFigure() instanceof Rook && rookField.getFigure().getFirstTurn() < 0) {
				//check right castling
				boolean right = true;
				for (int i = x + 1; i < 7; i++) {
					ChessField current = field.getBoard().getField(i, y);
					if (current.getFigure() != null || attackedFields.contains(current)) {
						right = false;
						break;
					}
				}
				if (right) {
					fields.add(field.getBoard().getField(6, y));
				}
			}
			rookField = field.getBoard().getField(0, y);
			if (rookField.getFigure() instanceof Rook && rookField.getFigure().getFirstTurn() < 0) {
				//check left castling
				boolean left = true;
				for (int i = x - 1; i > 0; i--) {
					ChessField current = field.getBoard().getField(i, y);
					if (current.getFigure() != null || i > 1 && attackedFields.contains(current)) {
						left = false;
						break;
					}
				}
				if (left) {
					fields.add(field.getBoard().getField(2, y));
				}
			}
		}
		return fields;
	}

	public boolean isCheck() {
		return field.getBoard().getAllAccessibleFields(color.revert()).contains(field);
	}

	public boolean isCheckMate() {
		if (isCheck()) {
			for (Figure figure : field.getBoard().getFigures(color)) {
				if (figure.getAllAccessibleFields().size() > 0) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isStaleMate() {
		if (!isCheck()) {
			for (Figure figure : field.getBoard().getFigures(color)) {
				if (figure.getAllAccessibleFields().size() > 0) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Figure postTurnAction(ChessField oldField, ChessField newField, boolean graphic) {
		if (graphic && getFirstTurn() < 0) {
			if (x == 2) {
				field.getBoard().getField(0, y).getFigure().move(field.getBoard().getField(3, y), true);
			} else if (x == 6) {
				field.getBoard().getField(7, y).getFigure().move(field.getBoard().getField(5, y), true);
			}
		}
		return null;
	}
}
