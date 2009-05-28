package com.quui.qlog.swing.gui.popup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.quui.qlog.swing.gui.Window;
import com.quui.qlog.swing.gui.tab.ITab;


public class FilterPopUp implements ActionListener, IPopUp, WindowListener, KeyListener
{
	private ITab _currentTab;
	private JTextField _tf;
	private Window _parent;
	private JFrame _frame;

	public FilterPopUp(Window parent, ITab currentTab)
	{
		_parent = parent;
		_currentTab = currentTab;
		
		_parent.addPopUp(this);
		
		Dimension size = new Dimension(320, 100);
		JPanel content = new JPanel();
		_tf = new JTextField(currentTab.getFilter());

		JButton btn = new JButton("Filter");
		_tf.setPreferredSize(new Dimension(size.width
				- btn.getPreferredSize().width - 25, 20));

		btn.addActionListener(this);
		_tf.addActionListener(this);

		content.add(_tf, BorderLayout.NORTH);
		content.add(btn, BorderLayout.NORTH);
		content.validate();

		_frame = PopUpFactory.createPopup(parent, "Filter", size, content);
		_frame.addWindowListener(this);
		_tf.addKeyListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		_currentTab.applyFilter(_tf.getText());
	}

	public void setCurrentTab(ITab currentTab)
	{
		_currentTab = currentTab;
	}

	public void setText(String text)
	{
		_tf.setText(text);
	}

	public void windowActivated(WindowEvent arg0)
	{
	}

	public void windowClosed(WindowEvent arg0)
	{
	}

	public void windowClosing(WindowEvent arg0)
	{
		_parent.removePopUp(this);
	}

	public void windowDeactivated(WindowEvent arg0)
	{
	}

	public void windowDeiconified(WindowEvent arg0)
	{
	}

	public void windowIconified(WindowEvent arg0)
	{
	}

	public void windowOpened(WindowEvent arg0)
	{
	}

	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			_parent.removePopUp(this);
			_frame.dispose();
		}
	}

	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
