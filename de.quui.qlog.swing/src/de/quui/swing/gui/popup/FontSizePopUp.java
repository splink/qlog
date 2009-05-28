package de.quui.swing.gui.popup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.quui.swing.gui.Window;
import de.quui.swing.gui.tab.ITab;

public class FontSizePopUp implements ActionListener, IPopUp, KeyListener
{
	private static int _fontSize = 12;
	
	public static void setFontSize(int fontSize)
	{
		_fontSize = fontSize;
	}
	
	public static int getFontSize()
	{
		return _fontSize;
	}
	
	private JTextField _tf;
	private List<ITab> _tabList;
	private JFrame _frame;

	public FontSizePopUp(Window parent, List<ITab> tabList)
	{
		_tabList = tabList;
		
		Dimension size = new Dimension(320, 100);
		JPanel content = new JPanel();

		_tf = new JTextField("" + _fontSize);

		JButton btn = new JButton("Change Size");
		_tf.setPreferredSize(new Dimension(size.width
				- btn.getPreferredSize().width - 25, 20));

		btn.addActionListener(this);
		_tf.addActionListener(this);
		_tf.addKeyListener(this);
		
		content.add(_tf, BorderLayout.NORTH);
		content.add(btn, BorderLayout.NORTH);
		content.validate();

		_frame = PopUpFactory.createPopup(parent, "Change Fontsize", size, content);
	}

	public void actionPerformed(ActionEvent e)
	{
		int size;
		try
		{
			size = Integer.parseInt(_tf.getText());
		}
		catch (Exception ex)
		{
			size = _fontSize;
		}
		
		for (ITab t : _tabList)
		{
			t.changeFontSize(size);
			t.applyFilter(t.getFilter());
		}
		
		_fontSize = size;
	}
	
	public void setCurrentTab(ITab currentTab)
	{
	}

	public void setText(String text)
	{
		_tf.setText(text);
	}

	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			_frame.dispose();
		}
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
	}
}
