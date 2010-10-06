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
import android.app.ProgressDialog;
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
	private Date lastStatusFetch = null;
	private ProgressDialog progressDialog = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if (savedInstanceState == null)
        	updateStatus();
        else
        {
        	// get the saved state
        	statuses = savedInstanceState.getParcelableArrayList("statuses");
        	lastStatusFetch = new Date(savedInstanceState.getLong("lastStatusFetch"));
        	updateUiFromCurrentStatus();
        }
    }
    
    @Override
    protected void onResume() {    	
    	// calculate the time since the last fetch, if it's more than 6 hours we force a pull (operating carts is a temporal business, after all)
    	if (lastStatusFetch != null) {
	    	Date now = new Date();
	    	long elapsedMilliSec = now.getTime() - lastStatusFetch.getTime();
	    	if (elapsedMilliSec > 21600000)
	    		updateStatus();
    	}
    	
    	super.onResume();
    }
    
	private void updateStatus() {
        try {
        	progressDialog = ProgressDialog.show(this, null, getResources().getText(R.string.status_loading));
			new UpdateStatusTask().execute(new URL(statusUrl));
		} catch (MalformedURLException e) {
			progressDialog.dismiss();
			progressDialog = null;
			e.printStackTrace();
		}
    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
    	if (statuses != null)
    	{
    		bundle.putParcelableArrayList("statuses", statuses);
    		bundle.putLong("lastStatusFetch", lastStatusFetch.getTime());
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
    
    private void updateUiFromCurrentStatus() {
		
		// dynamically create the UI from the status objects
		LinearLayout container = (LinearLayout) findViewById(R.id.statuses);
		container.removeAllViews();
		for (int i = 0; i < statuses.size(); i++)
		{
			// here, we dynamically create the various text elements and add them to our container
			com.districttaco.android.Status status = statuses.get(i);
			TextView locationName = new TextView(this);
			locationName.setText(status.getLocationName());
			locationName.setTextAppearance(this, R.style.StatusHeader);
			container.addView(locationName);
			TextView locationDescription = new TextView(this);
			locationDescription.setText(status.getLocationDescription());
			locationDescription.setTextAppearance(this, R.style.StatusContent);
			container.addView(locationDescription);
			TextView special = new TextView(this);
			special.setText(R.string.special);
			special.setTextAppearance(this, R.style.StatusHeader);
			container.addView(special);
			TextView statusDetail = new TextView(this);
			statusDetail.setText(status.getStatusText());
			statusDetail.setTextAppearance(this, R.style.StatusContent);
			container.addView(statusDetail);
			TextView infoHeader = new TextView(this);
			infoHeader.setText(status.getInfoHeader());
			infoHeader.setTextAppearance(this, R.style.InfoHeader);
			container.addView(infoHeader);
			TextView infoTitle = new TextView(this);
			infoTitle.setText(status.getInfoTitle());
			infoTitle.setTextAppearance(this, R.style.StatusHeader);
			container.addView(infoTitle);
			TextView infoBody = new TextView(this);
			infoBody.setText(status.getInfoBody());
			infoBody.setTextAppearance(this, R.style.StatusContent);
			container.addView(infoBody);
		}
		
		// show the time of the last successful fetch
		if (lastStatusFetch != null)
		{
			TextView lastUpdate = new TextView(this);
			SimpleDateFormat dateFormat = new SimpleDateFormat("'Last Updated:' E MMM d h:mm a");
			lastUpdate.setText(dateFormat.format(lastStatusFetch));
			lastUpdate.setTextAppearance(this, R.style.Footer);
			container.addView(lastUpdate);
		}
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
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class UpdateStatusTask extends AsyncTask<URL, Integer, ArrayList<com.districttaco.android.Status>> {

    	@Override
    	protected ArrayList<com.districttaco.android.Status> doInBackground(URL... params) {
    		ArrayList<com.districttaco.android.Status> newStatuses = new ArrayList<com.districttaco.android.Status>();
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
    					newStatuses.add(status);
    				}
    			} catch (JSONException e) {
    				e.printStackTrace();
    				return null;
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    			return null;
    		}
     		return newStatuses;
    	}
    	
    	protected void onPostExecute(ArrayList<com.districttaco.android.Status> result) {
    		// if we are displaying a progress dialog, it's time to dismiss it
    		if (progressDialog != null)
    		{
    			progressDialog.dismiss();
    			progressDialog = null;
    		}
    		
    		// populate the UI with the results
    		if (result != null) {
    			// save this in our instance
    			statuses = result;
				lastStatusFetch = new Date();
    			updateUiFromCurrentStatus();
    		}
    	}
    }
}