package com.quui.utils.event;

/**
 * Basic Interface for events which can be distributed by <code>IDistributor</code>.
 * 
 * @author Max Kugland
 */
public interface IEvent {

	/**
	 * The source of the <code>IDevent</code> This might be useful if you want to pull data from the
	 * <code>IDistributor</code>
	 * 
	 * @return the <code>IDistributor</code> which has distributed the <code>IDevent</code>
	 */
	public IDistributor getSource();

	/**
	 * The type of the <code>IDevent</code> which is used by the <code>IDistributor</code> to
	 * distinguish between different <code>IDevent</code>s.
	 * 
	 * @return the type of the <code>IDevent</code>
	 */
	public String getType();
}
