package com.quui.utils.event;

public interface IListener {
	public void onEvent(IEvent event, IListener listener);
}
