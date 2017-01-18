package bus.init;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import bus.model.Bus;
import bus.model.Person;

public class Initialization {
	private int numberPerson;
	private int numberBusStop;
	private int numberBus;
	private int maxCapacityBus;
	private Properties property;
	private FileInputStream input;
	private String fileName;
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void readParam() {
		try {
			property = new Properties();
			input = new FileInputStream(fileName);
			property.load(input);
			numberPerson = Integer.parseInt(property.getProperty("numberPersons"));
			numberBusStop = Integer.parseInt(property.getProperty("numberBusStops"));
			numberBus = Integer.parseInt(property.getProperty("numberBuses"));
			maxCapacityBus = Integer.parseInt(property.getProperty("busCapacity"));
		} catch (IOException ex) {
			System.err.println("Error of file property");
		}
	}
	
	public void createModel() {
		Creator.createBusStop(numberBusStop);
		Creator.createWay();
		Creator.createBus(numberBus, maxCapacityBus);
		Creator.createPerson(numberPerson, numberBusStop);
	}
	
	public void startMotion() {
		for (Person person: Creator.getListPerson()) {
			new Thread(person).start();
		}
		for (Bus bus: Creator.getListBus()) {
			new Thread(bus).start();
		}
	}
}