package de.quui.swing.data;

import org.w3c.dom.Document;

import de.quui.server.IClientDataHandler;
import de.quui.swing.gui.tab.ITab;
import de.quui.swing.gui.tab.ITreeTab;
import de.quui.swing.gui.tab.TabFactory;

public class SwingMediator implements IGuiMediator {
	private ITab _tab;
	private String _name;
	private IClientDataHandler _dataHandler;

	public SwingMediator() {
	}
	
	public void setClientDataHandler(IClientDataHandler dataHandler) {
		_dataHandler = dataHandler;
	}
	
	public void onLogin(String name) {
		_name = name;
		createTab();
	}
	
	public void onMessage(String message, String color) {
		_tab.incomingMessage(color, message);
	}
	
	public void onCommand(String command) {
		_tab.incomingCommand(command);
	}
	
	public void onTree(Document doc) {
		createTreeTab(doc);
	}
	
	private void createTab() {
		_tab = TabFactory.createTab(this, _name);
		_tab.incomingCommand("clearonconnect");
		_tab.incomingMessage("#ffffff", "login as " + _name);
	}

	private void createTreeTab(Document doc) {
		ITreeTab tab = TabFactory.createTreeTab(_name + "- Tree");
		tab.setXmlDoc(doc);
	}
	
	public void destroy() {
		_dataHandler.destroy();
		_dataHandler = null;
		_tab = null;

	}

}
