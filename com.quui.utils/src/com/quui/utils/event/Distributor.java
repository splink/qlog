package com.quui.utils.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * The <code>IDistributor</code> default implementation
 * 
 * @author maxmc
 */
public class Distributor implements IDistributor {
	private List<ListenerContainer> _eventListeners;

	public Distributor() {
		_eventListeners = new ArrayList<ListenerContainer>();
	}

	/**
	 * {@inheritDoc}
	 */
	public void register(Enum<? extends IEventType> type, IListener eventListener) {
		if (!contains(eventListener, type)) {
			System.out.println(eventListener + " register for " + type);
			_eventListeners.add(new ListenerContainer(type, eventListener));
		}
	}

	private boolean contains(IListener eventListener, Enum<? extends IEventType> type) {
		for (ListenerContainer container : _eventListeners) {
			if (container.getEventListener().equals(eventListener)
					&& container.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void unregister(Enum<? extends IEventType> type, IListener eventListener) {
		for (int i = 0; i < _eventListeners.size(); i++) {
			ListenerContainer container = _eventListeners.get(i);
			if (container.getEventListener().equals(eventListener)
					&& container.getType().equals(type)) {
				_eventListeners.remove(i);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void distribute(IEvent eventObject) {
		Vector<IListener> eventListeners = getEventListeners(eventObject.getType());
		for (int i = 0; i < eventListeners.size(); i++) {
			IListener listener = eventListeners.get(i);
			listener.onEvent(eventObject);
		}
	}

	private Vector<IListener> getEventListeners(Enum<? extends IEventType> type) {
		Vector<IListener> r = new Vector<IListener>();
		for (int i = 0; i < _eventListeners.size(); i++) {
			ListenerContainer listener = _eventListeners.get(i);
			if (listener.getType().equals(type)) {
				r.add(listener.getEventListener());
			}
		}

		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
		_eventListeners = new ArrayList<ListenerContainer>();
	}
}
