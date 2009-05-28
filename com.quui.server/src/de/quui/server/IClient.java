package de.quui.server;

public interface IClient {
	
	public void send(String message);
	
	public void destroy();
	
	public long getConnectionTime();
	
	public String getClientId();
}
