package net.querz.chess.io.ini;

public class IniParser {

	private String data;
	private int index;
	private Ini ini = new Ini();

	public IniParser(String data) {
		this.data = data;
	}

	public Ini read() {
		skipWhiteSpaces();
		String currentSection = null;
		while (hasNext()) {
			skipWhiteSpaces();
			if (!hasNext()) {
				return ini;
			}
			switch (currentChar()) {
			case ';':
				skipLine();
				break;
			case '[':
				next(); // skip '['
				currentSection = readUntil(']');
				next(); // skip ']'
				break;
			default:
				String key = readStringUntil('=');
				String value = readStringUntil('\n');
				if (currentSection == null) {
					ini.set(key, value);
				} else {
					ini.set(currentSection, key, value);
				}
			}
		}
		return ini;
	}

	// readUntil includes the first char but excludes the char it will read to
	private String readStringUntil(char c) {
		String out;
		if (currentChar() == '"') {
			StringBuilder s = null;
			int oldIndex = ++index;
			boolean escaped = false;
			while (hasNext()) {
				char ch = next();
				if (escaped) {
					if (ch != '\\' && ch != '"') {
						throw new RuntimeException("invalid escape of '" + ch + "'");
					}
					escaped = false;
				} else {
					if (ch == '\\') {
						escaped = true;
						if (s != null) {
							continue;
						}
						s = new StringBuilder(data.substring(oldIndex, index - 1));
						continue;
					}
					if (ch == '"') {
						return s == null ? data.substring(oldIndex, ++index - 2) : s.toString();
					}
				}
				if (s != null) {
					s.append(ch);
				}
			}
			throw new RuntimeException("no end quote");
		} else {
			out = readUntil(c);
		}
		if (hasNext()) {
			next(); // skip c
		}
		return out;
	}

	private char currentChar() {
		return data.charAt(index);
	}

	private boolean hasNext() {
		return index < data.length();
	}

	private char next() {
		return data.charAt(index++);
	}

	private void skipWhiteSpaces() {
		while (hasNext() && Character.isWhitespace(currentChar())) {
			index++;
		}
	}

	private void skipLine() {
		while (hasNext() && currentChar() != '\n') {
			index++;
		}
	}

	private String readUntil(char c) {
		int start = index;
		while (hasNext() && currentChar() != c) {
			if (currentChar() == '\n') {
				throw new RuntimeException("could not find character '" + c + "' in line");
			}
			index++;
		}
		if (!hasNext()) {
			return data.substring(start, ++index);
		}
		return data.substring(start, index);
	}
}
