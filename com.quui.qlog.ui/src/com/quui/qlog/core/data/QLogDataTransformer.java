package com.quui.qlog.core.data;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.quui.server.IClient;
import com.quui.server.IDataTransformer;


public class QLogDataTransformer implements IDataTransformer {
	private DocumentBuilder _builder;
	private boolean _isLoggedIn = false;
	private IClient _client;
	private IGuiMediator _mediator;

	public QLogDataTransformer(IGuiMediator mediator) {
		_mediator = mediator;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			_builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setClient(IClient client) {
		_client = client;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Expected data: <log><color>#ff00ff</color><msg>Some log msg</msg></log>
	 */
	public void onData(String data) {
		System.out.println("data: " + data);

		if (_isLoggedIn) {
			handleData(data);
		} else {
			handleLogin(data);
		}
	}

	private void handleData(String data) {
		Document doc;
		try {
			doc = _builder.parse(new InputSource(new StringReader(data)));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (doc != null) {
			String color = getColor(doc);
			String message = getMessage(doc);
			String command = getCommand(doc);
			String tree = getTree(doc);

			if (!command.equals("")) {
				_mediator.onCommand(command);
				return;
			} else if (!color.equals("") && !message.equals("")) {
				_mediator.onMessage(message, color);
				return;
			} else if (!tree.equals("")) {
				_mediator.onTree(doc);
			}
		}
	}

	private static String getTree(Document doc) {
		Node item = doc.getElementsByTagName("tree").item(0);
		if (item != null) {
			return item.getTextContent();
		} else
			return "";
	}

	private static String getCommand(Document doc) {
		Node item = doc.getElementsByTagName("command").item(0);
		if (item != null) {
			return item.getTextContent();
		} else
			return "";
	}

	private static String getMessage(Document doc) {
		Node item = doc.getElementsByTagName("msg").item(0);
		if (item != null) {
			return item.getTextContent();
		} else
			return "";

	}

	private static String getColor(Document doc) {
		Node item = doc.getElementsByTagName("color").item(0);
		if (item != null) {
			return item.getTextContent();
		} else
			return "";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Expected data: <login><name>Some name</name></login>
	 */
	private void handleLogin(String data) {
		String name = "default";
		try {
			Document doc = _builder.parse(new InputSource(new StringReader(data)));
			name = doc.getElementsByTagName("name").item(0).getTextContent();
		} catch (Exception e) {
			System.out.println(e.getMessage() + "\r" + e.getStackTrace());
		}
		
		_mediator.setDataTransformer(this);
		_mediator.onLogin(name);
		_isLoggedIn = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
		_client.destroy();
		_client = null;
		_builder = null;
	}

}
