import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.lang.Long;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer; 
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig; 
import org.apache.kafka.clients.producer.ProducerRecord; 
import org.apache.kafka.clients.producer.RecordMetadata; 
import org.apache.kafka.common.serialization.LongSerializer; 
import org.apache.kafka.common.serialization.StringSerializer;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class IoTaaSMessageProvider {

	static String telcoproviderName = null;
	static String brokerList = "localhost:9092";
	static String topic = null;
	static String hlrServer = null;
	static String hlrDatabase = null;
	static String hlrUsername = null;
	static String hlrPassword = null;
	static String hlrTable = null;
	static int throughput = 10;
	static String typeMessage = "JSON";
	static boolean aliens = false;
	static boolean statusSIMCARD = false;
	static HashMap <String , String> Subscribers = new HashMap <String , String>();
	


	private static String RandomSubscriberAlien() 
	{
		String simcard = new String("");
		DecimalFormat decimalFormat = new DecimalFormat("#0");
		simcard = "5" +	decimalFormat.format( new Float(10000000 + new Random().nextFloat() * 90000000) ) ;
		return simcard;
	}



	private static String RandomSubscriber(HashMap<String, String> subscribers2) 
	{
		String simcard = new String("");
		int index = (new Random()).nextInt(Subscribers.size());		
		Set<String> keys = Subscribers.keySet();
		Iterator<String> it = keys.iterator();
		for (int idx= 0 ; idx < index ;idx++) it.next();
		simcard =  (String) it.next();
		System.out.println("Simcard randomized: " + simcard);
		return simcard;
	}


	private static String CreateStatusMessage(String m_typeMessage, HashMap<String, String> subscribers2) 
	{		
		String newstatusMessage = new String("");
		String simcard;
		
		if (m_typeMessage.compareTo("JSON") == 0)
			newstatusMessage += "{\"StatusSIMCARD\":{" +		
							     "\"SIMCARD\":{" ;		
		
		else if (m_typeMessage.compareTo("XML") == 0)		
			newstatusMessage += "<StatusSIMCARD>" +
							    "<SIMCARD>";
		else 
		{
			System.out.println("Type of message not identified.");
			return (null);
		}
		
		Set<String> keys = subscribers2.keySet();
		Iterator<String> it = keys.iterator();
		
		for (int idx = 0 ; idx < keys.size() ;idx++)
		{
			simcard =  (String) it.next();
			
			if (m_typeMessage.compareTo("JSON") == 0)
			{
				newstatusMessage += "\"" + simcard + "\":\"" + new Random().nextInt(2) + "\"";
				if (it.hasNext()) newstatusMessage += "," ;
			}
			else if (m_typeMessage.compareTo("XML") == 0)
				newstatusMessage += "<" + simcard + ">" +  new Random().nextInt(2)  + "</" + simcard + ">";	
		}
		
		if (m_typeMessage.compareTo("JSON") == 0)
			newstatusMessage += "}}}"; 		
		
		else if (m_typeMessage.compareTo("XML") == 0)		
			newstatusMessage += "</SIMCARD></StatusSIMCARD>";
		
		return newstatusMessage;
	}

	
	private static Message CreateMessage(String m_typeMessage , String Simcard , Timestamp ts)
	{
		Message newMessage;
		newMessage = new Message();
		newMessage.setSimCard(Simcard);
		newMessage.setSeqKey(Subscribers.get(Simcard));
		newMessage.setTimeStamp(ts.toString());
		String[] locationOptions = {"Alameda", "Tagus","CTN","Arco Cego","Saldanha","Oeiras estação","Marquês do Pombal","Unknown"};
		newMessage.setLocation(locationOptions[new Random().nextInt(locationOptions.length)]);
		String[] equipmentOptions = {"Mobile Phone", "Hotspot","Custom","Laptop","Raspberry Pi","Unknown"};
		newMessage.setEquipment(equipmentOptions[new Random().nextInt(equipmentOptions.length)]);
		String[] serviceOptions = {"voice","data","sms","mms"};
		newMessage.setService(serviceOptions[new Random().nextInt(serviceOptions.length)]);
		
		DecimalFormat decimalFormat = new DecimalFormat("#0");	
		newMessage.setDestinationCom( "9" +	decimalFormat.format( new Float(10000000 + new Random().nextFloat() * 90000000) ) );
		if (newMessage.getService().compareTo("voice") == 0)
			newMessage.setTimeduration( new Integer(new Random().nextInt(200)).toString() );
		else if (newMessage.getService().compareTo("data") == 0)
			newMessage.setByteusage( new Integer(new Random().nextInt(1048576)).toString() );	
		else if (newMessage.getService().compareTo("mms") == 0)
			newMessage.setByteusage(new Integer(new Random().nextInt(4096)).toString() );	
		
		if (m_typeMessage.compareTo("JSON") == 0)
		{
			
			newMessage.setAsText(
				"{\"CallDataRecord\":{" +			
					"\"timeStamp\":\""+ newMessage.getTimeStamp() + "\"," + 
					"\"SimCard\":\""+ newMessage.getSimCard() + "\"," + 
					"\"service\":\""+ newMessage.getService() + "\","  + 
					"\"timeduration\":\""+ newMessage.getTimeduration() + "\","  + 
					"\"byteusage\":\""+ newMessage.getByteusage() + "\","  + 
					"\"location\":\""+ newMessage.getLocation() + "\"," + 
					"\"equipment\":\""+ newMessage.getEquipment() + "\","  + 
					"\"destinationCom\":\""+	newMessage.getDestinationCom() +"\"" + 		
				"}}"	
			);		
		}
		else if (m_typeMessage.compareTo("XML") == 0)
		{
			newMessage.setAsText(
				"<CallDataRecord>"+ 
				"<timeStamp>"+ newMessage.getTimeStamp() + "</timeStamp>" + 
				"<SimCard>"+ newMessage.getSimCard() + "</SimCard>" + 
				"<service>"+ newMessage.getService() + "</service>"  + 
				"<timeduration>"+ newMessage.getTimeduration() + "</timeduration>"  + 
				"<byteusage>"+ newMessage.getByteusage() + "</byteusage>"  + 
				"<location>"+ newMessage.getLocation() +"</location>" + 
				"<equipment>"+ newMessage.getEquipment() + "</equipment>"  + 
				"<destinationCom>"+	newMessage.getDestinationCom() +"</destinationCom>" + 	
				"</CallDataRecord>"   );
		}
		else 
		{
			System.out.println("Type of message not identified.");
			return (null);
		}

		return (newMessage);
	}
	
	
	private static HashMap <String , String> ReadFromDB(HashMap <String , String> oldsubscribers) 
	{
		oldsubscribers = null;
		
		HashMap <String , String> newsubscribers = new HashMap <String , String>();
		
		Connection conn = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + hlrServer + "/" + hlrDatabase , hlrUsername , hlrPassword);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + hlrTable);
			while (rs.next())
				newsubscribers.put( rs.getString("SIMCARD") , rs.getString("MSISDN"));
			conn.close();			
		}
		catch (SQLException sqle) { System.out.println("SQLException: " + sqle); }
		catch (ClassNotFoundException e) { e.printStackTrace();}
		
		System.out.println("Subscriber list updated, with " + newsubscribers.size() + " subscribers.");
		
		return newsubscribers;
	}
	
	
	private static void CheckArguments()
	{
		System.out.println(  "--telecommunications-provider-name=" + telcoproviderName + "\n" +
							 "--broker-list=" + brokerList + "\n" +
							 "--topic=" + 	topic + "\n" +
							 "--hlr-server=" + hlrServer + "\n" +
							 "--hlr-database=" + hlrDatabase + "\n" +
							 "--hlr-username=" + hlrUsername + "\n" +
							 "--hlr-password=" + hlrPassword + "\n" +
							 "--hlr-table=" + hlrTable + "\n" +
							 "--throughput=" + throughput + "\n" +
							 "--typeMessage=" +  typeMessage + "\n" +
							 "--statusSIMCARD=" + statusSIMCARD + "\n" +
							 "--aliens=" + aliens);
	}
	
	
	private static boolean VerifyArgs(String[] cabecalho)
	{
		boolean result = true;
		boolean mandatorytelcoprovidername = false;
		boolean mandatorytopic = false;
		boolean mandatoryhlrServer = false;
		boolean mandatoryhlrDatabase = false;
		boolean mandatoryhlrUsername = false;
		boolean mandatoryhlrPassword = false;
		boolean mandatoryhlrTable = false;

		for (int i=0 ; i < cabecalho.length ; i=i+2)
		{
	//		System.out.println("i=" + i + " = " + cabecalho[i]);
	//		System.out.println("i=" + i+1 + " = " + cabecalho[i+1]);
			
			if (cabecalho[i].compareTo("--broker-list") == 0) brokerList = cabecalho[i+1];
			else if (cabecalho[i].compareTo("--throughput") == 0) throughput = Integer.valueOf(cabecalho[i+1]).intValue();
			else if (cabecalho[i].compareTo("--typeMessage") == 0) typeMessage = cabecalho[i+1];
			else if (cabecalho[i].compareTo("--aliens") == 0) aliens = true;
			else if (cabecalho[i].compareTo("--statusSIMCARD") == 0) statusSIMCARD = true;
			else if (cabecalho[i].compareTo("--telecommunications-provider-name") == 0)
			{
				telcoproviderName = cabecalho[i+1];
				mandatorytelcoprovidername = true;
			}
			else if (cabecalho[i].compareTo("--topic") == 0) 
			{
				topic = cabecalho[i+1];
				mandatorytopic = true;
			}
			else if (cabecalho[i].compareTo("--hlr-server") == 0)
			{
				 hlrServer = cabecalho[i+1];
				 mandatoryhlrServer = true;
			}
			else if (cabecalho[i].compareTo("--hlr-database") == 0)
			{
				hlrDatabase = cabecalho[i+1];
				mandatoryhlrDatabase = true;
			}
			else if (cabecalho[i].compareTo("--hlr-username") == 0)
			{
				hlrUsername = cabecalho[i+1];
				mandatoryhlrUsername = true;
			}
			else if (cabecalho[i].compareTo("--hlr-password") == 0)
			{
				hlrPassword = cabecalho[i+1];
				mandatoryhlrPassword = true;
			}
			else if (cabecalho[i].compareTo("--hlr-table") == 0)
			{
				hlrTable = cabecalho[i+1];	
				mandatoryhlrTable = true;
			}
			else 
			{
				System.out.println("Bad argument name: " + cabecalho[i]);
				return(false);
			}
		}		
		if ( mandatorytopic && mandatorytelcoprovidername && mandatoryhlrServer &&
			 mandatoryhlrDatabase && mandatoryhlrUsername && mandatoryhlrPassword && mandatoryhlrTable	)	return(result);
		else if (mandatorytelcoprovidername == false) System.out.println ("Telecommunications provider name argument is mandatory!");
		else if (mandatorytopic == false) System.out.println ("Topic argument is mandatory!");
		else if (mandatoryhlrServer == false) System.out.println ("HLR Server argument is mandatory!");
		else if (mandatoryhlrDatabase == false) System.out.println ("HLR Database argument is mandatory!");
		else if (mandatoryhlrUsername == false) System.out.println ("HLR Username argument is mandatory!");
		else if (mandatoryhlrPassword == false) System.out.println ("HLR Password argument is mandatory!");
		else if (mandatoryhlrTable == false) System.out.println ("HLR Table argument is mandatory!");
		
		else System.out.println ("Token list argument is mandatory!");
			
		return (false);
	}
	
	private static void SendMessage( Message msg ,  KafkaProducer<String, String> prd , String topicTarget)
	{		
		System.out.println("This is the message to send = " + msg.getAsText());
		String seqkey = new String("");
		seqkey = msg.getSeqKey();		
		System.out.println("Sending new message to Kafka... with key=" + seqkey);	
		ProducerRecord<String, String> record = new ProducerRecord<>(topicTarget, seqkey, msg.getAsText());		
		prd.send(record);
		System.out.println("Sent...");
	}
	
	
	private static void SendSimpleMessage( String msg ,  KafkaProducer<String, String> prd , String topicTarget , Timestamp mili)
	{
		System.out.println("This is the message to send = " + msg);		
		String seqkey = mili.toString();	
		System.out.println("Sending new message to Kafka... with key=" + seqkey);
		ProducerRecord<String, String> record = new ProducerRecord<>(topicTarget, seqkey, msg);		
		prd.send(record);	
		System.out.println("Sent...");
	}
	
	
	public static void main(String[] args) {


		String usage = "\nThe usage of the Activity Radio Network Simulator for IoTaaS is the following.\n\n" 
				+ "IoTaaSSimulator "
				+ "--telecommunications-provider-name <name> "
				+ "--broker-list <brokers> "
				+ "--topic <topic> "				
				+ "--hlr-server <server> "
				+ "--hlr-database <database> "
				+ "--hlr-username <username> "
				+ "--hlr-password <password> "
				+ "--hlr-table <table> "
				+ "--throughput <value> "
				+ "--typeMessage <value> "
				+ "--statusSIMCARD <on|off>"
				+ "--aliens <on|off>\n"
				+ "\n"
				+ "where, \n"
				+ "--telecommunications-provider-name: is the name to assign to the telecommunication provider and is mandatory\n"
				+ "--broker-list: is a broker list with ports (e.g.: kafka02.example.com:9092,kafka03.example.com:9092), default value is localhost:9092\n"
				+ "--topic: is the Customer kafka topic to be provisioned and is mandatory\n"
				+ "--hlr-server: is the mysql DB server and port for HLR (e.g.: yourAWSDBIP:3306) and is mandatory\n"
				+ "--hlr-database: is the DB name for HLR and is mandatory\n"
				+ "--hlr-username: is the DB username for HLR and is mandatory\n"
				+ "--hlr-password: is the DB pasword for HLR and is mandatory\n"
				+ "--hlr-table: is the DB table name containing the tuples <SIM Card , MSISDN> and is mandatory\n"
				+ "--throughput: is the approximate maximum messages to be produced by minute, default value is 10\n"
				+ "--typeMessage: is the type of message to be produced: JSON or XML, default value is JSON\n"
				+ "--statusSIMCARD: is an option to add the status of each SIM card in the radio network, sent with a 5 minutes periodicity to topic \"StatusSIMCARD\", default value is off\n"
				+ "--aliens: is an option to add random alien activities in the radio network, default value is off\n";
				
		Properties kafkaPropsCDR = new Properties();
		if (args.length == 0) System.out.println(usage);
		else 
		{
			if (VerifyArgs(args))
			{		
				System.out.println ("The following arguments are accepted:");
				CheckArguments();
				System.out.println ("------- Processing starting -------");
				
				kafkaPropsCDR.put("bootstrap.servers", brokerList); 
				kafkaPropsCDR.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer"); 
				kafkaPropsCDR.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer"); 
				KafkaProducer<String, String> producerCDR = new KafkaProducer<String, String>(kafkaPropsCDR);
				
				Timestamp mili;
				Timestamp miliupdated = new Timestamp ((long) 0);
				Timestamp miliupdatedStatus = new Timestamp ((long) 0);
				Timestamp delta = new Timestamp ((long)  1 * 60 * 1000 ); // 1 minute to refresh data from database				
				Timestamp deltaStatus = new Timestamp ((long)  5 * 60 * 1000 ); // 5 minutes to send new simcard status
				
				while (true)
				{
					try {
						mili = new Timestamp(System.currentTimeMillis());
						
						if (mili.compareTo( new Timestamp ( miliupdated.getTime() + delta.getTime() ) ) > 0 )
						{
							Subscribers = ReadFromDB(Subscribers);			
							miliupdated = mili;
						}
						
						if (statusSIMCARD == true && (mili.compareTo( new Timestamp ( miliupdatedStatus.getTime() + deltaStatus.getTime() ) ) > 0 ))
						{
							SendSimpleMessage( CreateStatusMessage(typeMessage , Subscribers) , producerCDR , "StatusSIMCARD" , mili);
							miliupdatedStatus = mili;
						}						
						
						if (!Subscribers.isEmpty() )
						{
							Message messageToSend =  CreateMessage(typeMessage , RandomSubscriber(Subscribers) , mili );						
							if (messageToSend != null)	SendMessage( messageToSend , producerCDR , topic );
						}
						else System.out.println("Empty list of subscribers. Therefore, no message to send.");
						
						if (aliens == true && (new Random()).nextBoolean() ) 
						{
							Message alienmsg = CreateMessage(typeMessage , RandomSubscriberAlien() , mili);
							alienmsg.setSeqKey(RandomSubscriberAlien());
							SendMessage (  alienmsg ,  producerCDR , topic ) ;
							System.out.println("Alien message sent...");							
						}
					
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						System.out.println("Waiting..." + timestamp );
						Thread.sleep(60000/throughput);
					}
					catch (Exception e) { e.printStackTrace();}
					
					System.out.println("Fire-and-forget stopped.");
				}
			}
			else System.out.println("Application Arguments bad usage.\n\nPlease check syntax.\n\n" + usage);
		}
		
		
	}





}
