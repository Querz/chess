package net.querz.chess.figure;

import net.querz.chess.ChessField;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Figure {

	public Rook(Color color, ChessField field) {
		super(color, "rook", field);
	}

	@Override
	public List<ChessField> getAccessibleFields() {
		List<ChessField> fields = new ArrayList<>();
		for (int i = 1; x + i < 8; i++)
			if (addField(x + i, y, fields))
				break;
		for (int i = 1; x - i >= 0; i++)
			if (addField(x - i, y, fields))
				break;
		for (int i = 1; y + i < 8; i++)
			if (addField(x, y + i, fields))
				break;
		for (int i = 1; y - i >= 0; i++)
			if (addField(x, y - i, fields))
				break;
		return fields;
	}

	private boolean addField(int x, int y, List<ChessField> fields) {
		ChessField field = this.field.getBoard().getField(x, y);
		if (field.getFigure() == null) {
			fields.add(field);
			return false;
		} else if (field.getFigure().color != color) {
			fields.add(field);
		}
		return true;
	}
}
