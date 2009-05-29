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
	private String _type;

	/**
	 * @param source
	 *            the <code>IDistributor</code> instance which originally fired the event
	 * @param type
	 *            the type of the event
	 */
	public Event(IDistributor source, String type) {
		_source = source;
		_type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		return _type;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDistributor getSource() {
		return _source;
	}
}
