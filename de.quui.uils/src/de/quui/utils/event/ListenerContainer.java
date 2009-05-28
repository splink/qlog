package de.quui.utils.event;

public class ListenerContainer {
	private String _type;
	private IListener _eventlistener;

	public ListenerContainer(String type, IListener eventlistener) {
		_type = type;
		_eventlistener = eventlistener;
	}

	public String getType() {
		return _type;
	}

	public IListener getEventListener() {
		return _eventlistener;
	}

}
