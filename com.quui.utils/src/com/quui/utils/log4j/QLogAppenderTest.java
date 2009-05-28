package com.quui.utils.log4j;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

public class QLogAppenderTest
{
	private static Logger logger = Logger.getRootLogger();

	public static void main(String[] args)
	{
		new QLogAppenderTest();
	}

	public QLogAppenderTest()
	{
		try
		{
			SimpleLayout layout = new SimpleLayout();
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			QLogSocketAppender qlogAppender = new QLogSocketAppender(
					"localhost", 6666, "QLog4j");
			logger.addAppender(qlogAppender);
			logger.addAppender(consoleAppender);
			// ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
//			logger.setLevel(Level.WARN);
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
		logger.debug("My Debug message");
		logger.info("My Info message");
		logger.warn("My Warn message");
		logger.error("My Error message");
		logger.fatal("My Fatal message");
	}
}
