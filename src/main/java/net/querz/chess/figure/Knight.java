package net.querz.chess.figure;

import net.querz.chess.ChessField;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Figure {

	public Knight(Color color, ChessField field) {
		super(color, "knight", field);
	}

	@Override
	public List<ChessField> getAccessibleFields() {
		List<ChessField> fields = new ArrayList<>();
		addField(x + 2, y + 1, fields);
		addField(x + 2, y - 1, fields);
		addField(x + 1, y + 2, fields);
		addField(x + 1, y - 2, fields);
		addField(x - 2, y + 1, fields);
		addField(x - 2, y - 1, fields);
		addField(x - 1, y + 2, fields);
		addField(x - 1, y - 2, fields);
		return fields;
	}

	private void addField(int x, int y, List<ChessField> fields) {
		ChessField field = this.field.getBoard().getField(x, y);
		if (field != null && (field.getFigure() == null || field.getFigure().color != color)) {
			fields.add(field);
		}
	}
}
