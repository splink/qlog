package com.quui.utils.log4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Appender for Log4j for the purpose to send log messages to the QLog server
 * 
 * @author Max Kugland
 */
public class QLogSocketAppender extends AppenderSkeleton {
	private String _host;
	private int _port;
	private BufferedReader _reader;
	private Socket _socket;
	private PrintWriter _sender;
	private String _tabName;

	public QLogSocketAppender(String host, int port, String tabName) {
		_host = host;
		_port = port;
		_tabName = tabName;

		connect();
		setReadWrite();
		sendLoginMsg();
	}

	private void sendLoginMsg() {
		send("<login><name>" + _tabName + "</name></login>");
	}

	private void setReadWrite() {
		try {
			if (_socket != null) {
				_sender = new PrintWriter(_socket.getOutputStream(), true);
			}
		} catch (IOException e1) {
		}
	}

	private void connect() {
		try {
			_socket = new Socket(_host, _port);
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
	}

	@Override
	protected void append(LoggingEvent event) {
		String classInfo = event.getLocationInformation().getClassName() + " "
				+ event.getLocationInformation().getMethodName() + " "
				+ event.getLocationInformation().getLineNumber();

		String msg = buildMessage(event.getLevel().toString(), classInfo + " >> "
				+ event.getMessage());

		send(msg);
	}

	private void send(String msg) {
		if (_sender != null) {
			_sender.print(new String(msg + "\u0000"));
			_sender.flush();
		}
	}

	private String buildMessage(String level, String msg) {
		return "<log><color>" + getColorForLevel(level) + "</color><msg><![CDATA[" + msg
				+ "]]></msg></log>";
	}

	private String getColorForLevel(String level) {
		if (level.equals("GARBAGE")) {
			return "#999900";
		} else if (level.equals("FATAL")) {
			return "#FFFF00";
		} else if (level.equals("ERROR")) {
			return "#FF0000";
		} else if (level.equals("WARN")) {
			return "#FFFFCC";
		} else if (level.equals("INFO")) {
			return "#CCFFFF";
		} else if (level.equals("DEBUG")) {
			return "#00FF00";
		} else if (level.equals("TRACE")) {
			return "#FFFFFF";
		} else
			return "#FFFFFF";
	}

	public void close() {
		try {
			_reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean requiresLayout() {
		return false;
	}
}