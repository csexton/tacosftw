package com.districttaco.android;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Home extends Activity {
	private final String statusUrl = "http://carte.districttaco.com/status.json";
	private ArrayList<Status> statuses = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
			new UpdateStatusTask().execute(new URL(statusUrl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    }
    
    @Override
	public boolean onMenuOpened(int featureId, Menu menu) {
    	MenuItem mapItem = menu.getItem(1);
    	mapItem.setEnabled(statuses != null);
		return super.onMenuOpened(featureId, menu);
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.refresh:
        	try {
				new UpdateStatusTask().execute(new URL(statusUrl));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
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
    
    private class UpdateStatusTask extends AsyncTask<URL, Integer, ArrayList<com.districttaco.android.Status>> {

    	@Override
    	protected ArrayList<com.districttaco.android.Status> doInBackground(URL... params) {
    		ArrayList<com.districttaco.android.Status> statuses = new ArrayList<com.districttaco.android.Status>();
            // make the http call to retrieve the status
    		HttpClient client = new DefaultHttpClient();
    		HttpGet request;
    		try {
    			request = new HttpGet(params[0].toURI());
    		} catch (URISyntaxException e1) {
    			e1.printStackTrace();
    			return null;
    		}
    		try {
    			HttpResponse resp = client.execute(request);
    			if (resp.getStatusLine().getStatusCode() != 200)
    				return null;
    			String body = EntityUtils.toString(resp.getEntity());
    			try {
    				// extract the contents from the json feed
    				JSONObject json = new JSONObject(body);
    				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    				int len = json.getJSONArray("statuses").length();
    				for (int i = 0; i < len; i++)
    				{
    					// load new values
    					JSONObject currentStatus = json.getJSONArray("statuses").getJSONObject(i);
    					com.districttaco.android.Status status = new com.districttaco.android.Status();
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
    			} catch (JSONException e) {
    				e.printStackTrace();
    				return null;
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    			return null;
    		}
     		return statuses;
    	}
    	
    	protected void onPostExecute(ArrayList<com.districttaco.android.Status> result) {
    		if (result != null && result.size() > 0) {
    			// save this in our instance
    			statuses = result;
    			
    			// dynamically create the UI from the status objects
    			LinearLayout container = (LinearLayout) findViewById(R.id.statuses);
    			container.removeAllViews();
    			Context context = container.getContext();
    			for (int i = 0; i < result.size(); i++)
    			{
    				// here, we dynamically create the various text elements and add them to our container
    				com.districttaco.android.Status status = result.get(i);
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
    			TextView lastUpdate = new TextView(context);
    			SimpleDateFormat dateFormat = new SimpleDateFormat("'Last Update: 'yyyy/MM/dd HH:mm:ss");
    			Date date = new Date();
    			lastUpdate.setText(dateFormat.format(date));
    			lastUpdate.setTextAppearance(context, R.style.Small);
    			container.addView(lastUpdate);
    		}
    		else {
    			// update the status view, if it's still on the screen
    			TextView statusView = (TextView) findViewById(R.id.status);
    			if (statusView != null)
    				statusView.setText(R.string.status_fail);
    		}
    	}
    }
}