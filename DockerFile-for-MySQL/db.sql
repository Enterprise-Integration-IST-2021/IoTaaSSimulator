CREATE DATABASE HLR;
CREATE USER 'hlruser' IDENTIFIED BY 'hlrpass';
GRANT ALL PRIVILEGES ON HLR.* TO 'hlruser';
FLUSH PRIVILEGES;
CREATE TABLE HLR.subscriber (SIMCARD VARCHAR(22), MSISDN VARCHAR(15));
INSERT INTO HLR.subscriber (SIMCARD , MSISDN) VALUES (173864374,911234567);
INSERT INTO HLR.subscriber (SIMCARD , MSISDN) VALUES (273864374,911234568);
INSERT INTO HLR.subscriber (SIMCARD , MSISDN) VALUES (373864374,911234569);
INSERT INTO HLR.subscriber (SIMCARD , MSISDN) VALUES (373864574,911234570); 
