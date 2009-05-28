package de.quui.utils.event;

public interface IDistributor {
	public void register(String type, IListener listener);

	public void unregister(String type, IListener listener);

	public void distribute(IEvent eventObject);
}
