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

![chess_default](https://raw.githubusercontent.com/Querz/chess/616ca78d9c6e24668c923a7b2aa6da3b76f48aa3/assets/chess_default.png)

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

- [ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessBoard.java)#getFigures()
- [ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessBoard.java)#getCurrentTurn()
- [Figure](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Figure.java)#getName()
- [Figure](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Figure.java)#getPos()
- [Figure](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Figure.java)#getFirstTurn()

---

## Godmode

The program can be started in a Test-/Godmode by using the program argument "--godmode". This enables several functions to freely control the ChessBoard and the Pieces. Right-clicking a field shows a context menu to add and remove Pieces, and the options menu in the bottom right corner of the window can now clear the ChessBoard and increment the turn counter (therefore switching the player whose turn it is).

![chess_default](https://raw.githubusercontent.com/Querz/chess/616ca78d9c6e24668c923a7b2aa6da3b76f48aa3/assets/chess_godmode.png)

---
