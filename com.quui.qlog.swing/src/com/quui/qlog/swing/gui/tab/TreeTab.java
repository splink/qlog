package com.quui.qlog.swing.gui.tab;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.quui.qlog.swing.gui.Window;
import com.quui.qlog.ui.PropertiesReader;


public class TreeTab implements ITreeTab {
	private JTree _xmlTree;
	private Document _xmlDoc;
	private JScrollPane _scrollPane;
	private Window _window;
	private String _name;
	private String _filter = "";
	private String _defaultFilter = "";

	public TreeTab(Window window, PropertiesReader reader, String name) {
		_window = window;
		_name = name;

		_scrollPane = createTabContent();
		_window.addTab(this);
	}

	public void setXmlDoc(Document xmlDoc) {
		_xmlDoc = xmlDoc;

		NodeList children = _xmlDoc.getFirstChild().getChildNodes();
		Node rootNode = children.item(0);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode.getNodeName());
		((DefaultTreeModel) _xmlTree.getModel()).setRoot(root);

		children = rootNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			processElement(children.item(i), root);
		}

		_defaultFilter = rootNode.getNodeName();
		System.out.println(_defaultFilter);
		applyFilter(_filter);
		_window.notifyAboutIncomingMsg(_name);
	}

	public void incomingCommand(String command) {
	}

	public void incomingMessage(String color, String message) {
	}

	private JScrollPane createTabContent() {
		_xmlTree = new JTree();

		JScrollPane scrollPane = new JScrollPane(_xmlTree,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		return scrollPane;
	}

	private void processElement(Node node, DefaultMutableTreeNode dmtn) {
		DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(node.getNodeName().replace(
				"ı", ""));

		String text = null;
		if (node.getNodeType() == 3) {
			text = node.getNodeValue();
			text = text.replace(" ", "");
			text = text.replace("\n", "");

			if ((text != null) && (!text.equals(""))) {
				currentNode.add(new DefaultMutableTreeNode(text));
			} else
				return;
		}

		processAttributes(node, currentNode);

		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			processElement(children.item(i), currentNode);
		}

		dmtn.add(currentNode);
	}

	private void processAttributes(Node node, DefaultMutableTreeNode dmtn) {
		NamedNodeMap atts = node.getAttributes();
		if (atts == null)
			return;
		for (int i = 0; i < atts.getLength(); i++) {
			Node att = atts.item(i);
			DefaultMutableTreeNode attNode = new DefaultMutableTreeNode("@"
					+ att.getNodeName().replace("ı", ""));
			attNode.add(new DefaultMutableTreeNode(att.getNodeValue()));
			dmtn.add(attNode);
		}
	}

	public void close() {
		_scrollPane = null;
		_xmlTree = null;
		_xmlDoc = null;
	}

	public void clear() {
	}

	public JScrollPane getTabComponent() {
		return _scrollPane;
	}

	public String getName() {
		return _name;
	}

	public Icon getIcon() {
		return new CloseTabIcon();
	}

	public void setScrollLock(boolean lock) {
	}

	public void applyFilter(String filter) {
		if (filter.equals(""))
			filter = _defaultFilter;
		_filter = filter;
		searchAndExpand(_filter);
	}

	public void searchAndExpand(String text) {
		TreeNode[] path = search((DefaultMutableTreeNode) _xmlTree.getModel().getRoot(), text);

		if (path != null) {
			TreePath treePath = new TreePath(path);

			_xmlTree.scrollPathToVisible(treePath);
			_xmlTree.setSelectionPath(treePath);
		}
	}

	private TreeNode[] search(DefaultMutableTreeNode node, Object object) {
		TreeNode[] path = null;

		if (node.getUserObject().equals(object)) {
			path = ((DefaultTreeModel) _xmlTree.getModel()).getPathToRoot(node);
		} else {
			int i = 0;
			int n = _xmlTree.getModel().getChildCount(node);

			while ((i < n) && (path == null)) {
				path = search((DefaultMutableTreeNode) _xmlTree.getModel().getChild(node, i),
						object);

				i++;
			}
		}

		return path;
	}

	public void changeFontSize(int size) {
		Font tf = new Font("sansserif", Font.PLAIN, size);
		_xmlTree.setFont(tf);
	}

	public String getFilter() {
		return _filter;
	}
}