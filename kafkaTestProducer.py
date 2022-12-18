# Import necessary packages
import time

import pandas as pd
import requests
from kafka import KafkaProducer

# Set the URL to fetch the data from
states = pd.read_csv("states.csv")
urls = {}
for i, row in enumerate(states.values):
    urls[row[0]] = f'https://api.open-meteo.com/v1/forecast?latitude={row[2]}&longitude={row[3]}&current_weather=true'

# Set the interval to send the data (in seconds)
interval1 = 2
interval2 = 5

while True:
    # Fetch the data from the URL
    for state, url in urls.items():
        response = requests.get(url)
        data = response.text
        tempData = eval(data)
        tempData["state"] = state

        # Create a Kafka producer
        producer = KafkaProducer(bootstrap_servers=['localhost:9092'])

        # Send the data to Kafka
        producer.send('weather-data', str(tempData).encode())

        # Flush and close the producer
        producer.flush()
        producer.close()
        time.sleep(interval1)
        # Sleep for the specified interval before sending the data again
    time.sleep(interval2)
