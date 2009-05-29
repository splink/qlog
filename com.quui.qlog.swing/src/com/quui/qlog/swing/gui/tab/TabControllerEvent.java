package com.quui.qlog.swing.gui.tab;

import com.quui.utils.event.Event;
import com.quui.utils.event.IDistributor;
import com.quui.utils.event.IEvent;
import com.quui.utils.event.IEventType;

public class TabControllerEvent extends Event implements IEvent {
	public enum Tab implements IEventType {TAB_CHANGED};

	public TabControllerEvent(IDistributor source, Enum<? extends IEventType> type) {
		super(source, type);
	}

}
