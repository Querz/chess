package net.querz.chess.figure;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import net.querz.chess.ChessField;
import net.querz.chess.Helper;
import net.querz.chess.ChessGame;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Figure implements Serializable {

	public transient static final DataFormat CHESS_FIGURE = new DataFormat("chess.figure");
	private transient static Map<String, Image> imageCache = new HashMap<>();
	transient ChessField field;
	int x, y = -1;
	Color color;
	private String name;
	private String imageFileName;
	private int firstTurn = -1;

	Figure(Color color, String name, ChessField field) {
		this.color = color;
		this.name = name;
		setField(field);
		imageFileName = "images/" + color.getName() + "_" + name + ".png";
		if (!imageCache.containsKey(imageFileName)) {
			imageCache.put(imageFileName, Helper.loadImage(imageFileName, 50, 50));
		}
		if (field != null) {
			this.x = field.getX();
			this.y = field.getY();
			field.setFigure(this, true);
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}

	public String getName() {
		return name;
	}

	public String getPos() {
		return ((char) (x + 97)) + "" + ((char) (7 - y + 49));
	}

	//returns whether the Figure was successfully moved
	public Figure move(ChessField field, boolean graphic) {
		Figure killedFigure = field.getFigure();
		field.setFigure(this, graphic);
		if (this.field != null) {
			this.field.setFigure(null, graphic);
		}
		int oldX = x;
		int oldY = y;
		x = field.getX();
		y = field.getY();
		setField(field);
		Figure postFigure = postTurnAction(ChessGame.getBoard().getField(oldX, oldY), field, graphic);
		if (graphic && firstTurn < 0) {
			firstTurn = field.getBoard().getCurrentTurn();
		}
		if (graphic && (killedFigure != null || !(postFigure instanceof Pawn))) {
			ChessGame.getBoard().set50MoveRuleTurns(0);
		}
		return killedFigure == null ? postFigure : killedFigure;
	}

	public boolean canMoveTo(ChessField field) {
		return canMove() && getAllAccessibleFields().contains(field);
	}

	public boolean canMove() {
		return getField().getBoard().getTurn() == color;
	}

	public void setField(ChessField field) {
		this.field = field;
	}

	public ChessField getField() {
		return field;
	}

	public ImageView getImageView() {
		return new ImageView(imageCache.get(imageFileName));
	}

	public Image getImage() {
		return imageCache.get(imageFileName);
	}

	//executed after this Figure has been moved to the parameter field
	public Figure postTurnAction(ChessField oldField, ChessField newField, boolean graphic) {
		return null;
	}

	//this also considers the king being in check
	public List<ChessField> getAllAccessibleFields() {
		List<ChessField> fields = getAccessibleFields();
		King king = field.getBoard().getKing(color);
		if (king != null) {
			List<ChessField> trueFields = new ArrayList<>();
			ChessField oldField = field;
			for (ChessField to : fields) {
				//simulate move to that field
				Figure killedFigure = move(to, false);
				field.getBoard().recalculateAttackedFields();
				boolean check = king.isCheck();
				if (!check) {
					trueFields.add(to);
				}
				//revert move
				move(oldField, false);
				if (killedFigure != null) {
					killedFigure.field.setFigure(killedFigure, false);
				}
				field.getBoard().recalculateAttackedFields();
			}
			fields = trueFields;
		}
		return fields;
	}

	public int getFirstTurn() {
		return firstTurn;
	}

	public void setFirstTurn(int firstTurn) {
		this.firstTurn = firstTurn;
	}

	//returns a list of all accessible fields of this figure, including fields with opponent's figures
	//that can be beaten
	public abstract List<ChessField> getAccessibleFields();

	@Override
	public String toString() {
		return "Figure:<" + color.getName() +
				" " + name + " " +
				"x=" + x + " y=" + y +
				" Field=" + field + ">";
	}
}
