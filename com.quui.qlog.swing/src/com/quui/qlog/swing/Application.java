package com.quui.qlog.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.BindException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import com.quui.qlog.core.PropertiesReader;
import com.quui.qlog.swing.data.QLogDataTransformerFactory;
import com.quui.qlog.swing.gui.Window;
import com.quui.qlog.swing.gui.popup.FontSizePopUp;
import com.quui.qlog.swing.gui.popup.MessagePane;
import com.quui.qlog.swing.gui.tab.TabFactory;
import com.quui.qlog.swing.properties.PropertiesSaver;
import com.quui.server.Server;
import com.quui.utils.log4j.QLogSocketAppender;


public class Application {
	private static Logger _logger = Logger.getRootLogger();
	private static String _config = "config.xml";
	private Window _window;

	public Application() {
		configureLog4j();

		PropertiesReader reader = new PropertiesReader(_config);
		reader.initialize();

		connectServer(reader);
	}

	private void configureLog4j() {
		SimpleLayout layout = new SimpleLayout();
		ConsoleAppender consoleAppender = new ConsoleAppender(layout);
		QLogSocketAppender qlogAppender = new QLogSocketAppender("localhost", 6666, "QLog");
		_logger.addAppender(qlogAppender);
		_logger.addAppender(consoleAppender);
	}

	private void connectServer(PropertiesReader reader) {
		Window win = createWindow(reader);
		win.setSize(new Dimension(reader.getWidth(), reader.getHeight()));
		TabFactory.setWindow(win);
		TabFactory.setPropertiesReader(reader);
		FontSizePopUp.setFontSize(reader.getFontSize());

		try {
			new Server(reader.getIp(), reader.getPort(), new QLogDataTransformerFactory());
		} catch (BindException e) {
			MessagePane.createSocketErrorDialog(win, reader.getPort());
			System.exit(0);
		}
	}

	private void saveSettings() {
		new PropertiesSaver(_config, _window.getSize(), _window.getLocation(),
				_window.getAlwaysOnTop(), _window.getClearOnConnect(),
				FontSizePopUp.getFontSize());
	}

	private Window createWindow(PropertiesReader reader) {
		_window = new Window(reader);
		_window.pack();
		_window.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				saveSettings();
				System.exit(0);
			}
		});

		_window.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent evt) {
				Component c = (Component) evt.getSource();

				Window t = (Window) c;
				t.setPreferredSize(c.getSize());
			}

			public void componentHidden(ComponentEvent e) {
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentShown(ComponentEvent e) {
			}
		});

		_window.setVisible(true);
		return _window;
	}

	public static void main(String[] args) {
		new Application();
	}
}
