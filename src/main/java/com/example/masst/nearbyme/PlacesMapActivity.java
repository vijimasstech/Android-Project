package com.example.masst.nearbyme;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import java.util.List;

public class PlacesMapActivity extends AppCompatActivity {

    PlaceList nearPlaces;
    MapView mapView;
    List<Overlay> mapOverlays;
    AddItemizedOverlay itemizedOverlay;
    GeoPoint geoPoint;

    MapController mc;
    double latitude;
    double longitude;
    OverlayItem overlayItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_map);

        Intent i = getIntent();

        String user_latitude = i.getStringExtra("user_latitude");
        String user_longitude = i.getStringExtra("user_logitude");

        nearPlaces = (PlaceList) i.getSerializableExtra("near_places");

        mapView =(MapView) findViewById(R.id.mapView);
        mapView.setClickable(true);

        mapOverlays = mapView.getOverlays();

        // Geopoint to place on map
        geoPoint = new GeoPoint((int) (Double.parseDouble(user_latitude) * 1E6),
                (int) (Double.parseDouble(user_longitude) * 1E6));

        // Drawable marker icon
        Drawable drawable_user = this.getResources()
                .getDrawable(R.drawable.mark_red);

        itemizedOverlay = new AddItemizedOverlay(drawable_user, this);

        // Map overlay item
        overlayItem = new OverlayItem(geoPoint, "Your Location",
                "That is you!");

        itemizedOverlay.addOverlay(overlayItem);

        mapOverlays.add(itemizedOverlay);
        itemizedOverlay.populateNow();

        // Drawable marker icon
        Drawable drawable = this.getResources()
                .getDrawable(R.drawable.mark_blue);

        itemizedOverlay = new AddItemizedOverlay(drawable, this);

        mc = mapView.getController();

        int minLat = Integer.MAX_VALUE;
        int minLong = Integer.MAX_VALUE;
        int maxLat = Integer.MIN_VALUE;
        int maxLong = Integer.MIN_VALUE;

        if (nearPlaces.results != null) {
            // loop through all the places
            for (Place place : nearPlaces.results) {
                latitude = place.geometry.location.lat; // latitude
                longitude = place.geometry.location.lng; // longitude

                // Geopoint to place on map
                geoPoint = new GeoPoint((int) (latitude * 1E6),
                        (int) (longitude * 1E6));

                // Map overlay item
                overlayItem = new OverlayItem(geoPoint, place.name,
                        place.vicinity);

                itemizedOverlay.addOverlay(overlayItem);


                // calculating map boundary area
                minLat  = (int) Math.min( geoPoint.getLatitudeE6(), minLat );
                minLong = (int) Math.min( geoPoint.getLongitudeE6(), minLong);
                maxLat  = (int) Math.max( geoPoint.getLatitudeE6(), maxLat );
                maxLong = (int) Math.max( geoPoint.getLongitudeE6(), maxLong );
            }
            mapOverlays.add(itemizedOverlay);

            // showing all overlay items
            itemizedOverlay.populateNow();
        }

        // Adjusting the zoom level so that you can see all the markers on map
        mapView.getController().zoomToSpan(Math.abs( minLat - maxLat ), Math.abs( minLong - maxLong ));

        // Showing the center of the map
        mc.animateTo(new GeoPoint((maxLat + minLat)/2, (maxLong + minLong)/2 ));
        mapView.postInvalidate();

    }
    
    protected boolean isRouteDisplayed() {

        return false;
    }
}



