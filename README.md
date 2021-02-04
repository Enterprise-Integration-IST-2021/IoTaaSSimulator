# IoTaaSSimulator
IE 2020/2021 tool to simulate the Activity Radio Network for IoTaaS, using Kafka Producer and MySQL

Verify if JAVA 8 is available using the command: 

```
java -version
```

Then, to execute the generator of messages use the following command from the target directory:
```
java -jar .\IoTaaSSimulator.jar
```
```
The usage of the Activity Radio Network Simulator for IoTaaS is the following.

IoTaaSSimulator --telecommunications-provider-name <name> --broker-list <brokers> --topic <topic> --hlr-server <server> --hlr-database <database> --hlr-username <username> --hlr-password <password> --hlr-table <table> --throughput <value> --typeMessage <value> --statusSIMCARD <on|off> --aliens <on|off>

where,
--telecommunications-provider-name: is the name to assign to the telecommunication provider and is mandatory
--broker-list: is a broker list with ports (e.g.: kafka02.example.com:9092,kafka03.example.com:9092), default value is localhost:9092
--topic: is the Customer kafka topic to be provisioned and is mandatory
--hlr-server: is the mysql DB server and port for HLR (e.g.: yourAWSDBIP:3306) and is mandatory
--hlr-database: is the DB name for HLR and is mandatory
--hlr-username: is the DB username for HLR and is mandatory
--hlr-password: is the DB pasword for HLR and is mandatory
--hlr-table: is the DB table name containing the tuples <SIM Card , MSISDN> and is mandatory
--throughput: is the approximate maximum messages to be produced by minute, default value is 10
--typeMessage: is the type of message to be produced: JSON or XML, default value is JSON
--statusSIMCARD: is an option to add the status of each SIM card in the radio network, sent with a 5 minutes periodicity to topic "StatusSIMCARD", default value is off
--aliens: is an option to add random alien activities in the radio network, default value is off
```

One example of a Call Data Record message sent, in JSON, to the topic in Kafka is:
```
{"CallDataRecord":{"timeStamp":"2021-02-04 14:49:07.401","SimCard":"173864374","service":"voice","timeduration":"10","byteusage":"null","location":"Saldanha","equipment":"Mobile Phone","destinationCom":"914555190"}}
```
One example of a Call Data Record message sent, in XML, to the topic in Kafka is:
```
<CallDataRecord><timeStamp>2021-02-04 15:17:51.504</timeStamp><SimCard>473864374</SimCard><service>data</service><timeduration>null</timeduration><byteusage>108330</byteusage><location>Unknown</location><equipment>Raspberry Pi</equipment><destinationCom>974851792</destinationCom></CallDataRecord>
```
One example of the messages sent in JSON to the topic StatusSIMCARD in Kafka is:
```
{"StatusSIMCARD":{"SIMCARD":{"273864374":"1","373864374":"0","373864574":"1","173864374":"0"}}}
```
One example of the messages sent in XML to the topic StatusSIMCARD in Kafka is:
```
<StatusSIMCARD><SIMCARD><273864374>1</273864374><373864374>1</373864374><373864574>0</373864574><173864374>1</173864374></SIMCARD></StatusSIMCARD>
```
