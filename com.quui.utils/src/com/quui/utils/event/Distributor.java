package com.quui.utils.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Distributor implements IDistributor {
	private List<ListenerContainer> _eventListeners;

	public Distributor() {
		_eventListeners = new ArrayList<ListenerContainer>();
	}

	public void register(String type, IListener eventListener) {
		if (!contains(eventListener, type)) {
			System.out.println(eventListener + " register for " + type);
			_eventListeners.add(new ListenerContainer(type, eventListener));
		}
	}

	private boolean contains(IListener eventListener, String type) {
		for (ListenerContainer container : _eventListeners) {
			if (container.getEventListener().equals(eventListener)
					&& container.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}

	public void unregister(String type, IListener eventListener) {
		for (int i = 0; i < _eventListeners.size(); i++) {
			ListenerContainer container = _eventListeners.get(i);
			if (container.getEventListener().equals(eventListener)
					&& container.getType().equals(type)) {
				_eventListeners.remove(i);
			}
		}
	}

	public void distribute(IEvent eventObject) {
		Vector<IListener> eventListeners = getEventListeners(eventObject.getType());
		for (int i = 0; i < eventListeners.size(); i++) {
			IListener listener = eventListeners.get(i);
			listener.onEvent(eventObject, listener);
		}
	}

	private Vector<IListener> getEventListeners(String type) {
		Vector<IListener> r = new Vector<IListener>();
		for (int i = 0; i < _eventListeners.size(); i++) {
			ListenerContainer listener = _eventListeners.get(i);
			if (listener.getType().equals(type)) {
				r.add(listener.getEventListener());
			}
		}

		return r;
	}

	public void finalize() {
		_eventListeners = new ArrayList<ListenerContainer>();
	}
}
