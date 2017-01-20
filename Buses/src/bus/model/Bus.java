package bus.model;

import org.apache.log4j.Logger;

import bus.model.interfaces.IBuses;

public class Bus implements IBuses{
	
	Logger logWay = Logger.getLogger("way");
	
	private final int idBus;
	private int currentCapacity;
	private int idCurrentBusStop;
	private BusStop currentBusStop;
	private int countBusStop;
	private boolean forward;
	private final int maxCapacity;
	private Way way;
	
	public Bus(int idBus, int maxCapacity, Way way) {
		super();
		this.idBus = idBus;
		this.currentBusStop = null;
		this.countBusStop = 0;
		this.currentCapacity = 0;
		this.forward = true;
		this.maxCapacity = maxCapacity;
		this.way = way;
	}
	
	public synchronized BusStop getCurrentBusStop() {
		return currentBusStop;
	}

	public int getIdBus() {
		return idBus;
	}
	
	public synchronized int getCurrentCapacity() {
		return currentCapacity;
	}	
	
	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setIdCurrentBusStop(int idCurrentBusStop) {
		this.idCurrentBusStop = idCurrentBusStop;
	}
	
	public int getCountBusStop() {
		return countBusStop;
	}
	
	public void setCountBusStop(int countBusStop) {
		this.countBusStop = countBusStop;
	}
	
	public boolean isForward() {
		return forward;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}

	public int getIdCurrentBusStop() {
		return idCurrentBusStop;
	}
	
	public synchronized boolean checkEmptyPlace() {
		if (this.getCurrentCapacity() < this.getMaxCapacity()) {
			return true;
		} else return false;
	}
	
	public synchronized int getCurrentEmptyPlaces() {
		return this.getMaxCapacity() - this.getCurrentCapacity();
	}
	
	public synchronized boolean incrementCurrentCapacity() {
		if (this.checkEmptyPlace()) {
			this.currentCapacity++;
			return true;
		} else return false;
	}
	
	public synchronized boolean decrementCurrentCapacity() {
		if (this.getCurrentCapacity() > 0) {
			this.currentCapacity--;
			return true;
		} else return false;
	}
	
	public Way getWay() {
		return way;
	}
	
	@Override
	public void run() {
		while (true) {
			this.currentBusStop = getWay().nextBusStop(this);
		//	getWay().waitBus(this, currentBusStop.getIdBusStop());
			getCurrentBusStop().busGoToBusStop(this);
			getCurrentBusStop().busWaitPersonOnBusStop(this);
			getCurrentBusStop().busGoOutBusStop();
			getWay().notifyBus(this);
		//	logWay.info("Number persons in motion while bus on way - " + way.checkPersonsWaitBus());
			if (this.getIdCurrentBusStop() == 0 && this.getWay().checkPersonsWaitBus() == 0) {
				System.err.println("---------------------------------------Автобус номер " + this.idBus + " Законил");
				break;
			}			
		}
	}	
}