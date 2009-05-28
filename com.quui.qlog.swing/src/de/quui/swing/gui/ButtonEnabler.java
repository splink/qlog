package de.quui.swing.gui;

import java.util.List;

import javax.swing.JMenuItem;

import de.quui.swing.gui.tab.ITab;
import de.quui.swing.gui.tab.Tab;
import de.quui.swing.gui.tab.TreeTab;


public class ButtonEnabler
{
	public ButtonEnabler()
	{
	}
	
	public void process(ITab currentTab, List<JMenuItem> items)
	{
		if(currentTab != null)
		{
			if(currentTab.getClass().equals(Tab.class))
			{
				setForTab(items);
			}
			else if(currentTab.getClass().equals(TreeTab.class))
			{
				setForTreeTab(items);
			}
		}
		else
		{
			setForNullTab(items);
		}
	}

	private void setForNullTab(List<JMenuItem> items)
	{
		for (JMenuItem menuItem : items)
		{
			int id = Integer.parseInt(menuItem.getName());
			if(id == ButtonFactory.CLEAR) menuItem.setEnabled(false);
			else if(id == ButtonFactory.SAVE_LOG) menuItem.setEnabled(false);
			else if(id == ButtonFactory.FILTER) menuItem.setEnabled(false);
			else if(id == ButtonFactory.CHANGE_FONTSIZE) menuItem.setEnabled(false);
		}
	}

	private void setForTreeTab(List<JMenuItem> items)
	{
		for (JMenuItem menuItem : items)
		{
			int id = Integer.parseInt(menuItem.getName());
			if(id == ButtonFactory.CLEAR) menuItem.setEnabled(false);
			else if(id == ButtonFactory.SAVE_LOG) menuItem.setEnabled(false);
		}
	}

	private void setForTab(List<JMenuItem> items)
	{
		for (JMenuItem menuItem : items)
		{
			menuItem.setEnabled(true);
		}
	}
}
