package com.example.masst.nearbyme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

public class AddItemizedOverlay extends ItemizedOverlay<OverlayItem> {

    private ArrayList<OverlayItem> mapOverlays =  new ArrayList<>();
    private Context context;
    public AddItemizedOverlay(Drawable defaultMarker, Context context){
        this(defaultMarker);
        this.context = context;
    }
    public AddItemizedOverlay(Drawable defaultMarker) {

        super(boundCenterBottom(defaultMarker));
    }

    public boolean onTouchEvent(MotionEvent event, MapView mapView){
        if (event.getAction() == 1){
            GeoPoint geoPoint = mapView.getProjection().fromPixels(
                    (int) event.getX(),
                    (int) event.getY());
            double lat = geoPoint.getLatitudeE6() / 1E6;
            double lon = geoPoint.getLongitudeE6() / 1E6;
        }
        return false;
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mapOverlays.get(i);
    }

    @Override
    public int size() {
        return mapOverlays.size();
    }

    @Override
    protected boolean onTap(int index) {
        OverlayItem item = mapOverlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
        return true;
    }
    public void addOverlay(OverlayItem overlay){
        mapOverlays.add(overlay);
    }
    public void populateNow(){
        this.populate();
    }

}
