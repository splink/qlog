package com.quui.utils.event;

import com.quui.utils.util.IDestroyable;

/**
 * Basic Interface for <code>IDistributor</code> implementations. An <code>IDistributor</code> can
 * register <code>IListener</code> and distribute <code>IEvent</code>s to them.
 * 
 * If the <code>IEvent</code> is distributed the <code>onEvent</code> method of all registred
 * <code>IListener</code> is invoked with the event as argument.
 * 
 * 
 * @author Max Kugland
 */
public interface IDistributor extends IDestroyable {

	/**
	 * Any object can register with an <code>IDistributor</code> to receive <code>IEvent</code>s.
	 * 
	 * @param type
	 *            the <code>IEvent</code>s type to register for
	 * @param listener
	 *            the <code>IListener</code> wishing to receive <code>IEvent</code>s
	 */
	public void register(String type, IListener listener);

	/**
	 * Unregister removes a previously registered listener.
	 * 
	 * @param type
	 *            the <code>IEvent</code>s type the listener has been registered with
	 * @param listener
	 *            the <code>IListener</code> to unregister
	 */
	public void unregister(String type, IListener listener);

	/**
	 * Distributes an <code>IEvent</code> to all currently registered listeners for that
	 * <code>IEvent</code>.
	 * 
	 * @param event
	 *            the <code>IEvent</code> which is distributed to registered listeners
	 */
	public void distribute(IEvent eventObject);
}
