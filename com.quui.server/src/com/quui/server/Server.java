package com.quui.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Vector;

public class Server implements Runnable {
	public static String PING = "PING";
	private Thread _listener;
	private ServerSocket _server;
	private Vector<Client> _clients = new Vector<Client>();
	private int _clientCount = 0;
	private IDataTransformerFactory _factory;

	public Server(String ip, int port, IDataTransformerFactory factory) throws BindException {
		_factory = factory;
		startServer(ip, port);
	}

	private void startServer(String ip, int port) throws BindException {
		try {
			InetAddress byName = InetAddress.getByName(ip);
			_server = new ServerSocket(port, 30, byName);
			_listener = new Thread(this);
			_listener.start();
		} catch (Exception e) {
			throw new BindException(e.getMessage());
		}
	}

	public void run() {
		try {
			while (!_server.isClosed() && true) {
				Socket clientSocket = _server.accept();
				System.out.println("new client: " + _clientCount + " " + clientSocket);
				_clientCount++;
				Client c = new Client(this, clientSocket, _factory.create(), _clientCount + "_ID",
						new Date().getTime());
				c.start();
				_clients.addElement(c);
			}
		} catch (Exception e) {
			System.err.println("While running, caught exception: " + e.getMessage());
		}
	}

	/**
	 * Sends a serialized string to every connected client exept the client with the clientID.
	 * 
	 * @param clientId
	 *            the id of the destined client
	 * @param message
	 *            the message for the destined client to receive
	 */
	protected synchronized void broadcast(String clientId, String message) {
		Client client;
		for (int i = 0; i < _clients.size(); i++) {
			client = _clients.elementAt(i);
			if (!client.getClientId().equals(clientId)) {
				client.send(message);
			}
		}
	}

	/**
	 * Sends a serialized string to a client specified by clientID
	 * 
	 * @param clientID
	 * @param message
	 */
	protected synchronized void sendToClient(String id, String message) {
		Client client = getClientById(id);
		if (client != null) {
			client.send(message);
		}
	}

	/**
	 * Sends a serialized string to a client and disconnects the client afterwards
	 * 
	 * @param clientID
	 * @param message
	 */
	protected synchronized void disconnectClient(String clientID, String message) {
		Client client = getClientById(clientID);
		if (client != null) {
			if (message != null) {
				client.send(message);
			}
			client.destroy();
		}
	}

	/**
	 * Pings given client
	 * 
	 * @param clientID
	 */
	protected synchronized void pingClient(String clientID) {
		Client client = getClientById(clientID);
		if (client != null) {
			client.send(Server.PING);
		}
	}

	/**
	 * finds a client by its id and returns "null" if no matching client is conntected.
	 * 
	 * @param username
	 * @return client
	 */
	protected synchronized Client getClientById(String id) {
		for (int i = 0; i < _clients.size(); i++) {
			Client client = _clients.elementAt(i);
			if (client.getClientId().equals(id)) {
				return client;
			}
		}
		return null;
	}

	/**
	 * @return a list of client ids which are currently connected to the server
	 */
	protected synchronized Vector<String> getClientIdList() {
		Vector<String> clientList = new Vector<String>();
		for (int i = 0; i < _clients.size(); i++) {
			Client c = _clients.elementAt(i);
			clientList.addElement(c.getClientId());
		}
		return clientList;
	}
	
	protected synchronized void removeClient(Client client) {
		_clients.remove(client);
	}

	/**
	 * Closes the socket
	 */
	public void destroy() {
		try {
			_server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		_listener = null;
		_server = null;
		_factory = null;
		_clients = null;
	}
}