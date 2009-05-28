package de.quui.swing.gui.tab;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The class which generates the 'X' icon for the tabs. The constructor accepts
 * an icon which is extra to the 'X' icon, so you can have tabs like in
 * JBuilder. This value is null if no extra icon is required.
 */
class CloseTabIcon extends ImageIcon
{
	private static final long serialVersionUID = 1L;
	private int x_pos;
	private int y_pos;
	private int width;
	private int height;
	private Icon fileIcon;

	public CloseTabIcon()
	{
		super("img/icon_close.png");
		width = 16;
		height = 16;
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		this.x_pos = x;
		this.y_pos = y;
		
		super.paintIcon(c, g, x, y);
	}

	public int getIconWidth()
	{
		return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);
	}

	public int getIconHeight()
	{
		return height;
	}

	public Rectangle getBounds()
	{
		return new Rectangle(x_pos, y_pos, width, height);
	}
}