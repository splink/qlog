package com.quui.utils.event;

/**
 * Each class wishing to receive events from an <code>IDistributor</code> implementation needs to
 * implement the <code>IListener</code> interface.
 * 
 * @author maxmc
 */
public interface IListener {

	/**
	 * Invoked by the <code>IDistributor</code> the <code>IListener</code> has registered with when
	 * the event occurs.
	 * 
	 * @param event
	 *            the event object
	 */
	public void onEvent(IEvent event);
}
