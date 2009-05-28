package com.quui.qlog.swing.gui.tab;

import com.quui.utils.event.Event;
import com.quui.utils.event.IDistributor;
import com.quui.utils.event.IEvent;

public class TabControllerEvent extends Event implements IEvent {
	public static final String TAB_CHANGED = "tabChanged";

	public TabControllerEvent(IDistributor source, String type) {
		super(source, type);
	}

}
