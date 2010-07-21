package com.districttaco.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class EditOrder extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_order);

        // set up click handlers
        View createOrderButton = findViewById(R.id.add_item_button);
        createOrderButton.setOnClickListener((OnClickListener) this);
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.add_item:
        	startActivity(new Intent(this, EditItem.class));
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.add_item_button:
        	startActivity(new Intent(this, EditItem.class));
    		break;
    	}
    }
}