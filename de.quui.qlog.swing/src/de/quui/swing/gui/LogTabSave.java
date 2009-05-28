package de.quui.swing.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.quui.swing.gui.tab.Tab;
import de.quui.utils.file.FileSaver;

public class LogTabSave
{
	private static final String HEAD = "<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'><head><title></title></head><body>";
	private static final String FOOT = "</body></html>";
	
	public LogTabSave(Window parent, Tab currentTab)
	{
		String filename = currentTab.getName();

		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Save Logfile for " + filename);
		fc.setDialogType(JFileChooser.SAVE_DIALOG);
		fc.setSelectedFile(new File(filename + ".html"));
		int returnVal = fc.showOpenDialog(parent);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if (fc.getSelectedFile().exists())
			{
				int ans = JOptionPane.showConfirmDialog(parent, ""
						+ fc.getSelectedFile().getName()
						+ " exists. Overwrite?", "Save Over Existing File",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (ans != JOptionPane.OK_OPTION)
				{
					return;
				}
			}

			String content = HEAD + currentTab.getTableBuilder().getContent() + FOOT;
			content = content.replaceAll("<title>", "<title>QLog - "+filename);
			content = content.replaceAll("<style[^<]+</style>" , "");
			content = content.replaceAll("</title>", "</title>"+currentTab.getTableBuilder().getCss());
			
			new FileSaver(content, fc.getSelectedFile().getAbsolutePath());
		}
	}
}
