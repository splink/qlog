package com.quui.utils.util;

/**
 * By implementing the <code>IDestroyable</code> a class signs the contract to provide
 * a <code>destroy</code> method which is in charge of cleaning up the resources used by the 
 * implementing class. 
 * 
 * @author maxmc
 */
public interface IDestroyable {
	
	/**
	 * Finalizes the class frees all used resources. 
	 */
	public void destroy();
}
