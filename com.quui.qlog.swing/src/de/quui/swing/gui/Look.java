package de.quui.swing.gui;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Look
{
	public static void createWindowLook(JFrame frame)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());// getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.out.println("Unable to load native look and feel");
		}

		String filename = "img/icon16x16.gif";
		Image image = Toolkit.getDefaultToolkit().getImage(filename);
		frame.setIconImage(image);
	}
}
