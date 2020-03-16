abstract public class Event {

	private long evtTime;

	public Event(long eventTime) {

		evtTime = eventTime;

	}

	//Evento está pronto para ser executado se já passou do seu eventTime
	public boolean ready() {

		return System.currentTimeMillis() >= evtTime;

	}

	//Todo evento tem ação e descrição
	abstract public void action();

	abstract public String description();

}