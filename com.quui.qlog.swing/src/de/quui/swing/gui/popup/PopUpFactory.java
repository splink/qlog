package de.quui.swing.gui.popup;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import de.quui.swing.gui.Look;

public class PopUpFactory
{
	public static JFrame createPopup(Window parent, String title, Dimension size, Component content)
	{
		JFrame frame = new JFrame(title);
		frame.setResizable(false);
		Look.createWindowLook(frame);
		frame.setSize(size);
		frame.setAlwaysOnTop(true);

		Point position = parent.getLocation();
		int x = position.x + parent.getWidth() / 2 - frame.getSize().width / 2;
		int y = position.y + parent.getHeight() / 2 - frame.getSize().height / 2;
		frame.setLocation(x, y);

		PopupFactory factory = PopupFactory.getSharedInstance();
		Popup popup = factory.getPopup(frame, content, x, y);
		popup.show();
		frame.setVisible(true);
		return frame;
	}
}
