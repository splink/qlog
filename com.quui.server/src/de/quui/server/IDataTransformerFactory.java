package de.quui.server;

/**
 * A factory responsible for creating <code>IDataTransformer</code> implementation instances
 * which are used by <code>IClient</code> implementation instances created by the server.
 * 
 * @author maxmc
 *
 */
public interface IDataTransformerFactory {
	/**
	 * Creates an instance which implements the <code>IDataTransformer</code> interface
	 * 
	 * @return an <code>IDataTransformer</code> implementing instance
	 */
	public IDataTransformer create();
}
