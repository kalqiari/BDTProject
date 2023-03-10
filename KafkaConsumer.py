import json

import happybase
from pyspark.sql import SparkSession
from pyspark.streaming import StreamingContext
from dotenv import dotenv_values

config = dotenv_values(".env")

packages = [
    f'org.apache.spark:spark-sql-kafka-0-10_{config["scala_version"]}:{config["spark_version"]}',
    'org.apache.kafka:kafka-clients:3.3.1'
]

spark = SparkSession \
    .builder \
    .master(config["spark_master"]) \
    .appName("Word Count") \
    .config("spark.jars.packages", ",".join(packages)) \
    .getOrCreate()

ssc = StreamingContext(spark.sparkContext, 5)

df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", config["kafka_url"]) \
    .option("subscribe", config["kafka_topic"]) \
    .load()

print("df type: " + str(type(df)))

connection = happybase.Connection(config['hbase_host'], port=9090)

families = {
    'info': dict()
}

try:
    connection.create_table(config.get("hbase_table_name"), families)
except:
    print("Table already exists")


def store_row(row):
    connection = happybase.Connection(config.get("hbase_host"), port=9090)
    table = connection.table(config.get("hbase_table_name"))

    value_str = row["value"].decode('utf-8')
    value_dict = json.loads(value_str)
    generation_time_ms = str(value_dict['generationtime_ms'])

    table.put(generation_time_ms, {
        b'info:latitude': str(value_dict['latitude']),
        b'info:longitude': str(value_dict['longitude']),
        b'info:generationtime_ms': generation_time_ms,
        b'info:utc_offset_seconds': str(value_dict['utc_offset_seconds']),
        b'info:timezone': str(value_dict['timezone']),
        b'info:timezone_abbreviation': str(value_dict['timezone_abbreviation']),
        b'info:elevation': str(value_dict['elevation']),
        b'info:temperature': str(value_dict['current_weather']['temperature']),
        b'info:windspeed': str(value_dict['current_weather']['windspeed']),
        b'info:winddirection': str(value_dict['current_weather']['winddirection']),
        b'info:weathercode': str(value_dict['current_weather']['weathercode']),
        b'info:time': str(value_dict['current_weather']['time']),
        b'info:state': str(value_dict['state'])
    })

    print(row)


query = df.writeStream.foreach(store_row).start().awaitTermination()
connection.close()
