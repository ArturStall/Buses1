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
	private ArrayList<Integer> busyBusStops;
	private HashMap<Person, Integer> personsInWay;
	
	public Way(ArrayList<BusStop> busStops) {
		super();
		this.busStops = busStops;
		this.busyBusStops = new ArrayList<>();
		this.personsInWay = new HashMap<>();
	}
	
	public synchronized BusStop nextBusStop(Bus bus) {
		if (bus.isForward()) {
			countBusStops = bus.getCountBusStop();
			countBusStops++;
			bus.setCountBusStop(countBusStops);
			if (countBusStops == getBusStops().size()) {
				bus.setForward(false);
			}
			
			
		///////////////////////////////////////////////////////////////////////////	
			while (busyBusStops.contains((Integer)(countBusStops - 1))) {
				try {
					System.err.println("Автобус номер " + bus.getIdBus() + " ждет чтоб другой свалил");
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			busyBusStops.add((Integer)(countBusStops - 1));
			
			
			/////////////////////////////////////////////////////
			
			return getBusStops().get(countBusStops - 1);
		} else {
			countBusStops = bus.getCountBusStop();
			countBusStops--;
			bus.setCountBusStop(countBusStops);
			if (countBusStops - 1 == 0) {
				bus.setForward(true);
			}
			/////////////////////////////////////////////////////////////////////
			while (busyBusStops.contains((Integer)(countBusStops - 1))) {
				try {
					System.err.println("Автобус номер " + bus.getIdBus() + " ждет чтоб другой свалил");
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			busyBusStops.add((Integer)(countBusStops - 1));
			//////////////////////////////////////////////////////////////////
			return getBusStops().get(countBusStops - 1);
		}
    }		
	
/*	public synchronized ArrayList<Integer> getBusyBusStops() {
		return busyBusStops;
	}
	*/

	public synchronized void notifyBus(Bus bus) {
		busyBusStops.remove((Integer)bus.getIdCurrentBusStop());
		this.notifyAll();
	}

	public synchronized ArrayList<BusStop> getBusStops() {
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