package bus.model.interfaces;

import java.util.ArrayList;

import bus.model.Bus;
import bus.model.Person;

public interface IBusStops {

	int getIdBusStop();

	Bus getCurrentBus();

	ArrayList<Person> getPersonsWaitBusOnBusStop();

	void personGoToBusStop(Person person);

	void personGoToBus(Person person);

	void personGoOutBus(Person person);

	void busGoToBusStop(Bus bus);
	
	void busWaitPersonOnBusStop(Bus bus);

	void busGoOutBusStop();	
}