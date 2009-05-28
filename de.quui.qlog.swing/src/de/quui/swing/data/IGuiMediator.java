package de.quui.swing.data;

import org.w3c.dom.Document;

import de.quui.server.IClientDataHandler;
import de.quui.utils.util.IDestroyable;

public interface IGuiMediator extends IDestroyable {
	public void setClientDataHandler(IClientDataHandler dataHandler);

	public void onLogin(String name);

	public void onMessage(String message, String color);

	public void onCommand(String command);

	public void onTree(Document doc);
}
