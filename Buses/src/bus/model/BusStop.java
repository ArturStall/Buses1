package bus.model;

import java.util.ArrayList;

import bus.model.interfaces.IBusStops;

import org.apache.log4j.Logger;

public class BusStop implements IBusStops{
	
	Logger logBus = Logger.getLogger("bus");
	Logger logPerson = Logger.getLogger("person");
	
	private final int idBusStop;
	private boolean checkBus;
	private Bus currentBus;
	private ArrayList<Person> personsWaitBusOnBusStop;
	
	public BusStop(int idBusStop) {
		super();
		this.checkBus = false;
		this.currentBus = null;
		this.idBusStop = idBusStop;
		this.personsWaitBusOnBusStop = new ArrayList<>();
	}	

	public synchronized boolean isCheckBus() {	
		return checkBus;
	}

	public synchronized void setCheckBus(boolean checkBus) {
		this.checkBus = checkBus;
	}

	@Override
	public int getIdBusStop() {
		return idBusStop;
	}		
	
	public synchronized boolean setCurrentBus(Bus bus) {
		while (this.getCurrentBus() != bus ) {
			this.currentBus = bus;
		}
		return true;
	}

	@Override
	public synchronized Bus getCurrentBus() {
		return currentBus;
	}
	
	@Override	
	public synchronized ArrayList<Person> getPersonsWaitBusOnBusStop() {
		return personsWaitBusOnBusStop;
	}

	@Override
	public synchronized void personGoToBusStop(Person person) {
		this.getPersonsWaitBusOnBusStop().add(person);
	//	logPerson.info("A person # "+ person.getIdPerson() + " came to a bus stop # " + this.getIdBusStop() + " and need to bus stop # " + person.getIdTargetBusStop());
	}
	
	@Override
	public synchronized void personGoToBus(Person person) {
		try {
			while (!person.isInBus()) {			
				if (this.isCheckBus()) {
					if (this.getCurrentBus().getWay().checkPersonGoOutOnThisBusStop(this.getCurrentBus(), this) > 0) {
		//				logPerson.info("A person # "+ person.getIdPerson() + " waits for people out of the bus #" + currentBus.getIdBus());
						System.err.println("Жду чтоб вышли, человек № " + person.getIdPerson());
						wait();
					} else if (this.getCurrentBus().incrementCurrentCapacity()) {
						this.getPersonsWaitBusOnBusStop().remove(person);
						person.setInBus(true);
						this.getCurrentBus().getWay().setPersonsInWay(person, this.getCurrentBus());
		//				logPerson.info("A person # "+ person.getIdPerson() + " got on the bus # " + currentBus.getIdBus() + " from bus stop # " + this.getIdBusStop());
						System.err.println("Зашел человек № " + person.getIdPerson() + " в автобус номер " + this.getCurrentBus().getIdBus() + " с останови " + this.getIdBusStop() + " на останове - " + this.getPersonsWaitBusOnBusStop().size());
						if (this.getCurrentBus().getCurrentEmptyPlaces() == 0 | this.getPersonsWaitBusOnBusStop().size() == 0) {
							System.err.println("Зашел и пнул, в автобусе свободных мест - " + this.getCurrentBus().getCurrentEmptyPlaces());
							notifyAll();
						}
					} else {
		//				logPerson.info("A person # "+ person.getIdPerson() + " waited for the next bus because the bus # " + currentBus.getIdBus() + " ran the place");
						System.err.println("Остался ждать следующего, человек № " + person.getIdPerson());
						wait();
					}
				} else {
		//			logPerson.info("A person # "+ person.getIdPerson() + " waiting for the bus at the bus stop # " + this.getIdBusStop());
					System.err.println("Автобуса нет, жду, человек № " + person.getIdPerson() + " остановка # " + this.getIdBusStop());
					wait();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public synchronized void personGoOutBus(Person person) {
		try {
			while (person.isInBus()) {
				if(this.isCheckBus()) {
					if (this.getCurrentBus().getIdBus() == this.getCurrentBus().getWay().getPersonsInWay().get(person).intValue()) {
						this.getCurrentBus().decrementCurrentCapacity();
						if (this.getCurrentBus().getWay().removePersonsInWay(person)){
							person.setInBus(false);						
							if (this.getCurrentBus().getWay().checkPersonGoOutOnThisBusStop(this.getCurrentBus(), this) == 0) {
								System.err.println("Вышел и толкнул человек № " + person.getIdPerson() + " на остановке № " + this.getIdBusStop() + " собирался на остановке " + person.getIdTargetBusStop() + " людей выходящих тут в автобусе осталось - " + this.getCurrentBus().getWay().checkPersonGoOutOnThisBusStop(this.getCurrentBus(), this));
								this.notifyAll();
							} else {
								System.err.println("Вышел не толкая человек № " + person.getIdPerson() + " на остановке № " + this.getIdBusStop() + " собирался на остановке " + person.getIdTargetBusStop() + " людей выходящих тут в автобусе осталось - " + this.getCurrentBus().getWay().checkPersonGoOutOnThisBusStop(this.getCurrentBus(), this));
							}
						}
					}
				} else {
					wait();				
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void busGoToBusStop(Bus bus) {
		if (this.isCheckBus()) {
			System.err.println("Автобус" + bus.getIdBus() + ", остановка № " + this.getIdBusStop() + " ждет пока другой автобус уедет");
			while (this.isCheckBus()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.err.println("Поехал Автобус" + bus.getIdBus());
		}
		if (this.setCurrentBus(bus)){
			this.setCheckBus(true);
			this.getCurrentBus().setIdCurrentBusStop(this.getIdBusStop());
			System.err.println("Автобус" + bus.getIdBus() + ", остановка № " + this.getIdBusStop());
		}		
	}
	
	@Override
	public synchronized void busWaitPersonOnBusStop(Bus bus) {
		try {
	//		logBus.info("Bus # " + this.getCurrentBus().getIdBus() + " go to bus stop # " + getIdBusStop() + ", empty places in bus - " + this.getCurrentBus().getCurrentEmptyPlaces());
			if (this.getCurrentBus().getWay().checkPersonGoOutOnThisBusStop(this.getCurrentBus(), this) != 0) {
	//			logBus.info("Bus # " + this.getCurrentBus().getIdBus() + " wait for passengers go out from bus, empty places in bus - " + this.getCurrentBus().getCurrentEmptyPlaces());
				System.err.println("Автобус" + this.getCurrentBus().getIdBus() + ", остановка № " + this.getIdBusStop() + " людей в пути " + this.getCurrentBus().getWay().checkPersonsWaitBus() + " жду пока выйдут, в автобусе -  " + this.getCurrentBus().getWay().checkPersonGoOutOnThisBusStop(this.getCurrentBus(), this));
				this.notifyAll();
				wait();
				System.err.println("Автобус" + this.getCurrentBus().getIdBus() + " толкнут после выпуска людей");
			}
			if (this.getCurrentBus().getCurrentEmptyPlaces() != 0 & this.getPersonsWaitBusOnBusStop().size() != 0) {
//				logBus.info("Bus # " + this.getCurrentBus().getIdBus() + " waits for passengers sit from bus stop # " + getIdBusStop());
				System.err.println("Автобус" + this.getCurrentBus().getIdBus() + ", остановка № " + this.getIdBusStop() + " людей в пути " + this.getCurrentBus().getWay().checkPersonsWaitBus() + " жду пока зайдут, из автобуса хотят выйти тут -  " + this.getCurrentBus().getWay().checkPersonGoOutOnThisBusStop(this.getCurrentBus(), this) + " на остановке -" + this.getPersonsWaitBusOnBusStop().size());
				this.notifyAll();
				wait();
				System.err.println("Автобус" + this.getCurrentBus().getIdBus() + " толкнут после запуска людей, на остановке осталось - " + this.getPersonsWaitBusOnBusStop().size());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void busGoOutBusStop() {
		if (this.setCurrentBus(null)){
			this.setCheckBus(false);
			notifyAll();
		}
	}
}