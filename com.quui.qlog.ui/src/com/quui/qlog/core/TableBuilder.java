package com.quui.qlog.core;

import java.util.regex.Pattern;



public class TableBuilder
{
	private String _filter = "";
	private String _content = "";
	private int _fontSize;
	private String _css;
	private int _initialFontSize;
	
	public TableBuilder(int fontSize)
	{
	    // The only Swing-related dependency was here: FontSizePopUp.getFontSize();
	    // Replaced by a constructor taking the size
		_initialFontSize = _fontSize = fontSize;
		_css = "<style type='text/css'>table { width:100%; font-family: arial, sans-serif; font-size:" + _fontSize + "px; }</style>";
		_content =  _css;
	}
	
	public void setFilter(String filter)
	{
		_filter = filter;
	}
	
	public void changeFontSize(int fontSize)
	{
		_content = _content.replaceAll(
				"font-size:" + _fontSize + "px;",
				"font-size:" + fontSize + "px;");
		_fontSize = fontSize;
	}
	
	public String wrap(String message, String color)
	{
		return "<table><tr><td bgcolor='" + color + "'>" + message
		+ "</td></tr></table>";
	}

	public String buildHTML(String color, String message)
	{
		String newMsg = wrap(message, validateColor(color))
				+ System.getProperty("line.separator");
		_content += newMsg;

		if (!filter(newMsg))
			return null;
		return newMsg;
	}

	private String validateColor(String color)
	{
		if (color.equals(null))
			color = "#ffffff";
		color = color.replace("0x", "#");
		if (color.length() < 7)
		{
			for (int i = 0; i < (7 - color.length()); i++)
			{
				color += "0";
			}
		}

		return color;
	}

	private boolean filter(String str)
	{
		if (_filter.equals(""))
			return true;
		Pattern pattern = Pattern.compile(_filter);
		if (pattern.matcher(str).find())
			return true;
		else
			return false;
	}

	public void clear()
	{
		_content =  _css;
		int newFontSize = _fontSize;
		_fontSize = _initialFontSize;
		changeFontSize(newFontSize);
	}

	public String getContent()
	{
		return _content;
	}

	public String getFilter()
	{
		return _filter;
	}
	
	public String getCss()
	{
		return _css.replaceAll(
				"font-size:" + _initialFontSize + "px;",
				"font-size:" + _fontSize + "px;");
	}
}
