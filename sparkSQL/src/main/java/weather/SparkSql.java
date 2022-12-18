package weather;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.json.JSONArray;

public class SparkSql {
	private static final String TABLE_NAME = "weather";
	private static final String CF_DEFAULT = "info";
	
	static Configuration config;
	static JavaSparkContext jsc;
	
	public static void main(String[] args) {

		SparkConf sconf = new SparkConf().setAppName("SparkSQL").setMaster("local[3]");
		sconf.registerKryoClasses(new Class[] { org.apache.hadoop.hbase.io.ImmutableBytesWritable.class });
		
		config = HBaseConfiguration.create();
		config.set(TableInputFormat.INPUT_TABLE, TABLE_NAME);

		jsc = new JavaSparkContext(sconf);
		SQLContext sqlContext = new SQLContext(jsc.sc());
		
		JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = readTableByJavaPairRDD();
		System.out.println("Number of rows in hbase table: " + hBaseRDD.count());
		
		JavaRDD<weather_data> rows = hBaseRDD.map(x -> {
			weather_data wData = new weather_data();

			wData.setLatitude(Bytes.toString(x._1.get()));
			wData.setLongitude(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("longitude"))));
			wData.setGenerationtime_ms(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("generationtime_ms"))));
			wData.setUtc_offset_seconds(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("utc_offset_seconds"))));
			wData.setTimezone(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("timezone"))));
			wData.setTimezone_abbreviation(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("timezone_abbreviation"))));
			wData.setElevation(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("elevation"))));
			wData.setCurrent_weather(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("current_weather"))));

			return wData;
		});

		DataFrame tabledata = sqlContext
				.createDataFrame(rows, weather_data.class);
		tabledata.registerTempTable(TABLE_NAME);
		tabledata.printSchema();


		DataFrame query = sqlContext
				.sql("select * from weather");
		query.show();

		DataFrame query2 = sqlContext
				.sql("SELECT latitude, longitude FROM weather WHERE timezone_abbreviation = 'EST'");
		query2.show();


		DataFrame query3 = sqlContext
				.sql("SELECT generationtime_ms, utc_offset_seconds FROM weather WHERE elevation > 1000");
		query3.show();


		DataFrame query4 = sqlContext
				.sql("SELECT timezone, timezone_abbreviation FROM weather WHERE current_weather = 'sunny'");
		query4.show();

		
		 
		jsc.stop();

	}
	
    public static JavaPairRDD<ImmutableBytesWritable, Result> readTableByJavaPairRDD() {
		
    	JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = jsc
				.newAPIHadoopRDD(
						config,
						TableInputFormat.class,
						org.apache.hadoop.hbase.io.ImmutableBytesWritable.class,
						org.apache.hadoop.hbase.client.Result.class);
		return hBaseRDD;
    }
}
