package com.quui.qlog.swing.data;

import org.w3c.dom.Document;

import com.quui.qlog.core.data.IGuiMediator;
import com.quui.qlog.swing.gui.tab.ITab;
import com.quui.qlog.swing.gui.tab.ITreeTab;
import com.quui.qlog.swing.gui.tab.TabFactory;
import com.quui.utils.util.IDestroyable;


public class SwingMediator implements IGuiMediator {
	private ITab _tab;
	private String _name;
	private IDestroyable _dataHandler;

	public SwingMediator() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDataTransformer(IDestroyable dataHandler) {
		_dataHandler = dataHandler;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void onLogin(String name) {
		_name = name;
		createTab();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void onMessage(String message, String color) {
		_tab.incomingMessage(color, message);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void onCommand(String command) {
		_tab.incomingCommand(command);
	}
	
	/**
	 * {@inheritDoc}
	 */
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
	
	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
		_dataHandler.destroy();
		_dataHandler = null;
		_tab = null;

	}

}
