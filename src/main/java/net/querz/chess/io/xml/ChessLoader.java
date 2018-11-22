package net.querz.chess.io.xml;

import net.querz.chess.ChessBoard;
import net.querz.chess.ChessIO;
import net.querz.chess.FigureFactory;
import net.querz.chess.figure.Color;
import net.querz.chess.figure.Figure;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Stack;

public class ChessLoader implements ChessIO {

	@Override
	public void load(byte[] data, ChessBoard board) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = null;
		try {
			reader = factory.createXMLStreamReader(bais);
			Stack<String> stack = new Stack<>();
			while (reader.hasNext()) {
				switch (reader.getEventType()) {
					case XMLStreamReader.START_ELEMENT:
						stack.push(reader.getName().getLocalPart());
						switch (String.join("/", stack)) {
							case "Board":
								board.setCurrenTurn(Integer.parseInt(reader.getAttributeValue("", "CurrentTurn")));
								board.set50MoveRuleTurns(Integer.parseInt(reader.getAttributeValue("", "FiftyMoveRuleTurns")));
								break;
							case "Board/Black/Figure":
								loadFigure(Color.BLACK, reader, board);
								break;
							case "Board/White/Figure":
								loadFigure(Color.WHITE, reader, board);
								break;
						}
						break;
					case XMLStreamReader.END_ELEMENT:
						stack.pop();
						break;
				}
				reader.next();
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void loadFigure(Color color, XMLStreamReader reader, ChessBoard board) {
		int firstTurn = Integer.parseInt(reader.getAttributeValue("", "FirstTurn"));
		String pos = reader.getAttributeValue("", "Pos");
		String type = reader.getAttributeValue("", "Type");
		Figure figure = FigureFactory.createFigure(color, type, pos, firstTurn);
		board.setFigure(figure);
	}

	@Override
	public byte[] save(ChessBoard board) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = null;
		try {
			writer = factory.createXMLStreamWriter(baos);
			writer.writeStartDocument();
			writer.writeStartElement("Board");
			writer.writeAttribute("CurrentTurn", board.getCurrentTurn() + "");
			writer.writeAttribute("FiftyMoveRuleTurns", board.get50MoveRuleTurns() + "");
			writeFigures(Color.BLACK, board.getFigures(Color.BLACK), writer);
			writeFigures(Color.WHITE, board.getFigures(Color.WHITE), writer);
			writer.writeEndElement();
		} catch (XMLStreamException ex) {
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (XMLStreamException ex) {
					ex.printStackTrace();
				}
			}
		}
		return baos.toByteArray();
	}

	private void writeFigures(Color color, List<Figure> figures, XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement(color.getFancyName());
		for (Figure figure : figures) {
			writer.writeEmptyElement("Figure");
			writer.writeAttribute("Type", figure.getName());
			writer.writeAttribute("Pos", figure.getPos());
			writer.writeAttribute("FirstTurn", figure.getFirstTurn() + "");
		}
		writer.writeEndElement();
	}

	@Override
	public String getFileTypeDescription() {
		return "XML files (*.xml)";
	}

	@Override
	public String getFileExtension() {
		return "xml";
	}
}
