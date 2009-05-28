package de.quui.swing.gui.tab;

import de.quui.utils.event.Event;
import de.quui.utils.event.IDistributor;
import de.quui.utils.event.IEvent;

public class TabControllerEvent extends Event implements IEvent {
	public static final String TAB_CHANGED = "tabChanged";

	public TabControllerEvent(IDistributor source, String type) {
		super(source, type);
	}

}
