
public class Message {

	private String timeStamp;
	private String seqkey;
	private String SimCard;
	private String service;
	private String timeduration;
	private String byteusage;
	private String location;
	private String equipment;
	private String AsText;
	private String destinationCom;
	
	public Message(String timeStamp, String seqkey, String simCard, String service, String timeduration, String byteusage,
			String location, String equipment) {
		super();
		this.timeStamp = timeStamp;
		this.seqkey = seqkey;
		SimCard = simCard;
		this.service = service;
		this.timeduration = timeduration;
		this.byteusage = byteusage;
		this.location = location;
		this.equipment = equipment;
	}







	


	
	public Message()
	{
		super();

		timeStamp = null;
		seqkey = null;
		SimCard = null;
		service = null;
		location = null;
		equipment = null;
		destinationCom = null;
		timeduration = null;
		byteusage = null;
		
	}




	

	public String getSeqKey() {
		return seqkey;
	}

	public void setSeqKey(String seqkey) {
		this.seqkey = seqkey;
	}






	public String getTimeStamp() {
		return timeStamp;
	}






	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}






	public String getSimCard() {
		return SimCard;
	}






	public void setSimCard(String simCard) {
		SimCard = simCard;
	}






	public String getService() {
		return service;
	}






	public void setService(String service) {
		this.service = service;
	}






	public String getTimeduration() {
		return timeduration;
	}






	public void setTimeduration(String timeduration) {
		this.timeduration = timeduration;
	}






	public String getByteusage() {
		return byteusage;
	}






	public void setByteusage(String byteusage) {
		this.byteusage = byteusage;
	}






	public String getLocation() {
		return location;
	}






	public void setLocation(String location) {
		this.location = location;
	}






	public String getEquipment() {
		return equipment;
	}






	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}






	@Override
	public String toString() {
		return "Message [timeStamp=" + timeStamp + ", seqkey=" + seqkey + ", SimCard=" + SimCard + ", service="
				+ service + ", timeduration=" + timeduration + ", byteusage=" + byteusage + ", location=" + location
				+ ", equipment=" + equipment + "]";
	}











	public String getAsText() {
		return AsText;
	}











	public void setAsText(String asText) {
		AsText = asText;
	}











	public String getDestinationCom() {
		return destinationCom;
	}











	public void setDestinationCom(String destinationCom) {
		this.destinationCom = destinationCom;
	}




	
	
}
