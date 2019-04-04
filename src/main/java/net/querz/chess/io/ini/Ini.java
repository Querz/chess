package net.querz.chess.io.ini;

import java.util.LinkedHashMap;
import java.util.Map;

public class Ini {

	private Map<String, String> root = new LinkedHashMap<>();
	private Map<String, Map<String, String>> sections = new LinkedHashMap<>();

	public void set(String section, String key, String value) {
		Map<String, String> sectionMap = sections.computeIfAbsent(section, k -> new LinkedHashMap<>());
		sectionMap.put(key, value);
	}

	public void set(String section, String key, int value) {
		set(section, key, value + "");
	}

	public void set(String key, String value) {
		root.put(key, value);
	}

	public void set(String key, int value) {
		set(key, value + "");
	}

	public String get(String section, String key) {
		return sections.get(section).get(key);
	}

	public String get(String key) {
		return root.get(key);
	}

	public Map<String, String> getRootElements() {
		return root;
	}

	public Map<String, Map<String, String>> getSectionElements() {
		return sections;
	}

	public int getAsInt(String section, String key) {
		return Integer.parseInt(get(section, key));
	}

	public int getAsInt(String key) {
		return Integer.parseInt(get(key));
	}

	@Override
	public String toString() {
		return root + "\n" + sections;
	}
}
