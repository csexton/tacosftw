package com.districttaco.android;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.*;

public class Status {
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
				String lat = location.get("latitude").toString();
				String lng = location.get("longitude").toString();
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
