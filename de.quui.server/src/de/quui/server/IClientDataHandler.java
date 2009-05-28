package de.quui.server;


public interface IClientDataHandler {
	
	public void setClient(IClient client);
	
	public void onData(String data);

	public void destroy();
}
