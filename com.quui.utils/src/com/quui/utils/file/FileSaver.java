package com.quui.utils.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver
{
	private String _content;
	private String _filename;

	public FileSaver(String content, String filename)
	{
		_content = content;
		_filename = filename;
		
		writeToFile();
	}
	
	private void writeToFile()
	{
		File f = new File(_filename);
		
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(_content.toCharArray());
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
