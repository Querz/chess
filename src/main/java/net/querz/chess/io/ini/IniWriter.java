package net.querz.chess.io.ini;

import java.io.StringWriter;
import java.util.Map;

public class IniWriter {

	private Ini ini;
	private StringWriter writer = new StringWriter();

	public IniWriter(Ini ini) {
		this.ini = ini;
	}

	public String write() {
		writeRoot();
		writeSections();
		writer.flush();
		return writer.toString();
	}

	private void writeRoot() {
		writeMap(ini.getRootElements());
	}

	private void writeSections() {
		for (Map.Entry<String, Map<String, String>> section : ini.getSectionElements().entrySet()) {
			writer.append('[').append(section.getKey()).append(']').write('\n');
			writeMap(section.getValue());
		}
	}

	private void writeMap(Map<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			writer.append(entry.getKey()).append('=').append(entry.getValue()).write('\n');
		}
	}
}
