package com.districttaco.android;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.*;

public class Map extends MapActivity {
	private MapView mapView = null;
	private List<Overlay> mapOverlays;
	private CartItemizedOverlay cartOverlay;
	private Drawable drawable;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        GeoPoint point = new GeoPoint((int)(Status.getLatitude() * 1E6), (int)(Status.getLongitude() * 1E6));
        OverlayItem overlayItem = new OverlayItem(point, "", "");
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.cartmarker);
        cartOverlay = new CartItemizedOverlay(drawable);
        cartOverlay.addOverlay(overlayItem);
        mapOverlays.add(cartOverlay);
        MapController controller = mapView.getController();
        controller.setZoom(5);
        controller.setCenter(point);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
