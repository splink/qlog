package com.quui.qlog.swing.gui.tab;

import java.awt.Dimension;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

import com.quui.qlog.swing.gui.Filter;
import com.quui.qlog.swing.gui.TableBuilder;
import com.quui.qlog.swing.gui.Window;
import com.quui.qlog.swing.properties.PropertiesReader;
import com.quui.utils.util.IDestroyable;


public class Tab implements ITab {
	private PropertiesReader _reader;
	private TableBuilder _tableBuilder;
	private JScrollPane _scrollPane;
	private IDestroyable _client;
	private String _name;
	private Window _window;
	private boolean _scrollLock;

	public Tab(Window window, PropertiesReader reader, IDestroyable client, String name) {
		_reader = reader;
		_window = window;
		_scrollLock = reader.getScrollLock();
		_name = name;
		_client = client;
		_tableBuilder = new TableBuilder();
		_scrollPane = createTabContent();
		getEditorPane().setText(_tableBuilder.getContent());
		_window.addTab(this);
	}

	private JScrollPane createTabContent() {
		JEditorPane textPane = new JEditorPane();
		textPane.setEditable(false);
		textPane.setPreferredSize(new Dimension(_reader.getWidth(), _reader.getHeight()));
		textPane.setContentType("text/html");
		textPane.setEditorKit(new HTMLEditorKit());

		JScrollPane scrollPane = new JScrollPane(textPane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		return scrollPane;
	}

	private JEditorPane getEditorPane() {
		JEditorPane je = null;
		try {
			je = (JEditorPane) _scrollPane.getViewport().getView();
		} catch (Exception e) {
			return null;
		}
		return je;
	}

	public void incomingCommand(String command) {
		if (command.equals("clear") && _window.getClearOnConnect()) {
			_tableBuilder.clear();
			getEditorPane().setText(_tableBuilder.getContent());
		} else if (command.equals("clearonconnect") && _window.getClearOnConnect()) {
			_tableBuilder.clear();
			getEditorPane().setText(_tableBuilder.getContent());
		}
	}

	public void incomingMessage(String color, String message) {
		message = message.replace(" ", "&nbsp;");

		String newMsg = _tableBuilder.buildHTML(color, message);
		if (newMsg == null)
			return;

		try {
			((HTMLEditorKit) getEditorPane().getEditorKit()).read(new StringReader(newMsg),
					getEditorPane().getDocument(), getEditorPane().getDocument().getLength());
		} catch (BadLocationException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!_scrollLock) {
			getEditorPane().setCaretPosition(getEditorPane().getDocument().getLength());
		}

		_window.notifyAboutIncomingMsg(_name);
	}

	public void close() {
		_client.destroy();
		_reader = null;
		_client = null;
		_tableBuilder = null;
		_scrollPane = null;
	}

	public void clear() {
		_tableBuilder.clear();
		getEditorPane().setText(_tableBuilder.getContent());
	}

	public JScrollPane getTabComponent() {
		return _scrollPane;
	}

	public String getName() {
		return _name;
	}

	public Icon getIcon() {
		return new CloseTabIcon();
	}

	public TableBuilder getTableBuilder() {
		return _tableBuilder;
	}

	public void setScrollLock(boolean lock) {
		_scrollLock = lock;
	}

	public String getFilter() {
		return _tableBuilder.getFilter();
	}

	public void applyFilter(String filter) {
		_tableBuilder.setFilter(filter);
		getEditorPane().setText(Filter.find(filter, _tableBuilder));
	}

	public void changeFontSize(int size) {
		_tableBuilder.changeFontSize(size);
	}
}