package com.quui.qlog.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;

public class Filter
{
	public static String find(String f, TableBuilder tb)
	{
		if (f.equals(""))
		{
			return tb.getContent();
		}

		String line;
		String matches = "";
		Pattern pattern = Pattern.compile(f);
		BufferedReader r = new BufferedReader(new StringReader(tb.getContent()));
		try
		{
			while ((line = r.readLine()) != null)
			{
				if (pattern.matcher(line).find())
				{
					matches += line;
				}
			}
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return tb.getCss() + matches;
	}
}
