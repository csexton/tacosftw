package com.districttaco.android;

import java.io.IOException;
import java.util.Date;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.*;

public class Status {
	static float lat = 0.0f;
	static float lng = 0.0f;
	static Date lastUpdate = null; 
	
	static public boolean UpdateStatus()
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://carte.districttaco.com/status.json");
		try {
			HttpResponse resp = client.execute(request);
			String body = EntityUtils.toString(resp.getEntity());
			try {
				JSONObject json = new JSONObject(body);
				JSONObject status = json.getJSONArray("statuses").getJSONObject(0);
				JSONObject location = status.getJSONObject("location");
				lat = Float.parseFloat(location.get("latitude").toString());
				lng = Float.parseFloat(location.get("longitude").toString());
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
}
