package com.quui.qlog.swing.properties;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class PropertiesSaver {
	private String _path;
	private Dimension _size;
	private Point _point;
	private boolean _aOnTop;
	private boolean _cOnConnect;
	private int _fontsize;

	public PropertiesSaver(String path, Dimension size, Point point, boolean aOnTop,
			boolean cOnConnect, int fontsize) {
		_path = path;
		_size = size;
		_point = point;
		_aOnTop = aOnTop;
		_cOnConnect = cOnConnect;
		_fontsize = fontsize;

		initialize();
	}

	private void initialize() {
		try {
			File f = new File(_path);
			Document doc = new SAXBuilder().build(f);

			processWindowNodes(doc.getRootElement());
			writeFile(doc, f);
		} catch (Exception e) {
			System.out.println("exception caught: " + e.getMessage());
		}
	}

	/**
	 * Recursively process the xml file and inject the resources filesizes
	 * 
	 * @param element
	 *            used to traverse the xml tree
	 */
	@SuppressWarnings("unchecked")
	private void processWindowNodes(Element element) {
		List<Element> wchilds = element.getChild("window").getChildren();
		for (Element e : wchilds) {
			if (e.getName().equals("x"))
				e.setText("" + _point.x);
			else if (e.getName().equals("y"))
				e.setText("" + _point.y);
			else if (e.getName().equals("width"))
				e.setText("" + _size.width);
			else if (e.getName().equals("height"))
				e.setText("" + _size.height);
			else if (e.getName().equals("fontsize"))
				e.setText("" + _fontsize);
		}

		List<Element> schilds = element.getChild("settings").getChildren();
		for (Element e : schilds) {
			if (e.getName().equals("clearonconnect"))
				e.setText("" + _cOnConnect);
			else if (e.getName().equals("alwaysontop"))
				e.setText("" + _aOnTop);
		}
	}

	/**
	 * Wrtite the processed xml document to disk
	 * 
	 * @param doc
	 *            the xml document
	 * @param file
	 *            the file to store the xml doc into
	 */
	private void writeFile(Document doc, File file) {
		XMLOutputter out = new XMLOutputter();
		try {
			FileWriter writer = new FileWriter(file);
			out.output(doc, writer);
			writer.flush();
			writer.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
