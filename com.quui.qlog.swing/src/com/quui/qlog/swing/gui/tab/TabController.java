package com.quui.qlog.swing.gui.tab;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.quui.utils.event.Distributor;


public class TabController extends Distributor implements MouseListener, ChangeListener {
	private JTabbedPane _tabPane;
	private List<ITab> _tabList;

	int[] _keys = { KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4,
			KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9, };

	public TabController() {
		_tabList = new ArrayList<ITab>();
		_tabPane = new JTabbedPane();
		_tabPane.setOpaque(true);
		_tabPane.addMouseListener(this);
		_tabPane.addChangeListener(this);
	}

	public JTabbedPane getJTabbedPane() {
		return _tabPane;
	}

	public void addTab(ITab tab) {
		_tabList.add(tab);
		_tabPane.addTab(tab.getName(), tab.getIcon(), tab.getTabComponent());
		int index = _tabList.size() - 1;
		_tabPane.setMnemonicAt(index, _keys[index]);
		_tabPane.setSelectedComponent(tab.getTabComponent());
	}

	private void removeTab(ITab tab, int tabNumber) {
		_tabList.remove(tab);
		_tabPane.removeTabAt(tabNumber);
		tab.close();

		for (int i = 0; i < _tabPane.getTabCount(); i++)
			_tabPane.setMnemonicAt(i, _keys[i]);
	}

	public ITab getCurrentTab() {
		return getTabForComponent(_tabPane.getSelectedComponent());
	}

	private int getTabIndex(ITab tab) {
		for (int i = 0; i < _tabList.size(); i++) {
			if (_tabList.get(i).equals(tab)) {
				return i;
			}
		}
		return -1;
	}

	public ITab getTabForName(String name) {
		for (ITab t : _tabList) {
			if (t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}

	private ITab getTabForComponent(Component c) {
		for (ITab t : _tabList) {
			if (t.getTabComponent().equals(c)) {
				return t;
			}
		}
		return null;
	}

	public void stateChanged(ChangeEvent e) {
		int index = _tabPane.getSelectedIndex();
		if (index != -1)
			_tabPane.setBackgroundAt(index, Color.LIGHT_GRAY);

		distribute(new TabControllerEvent(this, TabControllerEvent.TAB_CHANGED));
	}

	public void mouseClicked(MouseEvent e) {
		int tabNumber = _tabPane.getUI().tabForCoordinate(_tabPane, e.getX(), e.getY());
		if (tabNumber < 0)
			return;
		Rectangle rect = ((CloseTabIcon) _tabPane.getIconAt(tabNumber)).getBounds();
		if (rect.contains(e.getX(), e.getY())) {
			Component c = _tabPane.getComponentAt(tabNumber);
			ITab tab = getTabForComponent(c);
			removeTab(tab, tabNumber);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public List<ITab> getTabList() {
		return _tabList;
	}

	public void notifyAboutIncomingMsg(String source) {
		ITab t = getTabForName(source);
		int index = getTabIndex(t);
		if (!getCurrentTab().equals(t)) {
			_tabPane.setBackgroundAt(index, Color.GRAY);
		}
	}
}
