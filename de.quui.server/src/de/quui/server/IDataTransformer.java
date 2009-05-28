package de.quui.server;

import de.quui.utils.util.IDestroyable;

/**
 * An <code>IDataTransformer</code> instance receives incoming data from it's associated
 * <code>IClient</code> and is responsible for transforming it into something meaningful.
 * 
 * @author maxmc
 *
 */
public interface IDataTransformer extends IDestroyable {
	
	/**
	 * Sets the client from which the data is received
	 * 
	 * @param client
	 */
	public void setClient(IClient client);
	
	/**
	 * Invoked whenever new data arrives
	 * 
	 * @param data
	 */
	public void onData(String data);
}
