package com.districttaco.android;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.*;

public class Status {
	static private double lat = 0.0f;
	static private double lng = 0.0f;
	static private String locationName = null;
	static private String locationDescription = null;
	static private String statusText = null;
	static private Date lastUpdate = null;
	static private String infoTitle = null;
	static private String infoHeader = null;
	static private String infoBody = null;
	
	static public boolean updateStatus()
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://carte.districttaco.com/status.json");
		try {
			HttpResponse resp = client.execute(request);
			String body = EntityUtils.toString(resp.getEntity());
			try {
				// extract the contents from the json feed
				JSONObject json = new JSONObject(body);
				JSONObject status = json.getJSONArray("statuses").getJSONObject(0);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
				try {
					lastUpdate = formatter.parse(status.getString("updated_at"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject info = status.getJSONObject("info");
				infoTitle = info.getString("title");
				infoHeader = info.getString("header");
				infoBody = info.getString("body");
				JSONObject location = status.getJSONObject("location");
				locationName = location.getString("name");
				locationDescription = location.getString("description");
				lat = location.getDouble("latitude");
				lng = location.getDouble("longitude");
				statusText = status.getString("body");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	static String getLocationName() {
		return locationName;
	}
	
	static String getLocationDescription() {
		return locationDescription;
	}
	
	static double getLatitude() {
		return lat;
	}
	
	static double getLongitude() {
		return lng;
	}
	
	static String getStatusText() {
		return statusText;
	}
	
	static Date getLastUpdate() {
		return lastUpdate;
	}
	
	static String getInfoTitle() {
		return infoTitle;
	}
	static String getInfoHeader() {
		return infoHeader;
	}
	static String getInfoBody() {
		return infoBody;
	}
}
