package com.quui.utils.event;

public class Event implements IEvent {
	private IDistributor _source;
	private String _type;

	public Event(IDistributor source, String type) {
		_source = source;
		_type = type;
	}

	public String getType() {
		return _type;
	}

	public IDistributor getSource() {
		return _source;
	}
}
