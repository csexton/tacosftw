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
        if (statuses != null && statuses.size() > 0)
        {
        	mapOverlays = mapView.getOverlays();
        	drawable = this.getResources().getDrawable(R.drawable.cartmarker);
        	cartOverlay = new CartItemizedOverlay(drawable);
        	for (int i = 0; i < statuses.size(); i++)
        	{
        		Status status = statuses.get(i);
//		        if (status.getLatitude() != 0.0 && status.getLongitude()!= 0.0) {
		        	GeoPoint point = new GeoPoint((int) (status.getLatitude() * 1E6), (int) (status.getLongitude() * 1E6));
		        	OverlayItem overlayItem = new OverlayItem(point, "", "");
		        	cartOverlay.addOverlay(overlayItem);
//		        }
        	}
        	mapOverlays.add(cartOverlay);
        	MapController controller = mapView.getController();
        	controller.setZoom(12);
        }
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
