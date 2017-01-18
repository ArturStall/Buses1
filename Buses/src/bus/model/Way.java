package bus.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bus.model.interfaces.IWays;
import bus.model.BusStop;

public class Way implements IWays{
	private int countBusStops;
	private ArrayList<BusStop> busStops;
	private HashMap<Person, Integer> personsInWay;
	
	public Way(ArrayList<BusStop> busStops) {
		super();
		this.busStops = busStops;
		this.personsInWay = new HashMap<>();
	}
	
	public synchronized BusStop nextBusStop(Bus bus) {
		if (bus.isForward()) {
			this.countBusStops = bus.getCountBusStop();
			this.countBusStops++;
			bus.setCountBusStop(this.countBusStops);
			if (this.countBusStops == this.getBusStops().size()) {
				bus.setForward(false);
			}
			return this.getBusStops().get(this.countBusStops - 1);
		} else {
			this.countBusStops = bus.getCountBusStop();
			this.countBusStops--;
			bus.setCountBusStop(this.countBusStops);
			if (this.countBusStops - 1 == 0) {
				bus.setForward(true);
			}
			return this.getBusStops().get(this.countBusStops - 1);
		}
    }
	
	public ArrayList<BusStop> getBusStops() {
		return busStops;
	}
	
	public synchronized HashMap<Person, Integer> getPersonsInWay() {
		return personsInWay;
	}
	
	public synchronized int checkPersonsWaitBus() {
		int numberPersonWaitBus = 0;
		for (int i = 0; i < getBusStops().size(); i++) {
			numberPersonWaitBus += getBusStops().get(i).getPersonsWaitBusOnBusStop().size();
		}
		return numberPersonWaitBus += getPersonsInWay().size();
	}
	
	public synchronized void setPersonsInWay(Person person, Bus bus) {
		this.getPersonsInWay().put(person, bus.getIdBus());
	}
	
	public synchronized boolean removePersonsInWay(Person person) {
		boolean check = false;
		for (Iterator<Map.Entry<Person, Integer>> iter = this.getPersonsInWay().entrySet().iterator(); iter.hasNext();) {
			Map.Entry<Person, Integer> entry = iter.next();
			if (entry.getKey().equals(person)) {
				iter.remove();
				check = true;
			}
		}
		return check;
	}
	
	public synchronized int checkPersonGoOutOnThisBusStop(Bus bus, BusStop busStop) {
		int check = 0;
		if (getPersonsInWay().size() == 0) {
			return check;
		} else {
			for (Map.Entry<Person, Integer> entry: this.getPersonsInWay().entrySet()) {
				if (entry.getValue() == bus.getIdBus()) {
					if (entry.getKey().getIdTargetBusStop() == busStop.getIdBusStop())
						check++;
				} else {
					return check;
				}
			}
		}
		return check;
	}
}