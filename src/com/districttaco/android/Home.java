package com.districttaco.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Home extends Activity {
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

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
    	MenuItem mapMenuItem = menu.getItem(1);
    	mapMenuItem.setEnabled(Status.getLatitude() != 0.0 && Status.getLongitude() != 0.0);
    	
    	return true;
    }
    
    public void launchTwitter(View v)
    {
    	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/districttaco")));
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.refresh:
        	updateStatus();
        	return true;
        	
        case R.id.view_map:
        	startActivity(new Intent(this, Map.class));
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
    
    private void updateStatus() {
        // this makes the http call to retrieve the status
        if (Status.updateStatus()) {
        	// set the user elements with the info
        	TextView specialDetails = (TextView) findViewById(R.id.special_details);
        	specialDetails.setText(Status.getStatusText() != null ? Status.getStatusText() : "");
        	TextView locationName = (TextView) findViewById(R.id.location_name);
        	locationName.setText(Status.getLocationName() != null ? Status.getLocationName() : "");
        	TextView locationDescription = (TextView) findViewById(R.id.location_description);
        	locationDescription.setText(Status.getLocationDescription() != null ? Status.getLocationDescription() : "");
        	TextView infoTitle = (TextView) findViewById(R.id.info_title);
        	infoTitle.setText(Status.getInfoTitle() != null ? Status.getInfoTitle() : "");
        	TextView infoHeader = (TextView) findViewById(R.id.info_header);
        	infoHeader.setText(Status.getInfoHeader() != null ? Status.getInfoHeader() : "");
        	TextView infoBody = (TextView) findViewById(R.id.info_body);
        	infoBody.setText(Status.getInfoBody() != null ? Status.getInfoBody() : "");
        } else {
        	TextView specialDetails = (TextView) findViewById(R.id.special_details);
        	specialDetails.setText(R.string.status_fail);
        }
    }
}