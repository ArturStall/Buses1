package bus.model;

import bus.model.interfaces.IPersons;

public class Person implements IPersons{
	private final int idPerson;
	private int idCurrentBusStop;
	private final int idTargetBusStop;
	private Way way;
	private boolean inBus;
	
 	public Person(int idPerson, int idCurrentBusStop, int idTargetBusStop, Way way) {
		super();
		this.idPerson = idPerson;
		this.idCurrentBusStop = idCurrentBusStop;
		this.idTargetBusStop = idTargetBusStop;
		this.way = way;
		this.inBus = false;
	}
	
	public int getIdCurrentBusStop() {
		return idCurrentBusStop;
	}
	
	public void setIdCurrentBusStop(int idCurrentBusStop) {
		this.idCurrentBusStop = idCurrentBusStop;
	}
	
	public int getIdPerson() {
		return idPerson;
	}
	
	public int getIdTargetBusStop() {
		return idTargetBusStop;
	}
	
	public boolean isInBus() {
		return inBus;
	}
	
	public void setInBus(boolean inBus) {
		this.inBus = inBus;
	}	
	
	public Way getWay() {
		return way;
	}

	@Override
	public void run() {
		getWay().getBusStops().get(idCurrentBusStop).personGoToBusStop(this);
		getWay().getBusStops().get(idCurrentBusStop).personGoToBus(this);
		getWay().getBusStops().get(idTargetBusStop).personGoOutBus(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idCurrentBusStop;
		result = prime * result + idPerson;
		result = prime * result + idTargetBusStop;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (idCurrentBusStop != other.idCurrentBusStop)
			return false;
		if (idPerson != other.idPerson)
			return false;
		if (idTargetBusStop != other.idTargetBusStop)
			return false;
		return true;
	}
}