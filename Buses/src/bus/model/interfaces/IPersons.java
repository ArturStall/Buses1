package bus.model.interfaces;

public interface IPersons extends Runnable{

	public int getIdCurrentBusStop();

	public void setIdCurrentBusStop(int idCurrentBusStop);

	public int getIdPerson();

	public int getIdTargetBusStop();

	public boolean isInBus();

	public void setInBus(boolean inBus);
}