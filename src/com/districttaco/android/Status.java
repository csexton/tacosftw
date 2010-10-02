package com.districttaco.android;

import java.io.IOException;
import java.util.Date;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.*;
import android.location.Location;

public class Status {
	static double lat = 0.0f;
	static double lng = 0.0f;
	static String status_text = null;
	static Date lastUpdate = null;
	
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
				JSONObject info = status.getJSONObject("info");
				JSONObject location = status.getJSONObject("location");
				lat = location.getDouble("latitude");
				lng = location.getDouble("longitude");
				status_text = status.getString("body");
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
	
	static double getLatitude() {
		return lat;
	}
}
