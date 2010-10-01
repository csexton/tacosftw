package com.districttaco.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Home extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Status.UpdateStatus();
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.settings:
        	startActivity(new Intent(this, Settings.class));
        	return true;
        	
        case R.id.view_orders:
        	startActivity(new Intent(this, ViewOrders.class));
            return true;
        	
        case R.id.photos:
        	startActivity(new Intent(this, Photos.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}