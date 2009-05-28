package de.quui.swing.data;

import de.quui.server.IClientDataHandler;
import de.quui.server.IClientDataHandlerFactory;

public class QLogClientDataHandlerFactory implements IClientDataHandlerFactory{

	@Override
	public IClientDataHandler create() {
		return new QLogClientDataHandler(new SwingMediator());
	}

}
