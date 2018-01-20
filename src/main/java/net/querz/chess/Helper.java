package net.querz.chess;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Helper {

	public static Image loadImage(String resource, int width, int height) {
		return new Image(Helper.class.getClassLoader().getResourceAsStream(resource), width, height, true, true);
	}

	public static String loadStringFromFile(File file) {
		StringBuilder sb = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(file.toURI()))) {
			stream.forEach(sb::append);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	public static String loadStringFromResource(String resource) {
		StringBuilder sb = new StringBuilder();
		URI uri = null;
		try {
			URL url = ChessIO.class.getClassLoader().getResource(resource);
			if (url != null) {
				uri = url.toURI();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (uri == null) {
			return null;
		}
		try (Stream<String> stream = Files.lines(Paths.get(uri))) {
			stream.forEach(sb::append);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	public static void saveStringToFile(String string, File file) {
		try {
			Files.write(Paths.get(file.toURI()), string.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
