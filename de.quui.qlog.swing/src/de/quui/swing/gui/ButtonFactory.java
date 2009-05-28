package de.quui.swing.gui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class ButtonFactory
{
	private static final List<JMenuItem> _items = new ArrayList<JMenuItem>();
	
	public static final int ABOUT = 1;
	public static final int CLEAR = 2;
	public static final int CLEAR_ON_CONNECT = 3;
	public static final int FILTER = 4;
	public static final int ALWAYS_ON_TOP = 5;
	public static final int SAVE_LOG = 6;
	public static final int CHANGE_FONTSIZE = 7;
	public static final int SCROLL_LOCK = 8;

	public static JMenuItem create(int id, ActionListener listener)
	{
		JMenuItem item = null;
		switch (id)
		{
		case ABOUT:
			 item = buildItem(id, "About", KeyEvent.VK_A, "A", "about", listener);
			 break;
		case CLEAR:
			 item = buildItem(id, "Clear", KeyEvent.VK_E, "E", "clear", listener);
			 break;
		case CLEAR_ON_CONNECT:
			 item = buildItem(id, "Clear On Connect", KeyEvent.VK_O, "O",
					"clearonconnect", listener);
			 break;
		case FILTER:
			 item = buildItem(id, "Filter", KeyEvent.VK_F, "F", "filter", listener);
			 break;
		case ALWAYS_ON_TOP:
			 item = buildItem(id, "Always on top", KeyEvent.VK_T, "T",
					"alwaysontop", listener);
			 break;
		case SAVE_LOG:
			 item = buildItem(id, "Save Logfile", KeyEvent.VK_S, "S", "savefile",
					listener);
			 break;
		case CHANGE_FONTSIZE:
			 item = buildItem(id, "Change Fontsize", KeyEvent.VK_P, "P", "fontsize",
					listener);
			 break;
		case SCROLL_LOCK:
			 item = buildItem(id, "Scroll Lock", KeyEvent.VK_L, "L", "scrollLock",
					listener);
			 break;
		default:
			System.out.println("No Button available for id " + id);
		}
		
		if(item != null) _items.add(item);
		
		return item;
	}

	private static JMenuItem buildItem(int id, String title, int keyEvent, String key,
			String actionCmd, ActionListener listener)
	{
		JMenuItem item = new JMenuItem(title, keyEvent);
		item.setAccelerator(KeyStroke.getKeyStroke("control "
				+ key.toUpperCase()));
		item.setActionCommand(actionCmd);
		item.addActionListener(listener);
		item.setName(""+id);

		return item;
	}

	public static List<JMenuItem> getItems()
	{
		return _items;
	}
}
