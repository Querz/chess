# Chess
Chess in Java using JavaFX and JSON

---

A small Chess game implementing the default rules by restricting movement of figures, highlighting fields and showing status messages for:
- Movement of pieces
- En passant
- Castling
- Check
- Check mate
- Stalemate

---

## Serialization and Deserialization

The game can be saved and loaded using any String format. The interface [ChessIO](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessIO.java) can be implemented individually and a custom format implemented:

[ChessIO](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessIO.java)#load(String, ChessBoard) is used to load a String (the first parameter) into the ChessBoard (the second parameter).

[ChessIO](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessIO.java)#save(ChessBoard) is used to serialize the current state of the ChessBoard into a String.

[ChessLoader](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/json/ChessLoader.java) is an example implementation that uses the Google GSON library to serialize the ChessBoard into a JSON String and to deserialize a JSON String into the ChessBoard.

[FigureFactory](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/FigureFactory.java).createFigure(Color, String, String, int) can be used to make Figures from basic data. For example:
```java
Figure figure = FigureFactory.createFigure(Color.BLACK, "queen", "c4", 6);
```
This would create a new instance of [Queen](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Queen.java) on the field C4, which moved the first time on the 6th turn in the game. Keeping track of when the first turn of a figure occured is used to validate Castling and En Passant.

[ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessBoard.java)#setFigure(Figure) then sets the Figure to the respective field.

For serialization, the following methods will return all required information to save an entire Chess game:

- [ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessBoard.java)#getCurrentTurn()
- [ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Figure.java)#getName()
- [ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Figure.java)#getPos()
- [ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Figure.java)#getFirstTurn()

---
