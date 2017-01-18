package bus.init;

import java.util.ArrayList;

import bus.model.Bus;
import bus.model.BusStop;
import bus.model.Person;
import bus.model.Way;

public class Creator {
	private static ArrayList<Person> listPerson;
	private static ArrayList<Bus> listBus;
	private static ArrayList<BusStop> listBusStop;
	private static Way way;

	protected static ArrayList<Person> getListPerson() {
		return listPerson;
	}

	protected static ArrayList<Bus> getListBus() {
		return listBus;
	}

	protected static void createBusStop(int numberBusStop) {
		int idBusStop;
		listBusStop = new ArrayList<>(numberBusStop);
		
		for (idBusStop = 0; idBusStop < numberBusStop; idBusStop++) {
			listBusStop.add(new BusStop(idBusStop));
		}
	}

	protected static void createWay() {
		way = new Way(listBusStop);
	}
	
	protected static void createBus(int numberBus, int maxCapacityBus) {
		int idBus;
		listBus = new ArrayList<>(numberBus);
		
		for (idBus = 0; idBus < numberBus; idBus++) {
			listBus.add(new Bus(idBus, maxCapacityBus, way));
		}
	}

	protected static void createPerson(int numberPerson, int numberBusStop) {
		int idPerson;	
		int currentBusStop;
		int targetBusStop;
		listPerson = new ArrayList<>(numberPerson);
		
		for (idPerson = 0; idPerson < numberPerson; idPerson++) {
			currentBusStop = (int)(Math.random() * (numberBusStop-1));
			targetBusStop = (int)(Math.random() * (numberBusStop-1));
			if (currentBusStop == targetBusStop){
				if (targetBusStop < (numberBusStop - 1)) {
					targetBusStop++;
				} else {
					targetBusStop--;
				}
			}
			listPerson.add(new Person(idPerson, currentBusStop, targetBusStop, way));
		}
	}
}