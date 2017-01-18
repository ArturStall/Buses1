package bus.model.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import bus.model.Bus;
import bus.model.BusStop;
import bus.model.Person;

public interface IWays {

	public BusStop nextBusStop(Bus bus);

	public ArrayList<BusStop> getBusStops();

	public HashMap<Person, Integer> getPersonsInWay();

//	public int checkPersonsWaitBus();

	public void setPersonsInWay(Person person, Bus bus);

	public boolean removePersonsInWay(Person person);

	public int checkPersonGoOutOnThisBusStop(Bus bus, BusStop busStop);
}