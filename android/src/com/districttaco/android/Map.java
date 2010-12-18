package com.districttaco.android;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
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
        
        // add our markers
        Bundle bundle = getIntent().getExtras();
        statuses = bundle.getParcelableArrayList("statuses");
    	GeoPoint point = new GeoPoint((int) (33.1 * 1E6), (int) (11.2 * 1E6));
    	OverlayItem overlayItem = new OverlayItem(point, "District Taco Restaurant", "5723 Lee Highway");
    	drawable = this.getResources().getDrawable(R.drawable.cartmarker);
    	cartOverlay = new CartItemizedOverlay(drawable, this);
    	cartOverlay.addOverlay(overlayItem);
    	GeoPoint lastPoint = point;
        if (statuses != null && statuses.size() > 0)
        {
        	mapOverlays = mapView.getOverlays();
        	for (int i = 0; i < statuses.size(); i++)
        	{
        		Status status = statuses.get(i);
		        if (status.getLatitude() != 0.0 && status.getLongitude() != 0.0) {
		        	point = new GeoPoint((int) (status.getLatitude() * 1E6), (int) (status.getLongitude() * 1E6));
		        	overlayItem = new OverlayItem(point, status.getLocationName(), status.getLocationDescription());
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
