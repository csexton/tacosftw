package com.districttaco.android;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Home extends Activity {
	private ArrayList<Status> statuses = new ArrayList<Status>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    protected void onResume() {
    	super.onResume();
    	updateStatus();
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void launchTwitter(View v)
    {
    	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://mobile.twitter.com/districttaco")));
    }
    
    public ArrayList<Status> getStatuses() {
    	return statuses;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.refresh:
        	updateStatus();
        	return true;
        	
        case R.id.view_map:
        	Intent intent = new Intent(this, Map.class);
        	Bundle bundle = new Bundle();
        	bundle.putParcelableArrayList("statuses", statuses);
        	intent.putExtras(bundle);
        	startActivity(intent);
        	return true;
        	
//        case R.id.settings:
//        	startActivity(new Intent(this, Settings.class));
//        	return true;
        	
//        case R.id.view_orders:
//        	startActivity(new Intent(this, ViewOrders.class));
//        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private boolean updateStatus() {
        // make the http call to retrieve the status
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://carte.districttaco.com/status.json");
		try {
			HttpResponse resp = client.execute(request);
			if (resp.getStatusLine().getStatusCode() != 200)
				return false;
			String body = EntityUtils.toString(resp.getEntity());
			try {
				// we've successfully fetched the feed, wipe out all current status info
				statuses.clear();
				
				// extract the contents from the json feed
				JSONObject json = new JSONObject(body);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				int len = json.getJSONArray("statuses").length();
				for (int i = 0; i < len; i++)
				{
					// load new values
					JSONObject currentStatus = json.getJSONArray("statuses").getJSONObject(i);
					Status status = new Status();
					try {
						status.setLastUpdate(formatter.parse(currentStatus.getString("updated_at")));
					} catch (ParseException e) {
						e.printStackTrace();
					}					
					JSONObject info = currentStatus.getJSONObject("info");
					status.setInfoTitle(info.getString("title"));
					status.setInfoHeader(info.getString("header"));
					status.setInfoBody(info.getString("body"));
					JSONObject location = currentStatus.getJSONObject("location");
					status.setLocationName(location.getString("name"));
					status.setLocationDescription(location.getString("description"));
					status.setLatitude(location.getDouble("latitude"));
					status.setLongitude(location.getDouble("longitude"));
					status.setStatusText(currentStatus.getString("body"));
					
					// status object is fully populated, add it to the list
					statuses.add(status);
				}
				
				// dynamically create the UI from the status objects
				LinearLayout container = (LinearLayout) findViewById(R.id.statuses);
				container.removeAllViews();
				Context context = container.getContext();
				for (int i = 0; i < statuses.size(); i++)
				{
					// here, we dynamically create the various text elements and add them to our container
					Status status = statuses.get(i);
					TextView locationName = new TextView(context);
					locationName.setText(status.getLocationName());
					locationName.setTextAppearance(context, R.style.GreenHeader);
					container.addView(locationName);
					TextView locationDescription = new TextView(context);
					locationDescription.setText(status.getLocationDescription());
					locationDescription.setTextAppearance(context, R.style.Default);
					container.addView(locationDescription);
					TextView special = new TextView(context);
					special.setText(R.string.special);
					special.setTextAppearance(context, R.style.GreenHeader);
					container.addView(special);
					TextView statusDetail = new TextView(context);
					statusDetail.setText(status.getStatusText());
					statusDetail.setTextAppearance(context, R.style.Default);
					container.addView(statusDetail);
					TextView infoHeader = new TextView(context);
					infoHeader.setText(status.getInfoHeader());
					infoHeader.setTextAppearance(context, R.style.DefaultBold);
					container.addView(infoHeader);
					TextView infoTitle = new TextView(context);
					infoTitle.setText(status.getInfoTitle());
					infoTitle.setTextAppearance(context, R.style.GreenHeader);
					container.addView(infoTitle);
					TextView infoBody = new TextView(context);
					infoBody.setText(status.getInfoBody());
					infoBody.setTextAppearance(context, R.style.Default);
					container.addView(infoBody);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        
        return true;
    }
}