package com.quui.utils.event;

public interface IEvent {
	public IDistributor getSource();

	public String getType();
}
