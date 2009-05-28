package de.quui.swing.gui.tab;

import javax.swing.Icon;
import javax.swing.JScrollPane;

public interface ITab
{
	public void incomingMessage(String color, String message);
	
	public void incomingCommand(String command);
	
	public void close();
	
	public void clear();
	
	public JScrollPane getTabComponent();
	
	public String getName();
	
	public Icon getIcon();
	
	public void setScrollLock(boolean lock);
	
	public void applyFilter(String filter);
	
	public String getFilter();
	
	public void changeFontSize(int size);
}
