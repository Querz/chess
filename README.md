# Chess
Chess in Java to exercise the usage of different serialization formats.

---

A small Chess game implementing the default rules by restricting movement of figures, highlighting fields and showing status messages for:
- Movement of pieces
- En passant
- Castling
- Promotion
- Check
- Check mate
- Stalemate
- 50-moves-rule

![chess_default](https://raw.githubusercontent.com/Querz/chess/616ca78d9c6e24668c923a7b2aa6da3b76f48aa3/assets/chess_default.png)

---

## Serialization and Deserialization

The game can be saved and loaded using any format. The interface [ChessIO](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessIO.java) can be used to implement a custom format:

[ChessIO](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessIO.java)#load(byte[], ChessBoard) is used to load a byte[] (the first parameter) into the ChessBoard (the second parameter).

[ChessIO](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessIO.java)#save(ChessBoard) is used to serialize the current state of the ChessBoard into a byte[].

To make your own implementation show up in the load / save dialog, simply implement the [ChessIO](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessIO.java)#getFileTypeDescription() and [ChessIO](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessIO.java)#getFileExtension() and add the complete class name to [META-INF/services/net.querz.chess.ChessIO](https://github.com/Querz/chess/blob/master/src/main/resources/META-INF/services/net.querz.chess.ChessIO).

There are multiple example implementations of linear reading and writing of differend kinds of formats:

| Library | Location | Description |
| :-------: | :--------: | :------------ |
| [gson](https://github.com/google/gson) | net.querz.chess.io.json | Saves and loads the game's state as JSON |
| stax | net.querz.chess.io.xml  | Uses the StAX library to save and load xml files |
| [snakeyaml](https://bitbucket.org/asomov/snakeyaml) | net.querz.chess.io.yaml | Uses the snakeyaml library to save and load yml files |
| csv | net.querz.chess.io.csv | Saves and loads the game's state as CSV files |
| [NBT](https://github.com/Querz/NBT) | net.querz.chess.io.nbt | Uses Tags from my NBT library to load from and save the game's state to gzip compressed NBT files |
| [BSON](https://github.com/mongodb/mongo-java-driver/tree/master/bson) | net.querz.chess.io.bson | Loads from and saves the game's state to gzip compressed BSON encoded files |
| Ini | net.querz.chess.io.ini | Loads and saves the game state from and to ini files using a simple ini parser |
| bits | net.querz.chess.io.bits | An absolutely minimized proprietary format to save the game's state using bit manipulation |

[FigureFactory](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/FigureFactory.java).createFigure(Color, String, String, int) can be used to make Figures from basic data. For example:
```java
Figure figure = FigureFactory.createFigure(Color.BLACK, "queen", "c4", 6);
```
This would create a new instance of [Queen](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Queen.java) on the field C4, which moved the first time on the 6th turn in the game. Keeping track of when the first turn of a figure occured is used to validate Castling and En Passant.

[ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessBoard.java)#setFigure(Figure) then sets the Figure to the respective field.

For serialization, the following methods will return all required information to save an entire Chess game:

- [ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessBoard.java)#getFigures()
- [ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessBoard.java)#getCurrentTurn()
- [ChessBoard](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/ChessBoard.java)#get50MoveRuleTurns()
- [Figure](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Figure.java)#getName()
- [Figure](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Figure.java)#getPos()
- [Figure](https://github.com/Querz/chess/blob/master/src/main/java/net/querz/chess/figure/Figure.java)#getFirstTurn()

---

## Godmode

The program can be started in a Test-/Godmode by using the program argument "--godmode". This enables several functions to freely control the ChessBoard and the Pieces. Right-clicking a field shows a context menu to add and remove Pieces, and the options menu in the bottom right corner of the window can now clear the ChessBoard and increment the turn counter (therefore switching the player whose turn it is).

![chess_default](https://raw.githubusercontent.com/Querz/chess/616ca78d9c6e24668c923a7b2aa6da3b76f48aa3/assets/chess_godmode.png)

---
