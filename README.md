# BDTProject

## Used Technologoies:
- Python
- Kafka
- Spark Streaming
- HBase
- Hive
- Spark SQL
- Tableau
- Docker
- Java


## Prequests:
- Docker 
- VM Cloudera Runing
- Python 3.7 
- Java JDK 1.8 or above.
- install python libraries (happybase, pyspark, python-dotenv, pandas, kafka)

## Installation Steps: 

- brew install --cask docker
- git clone https://github.com/kalqiari/BDTProject.git
- cd BDTProject
- docker compose up
- Get VM internal IP using ifconfig command and add it to .env file
- Run Hbase Using (sudo service hbase-master start, sudo service hbase-regionserver start)
- python KafkaProducer.py
- python KafkaConsumer.py
- Copy the Hive script and execute it inside the VM. 
- Then open the Visualization file using Tableau and Update the data source to point to the VM IP. 
