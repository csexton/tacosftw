package com.districttaco.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Address;
import android.os.Bundle;

import com.google.android.maps.*;

public class Map extends MapActivity {
	private MapView mapView = null;
	private List<Overlay> mapOverlays;
	private CartItemizedOverlay cartOverlay;
	private Drawable drawable;
	private ArrayList<Status> statuses;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        // add the restaurant location
    	drawable = this.getResources().getDrawable(R.drawable.cartmarker);
    	cartOverlay = new CartItemizedOverlay(drawable, this);
    	GeoPoint lastPoint = null;
		try {
	        Geocoder geocoder = new Geocoder(this, Locale.US);
			List<Address> addresses = geocoder.getFromLocationName("5723 Lee Highway, Arlington, VA 22207", 5);
	    	GeoPoint point = new GeoPoint((int) (addresses.get(0).getLatitude() * 1E6), (int) (addresses.get(0).getLongitude() * 1E6));
	    	OverlayItem overlayItem = new OverlayItem(point, "District Taco Restaurant", "5723 Lee Highway\nArlington, VA 22207");
	    	cartOverlay.addOverlay(overlayItem);
	    	lastPoint = point;
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // add our markers
        Bundle bundle = getIntent().getExtras();
        statuses = bundle.getParcelableArrayList("statuses");
        if (statuses != null && statuses.size() > 0)
        {
        	mapOverlays = mapView.getOverlays();
        	for (int i = 0; i < statuses.size(); i++)
        	{
        		Status status = statuses.get(i);
		        if (status.getLatitude() != 0.0 && status.getLongitude() != 0.0) {
		        	GeoPoint point = new GeoPoint((int) (status.getLatitude() * 1E6), (int) (status.getLongitude() * 1E6));
		        	OverlayItem overlayItem = new OverlayItem(point, status.getLocationName(), status.getLocationDescription());
		        	cartOverlay.addOverlay(overlayItem);
		        	lastPoint = point;
		        }
        	}
       		mapOverlays.add(cartOverlay);
       		MapController controller = mapView.getController();
       		controller.setCenter(lastPoint);
       		controller.setZoom(15);
        }
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
