package com.quui.qlog.core;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class PropertiesReader {
	private String _path;
	private Document _document;

	public PropertiesReader(String path) {
		_path = path;
	}

	public void initialize() {
		try {
			readFile();
		} catch (Exception e) {
			System.out.println("Exeption with PropertiesReader.");
			e.printStackTrace();
		}
	}

	private void readFile() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		_document = builder.parse(new File(_path));
	}

	public String getIp() {
		return _document.getElementsByTagName("ip").item(0).getTextContent();
	}

	public int getPort() {
		return Integer.parseInt(_document.getElementsByTagName("port").item(0).getTextContent());
	}

	public int getX() {
		return Integer.parseInt(_document.getElementsByTagName("x").item(0).getTextContent());
	}

	public int getY() {
		return Integer.parseInt(_document.getElementsByTagName("y").item(0).getTextContent());
	}

	public int getWidth() {
		return Integer.parseInt(_document.getElementsByTagName("width").item(0).getTextContent());
	}

	public int getHeight() {
		return Integer.parseInt(_document.getElementsByTagName("height").item(0).getTextContent());
	}

	public boolean getAlwaysOnTop() {
		return _document.getElementsByTagName("alwaysontop").item(0).getTextContent()
				.equals("true");
	}

	public boolean getClearOnConnect() {
		return _document.getElementsByTagName("clearonconnect").item(0).getTextContent().equals(
				"true");
	}

	public int getFontSize() {
		return Integer
				.parseInt(_document.getElementsByTagName("fontsize").item(0).getTextContent());
	}

	public boolean getScrollLock() {
		return _document.getElementsByTagName("scrolllock").item(0).getTextContent().equals("true");
	}
}
