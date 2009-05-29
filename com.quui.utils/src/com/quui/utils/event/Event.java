package com.quui.utils.event;

/**
 * Basic <code>IEvent</code> implementation
 * 
 * @see Devent
 * @see IDistributor
 * 
 * @author Max Kugland
 */
public class Event implements IEvent {
	private IDistributor _source;
	private Enum<? extends IEventType> _type;

	/**
	 * @param source
	 *            the <code>IDistributor</code> instance which originally fired the event
	 * @param type
	 *            the type of the event
	 */
	public Event(IDistributor source, Enum<? extends IEventType> type) {
		_source = source;
		_type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	public Enum<? extends IEventType> getType() {
		return _type;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDistributor getSource() {
		return _source;
	}
}
