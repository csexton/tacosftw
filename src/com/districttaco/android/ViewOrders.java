package com.districttaco.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class ViewOrders extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders);

        // set up click handlers
        View createOrderButton = findViewById(R.id.create_order_button);
        createOrderButton.setOnClickListener((OnClickListener) this);
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_orders_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.create_order:
        	startActivity(new Intent(this, EditOrder.class));
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.create_order_button:
        	startActivity(new Intent(this, EditOrder.class));
    		break;
    	}
    }
}