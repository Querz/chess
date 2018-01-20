package net.querz.chess.figure;

import net.querz.chess.ChessField;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Figure {

	public Bishop(Color color, ChessField field) {
		super(color, "bishop", field);
	}

	@Override
	public List<ChessField> getAccessibleFields() {
		List<ChessField> fields = new ArrayList<>();
		for (int i = 1; x + i < 8 && y + i < 8; i++)
			if (addField(x + i, y + i, fields))
				break;
		for (int i = 1; x + i < 8 && y - i >= 0; i++)
			if (addField(x + i, y - i, fields))
				break;
		for (int i = 1; x - i >= 0 && y + i < 8; i++)
			if (addField(x - i, y + i, fields))
				break;
		for (int i = 1; x - i >= 0 && y - i >= 0; i++)
			if (addField(x - i, y - i, fields))
				break;
		return fields;
	}

	private boolean addField(int x, int y, List<ChessField> fields) {
		ChessField field = this.field.getBoard().getField(x, y);
		if (field != null) {
			if (field.getFigure() == null) {
				fields.add(field);
				return false;
			} else if (field.getFigure().color != color) {
				fields.add(field);
			}
		}
		return true;
	}
}
