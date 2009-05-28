package de.quui.swing.data;

import de.quui.server.IDataTransformer;
import de.quui.server.IDataTransformerFactory;

public class QLogDataTransformerFactory implements IDataTransformerFactory{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDataTransformer create() {
		return new QLogDataTransformer(new SwingMediator());
	}

}
