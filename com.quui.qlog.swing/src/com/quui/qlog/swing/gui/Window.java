package com.quui.qlog.swing.gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.quui.qlog.swing.gui.popup.FilterPopUp;
import com.quui.qlog.swing.gui.popup.FontSizePopUp;
import com.quui.qlog.swing.gui.popup.IPopUp;
import com.quui.qlog.swing.gui.popup.MessagePane;
import com.quui.qlog.swing.gui.tab.ITab;
import com.quui.qlog.swing.gui.tab.Tab;
import com.quui.qlog.swing.gui.tab.TabController;
import com.quui.qlog.swing.gui.tab.TabControllerEvent;
import com.quui.qlog.ui.PropertiesReader;
import com.quui.utils.event.IEvent;
import com.quui.utils.event.IListener;

public class Window extends JFrame implements ActionListener, IListener {
	private static final long serialVersionUID = 1L;

	private ImageIcon _checkIcon = new ImageIcon("img/check.png");
	private List<IPopUp> _openPopUps = new ArrayList<IPopUp>();
	private boolean _clearOnConnect;
	private boolean _alwaysOnTop;
	private boolean _scrollLock;
	private TabController _tabCtrl;
	private ButtonEnabler _enabler;

	public Window(PropertiesReader reader) {
		setLocation(new Point(reader.getX(), reader.getY()));

		_clearOnConnect = reader.getClearOnConnect();
		_alwaysOnTop = reader.getAlwaysOnTop();
		_scrollLock = reader.getScrollLock();

		setTitle("QLog");

		_enabler = new ButtonEnabler();

		_tabCtrl = new TabController();
		_tabCtrl.register(TabControllerEvent.Tab.TAB_CHANGED, this);

		Look.createWindowLook(this);
		createGui();
		add(_tabCtrl.getJTabbedPane());
	}

	public void onEvent(IEvent event) {
		setSelectedTab();
	}

	private void setSelectedTab() {
		ITab tab = _tabCtrl.getCurrentTab();

		if (tab != null) {
			tab.setScrollLock(_scrollLock);
			for (IPopUp popup : _openPopUps) {
				popup.setCurrentTab(tab);
				if (popup.getClass().equals(FilterPopUp.class)) {
					popup.setText(tab.getFilter());
				}
			}
		}
		_enabler.process(tab, ButtonFactory.getItems());
	}

	public void notifyAboutIncomingMsg(String source) {
		_tabCtrl.notifyAboutIncomingMsg(source);
	}

	public void addTab(ITab tab) {
		_tabCtrl.addTab(tab);
	}

	public List<IPopUp> getOpenPopUpList() {
		return _openPopUps;
	}

	public void addPopUp(IPopUp popup) {
		_openPopUps.add(popup);
	}

	public void removePopUp(IPopUp popup) {
		_openPopUps.remove(popup);
	}

	public ITab getTabForName(String name) {
		return _tabCtrl.getTabForName(name);
	}

	private void createGui() {
		createMenu();
		setSelectedTab();
		BorderLayout border = new BorderLayout();
		setLayout(border);
	}

	private void createMenu() {
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		menubar.add(menu);

		JMenu window = new JMenu("Window");
		window.setMnemonic(KeyEvent.VK_W);
		menubar.add(window);

		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		menubar.add(help);

		help.add(ButtonFactory.create(ButtonFactory.ABOUT, this));
		menu.add(ButtonFactory.create(ButtonFactory.CLEAR, this));
		JMenuItem clearOnConnect = menu.add(ButtonFactory.create(ButtonFactory.CLEAR_ON_CONNECT,
				this));
		menu.add(ButtonFactory.create(ButtonFactory.FILTER, this));
		JMenuItem alwaysOnTop = window.add(ButtonFactory.create(ButtonFactory.ALWAYS_ON_TOP, this));
		window.add(ButtonFactory.create(ButtonFactory.SAVE_LOG, this));
		window.add(ButtonFactory.create(ButtonFactory.CHANGE_FONTSIZE, this));
		JMenuItem scrollLock = window.add(ButtonFactory.create(ButtonFactory.SCROLL_LOCK, this));
		setJMenuBar(menubar);

		if (_clearOnConnect)
			clearOnConnect.setIcon(_checkIcon);
		if (_scrollLock) {
			scrollLock.setIcon(_checkIcon);
			if (_tabCtrl.getCurrentTab() != null)
				_tabCtrl.getCurrentTab().setScrollLock(_scrollLock);
		}
		if (_alwaysOnTop) {
			setAlwaysOnTop(_alwaysOnTop);
			alwaysOnTop.setIcon(_checkIcon);
		}
	}

	/**
	 * Called if a button is invoked
	 * 
	 * @param evt
	 *            carrys information to distinguish which button has been invoked
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand() == "scrollLock") {
			handleScrollLock((JMenuItem) evt.getSource());
		} else if (evt.getActionCommand() == "savefile") {
			if (_tabCtrl.getCurrentTab() != null) {
				if (_tabCtrl.getCurrentTab().getClass().equals(Tab.class))
					new LogTabSave(this, (Tab) _tabCtrl.getCurrentTab());
				else
					MessagePane.createTreeTabErrorDialog(this);
			} else
				MessagePane.createTabErrorDialog(this);
		} else if (evt.getActionCommand().equals("clear")) {
			if (_tabCtrl.getCurrentTab() != null)
				_tabCtrl.getCurrentTab().clear();
		} else if (evt.getActionCommand().equals("fontsize")) {
			if (_tabCtrl.getCurrentTab() != null)
				new FontSizePopUp(this, _tabCtrl.getTabList());
			else
				MessagePane.createTabErrorDialog(this);
		} else if (evt.getActionCommand().equals("clearonconnect")) {
			handleClearOnConnect((JMenuItem) evt.getSource());
		} else if (evt.getActionCommand().equals("alwaysontop")) {
			handleAlwaysOnTop((JMenuItem) evt.getSource());
		} else if (evt.getActionCommand().equals("about")) {
			MessagePane.createInfoDialog(this);
		} else if (evt.getActionCommand().equals("filter")) {
			if (_tabCtrl.getCurrentTab() != null)
				new FilterPopUp(this, _tabCtrl.getCurrentTab());
			else
				MessagePane.createTabErrorDialog(this);
		}
	}

	private void handleAlwaysOnTop(JMenuItem item) {
		_alwaysOnTop = !_alwaysOnTop;
		setAlwaysOnTop(_alwaysOnTop);

		if (_alwaysOnTop)
			item.setIcon(_checkIcon);
		else
			item.setIcon(null);
	}

	private void handleClearOnConnect(JMenuItem item) {
		_clearOnConnect = !_clearOnConnect;

		if (_clearOnConnect)
			item.setIcon(_checkIcon);
		else
			item.setIcon(null);
	}

	private void handleScrollLock(JMenuItem item) {
		_scrollLock = !_scrollLock;
		if (_tabCtrl.getCurrentTab() != null)
			_tabCtrl.getCurrentTab().setScrollLock(_scrollLock);

		if (_scrollLock)
			item.setIcon(_checkIcon);
		else
			item.setIcon(null);
	}

	public boolean getClearOnConnect() {
		return _clearOnConnect;
	}

	public boolean getAlwaysOnTop() {
		return _alwaysOnTop;
	}
}