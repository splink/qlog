package com.quui.qlog.swing.data;

import com.quui.qlog.core.data.QLogDataTransformer;
import com.quui.server.IDataTransformer;
import com.quui.server.IDataTransformerFactory;

public class QLogDataTransformerFactory implements IDataTransformerFactory{
	
	/**
	 * {@inheritDoc}
	 */
	public IDataTransformer create() {
		return new QLogDataTransformer(new SwingMediator());
	}

}
