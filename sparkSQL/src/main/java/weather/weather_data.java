package weather;

import java.util.ArrayList;
import java.util.List;

public class weather_data {
	


	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getGenerationtime_ms() {
		return generationtime_ms;
	}

	public void setGenerationtime_ms(String generationtime_ms) {
		this.generationtime_ms = generationtime_ms;
	}

	public String getUtc_offset_seconds() {
		return utc_offset_seconds;
	}

	public void setUtc_offset_seconds(String utc_offset_seconds) {
		this.utc_offset_seconds = utc_offset_seconds;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getTimezone_abbreviation() {
		return timezone_abbreviation;
	}

	public void setTimezone_abbreviation(String timezone_abbreviation) {
		this.timezone_abbreviation = timezone_abbreviation;
	}

	public String getElevation() {
		return elevation;
	}

	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

	public String getCurrent_weather() {
		return current_weather;
	}

	public void setCurrent_weather(String current_weather) {
		this.current_weather = current_weather;
	}

	private String latitude;
	
	private String longitude;
	
	private String generationtime_ms;

	private String utc_offset_seconds;

	private String timezone;

	private String timezone_abbreviation;

	private String elevation;

	private String current_weather;

	public String getlatitude() {
		return latitude;
	}

	@Override
	public String toString() {
		return "Tweet [latitude=" + latitude + ", longitude=" + longitude + ", generationtime_ms="
				+ generationtime_ms + ", utc_offset_seconds=" + utc_offset_seconds
				+ ", timezone=" + timezone + ", timezone_abbreviation=" + timezone_abbreviation
				+ ", elevation=" + elevation + ", current_weather=" + current_weather + "]";
	}

	
}
