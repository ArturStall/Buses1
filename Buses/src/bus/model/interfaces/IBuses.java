package bus.model.interfaces;

import bus.model.Way;

public interface IBuses extends Runnable {

	public int getIdBus();

	public int getCurrentCapacity();

	public void setIdCurrentBusStop(int idCurrentBusStop);

	public boolean isForward();

	public void setForward(boolean forward);
	
	public int getCountBusStop();

	public void setCountBusStop(int countBusStop);

	public int getIdCurrentBusStop();

	public boolean checkEmptyPlace();

	public int getCurrentEmptyPlaces();

	public boolean incrementCurrentCapacity();

	public boolean decrementCurrentCapacity();

	public Way getWay();
}