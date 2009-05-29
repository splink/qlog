package com.quui.qlog.core.data;

import org.w3c.dom.Document;

import com.quui.utils.util.IDestroyable;


/**
 * <code>IGuiMediator</code> implementations mediate between the gui layer and the data layer.
 * 
 * 
 * @author maxmc
 *
 */
public interface IGuiMediator extends IDestroyable {
	
	/**
	 * Sets the <code>IDestroyable</code> data transformer from which <code>IGuiMediator<code>
	 * receives data
	 * 
	 * @param dataHandler
	 */
	public void setDataTransformer(IDestroyable dataHandler);
	
	/**
	 * Invoked when a login occurs
	 * 
	 * @param name
	 */
	public void onLogin(String name);
	
	/**
	 * Invoked when a new message arrives
	 * 
	 * @param message
	 * @param color
	 */
	public void onMessage(String message, String color);
	
	/**
	 * Invoked when a new command arrives
	 * 
	 * @param command
	 */
	public void onCommand(String command);
	
	/**
	 * Invoked when a new tree arrives
	 * 
	 * @param doc
	 */
	public void onTree(Document doc);
}
