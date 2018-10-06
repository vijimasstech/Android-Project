package com.example.masst.nearbyme;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SinglePlaceActivity extends AppCompatActivity {
Button dir,cal;
Boolean isInternetPresent = false;
ConnectionDetector cd;
AlertDialogManager alert = new AlertDialogManager();
GooglePlaces googlePlaces;
PlaceDetails placeDetails;
ProgressDialog progressDialog;
GPSTracker gps;

public static String KEY_REFERENCE = "reference";
private String name,address,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_place);

        dir = (Button) findViewById(R.id.dirbut);
        cal =(Button) findViewById(R.id.callbut);
        Intent i = getIntent();
        String reference = i.getStringExtra(KEY_REFERENCE);
        new LoadSinglePlaceDetails().execute(reference);
    }
@SuppressLint("LongLogTag")
private void getGPSlocation(){
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent){
            alert.showAlertDialog(SinglePlaceActivity.this, "Internet Connection Error", "Please connect to working Internet connection", false);
            return;
        }
        gps = new GPSTracker(this);
        if (gps.canGetLocation()){
            Log.d("MenuActivity Your Location", "latitude:" + gps.getLatitude() + ", longitude" + gps.getLongitude());
        }else{
            alert.showAlertDialog(SinglePlaceActivity.this, "GPS Status", "couldn't get location information. Please enable GPS",false);
            return;
        }
}
    private class LoadSinglePlaceDetails extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SinglePlaceActivity.this);
            progressDialog.setMessage("Loading Profile....");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String reference = args[0];
            googlePlaces = new GooglePlaces();
            try {
                placeDetails = googlePlaces.getPlaceDetails(reference);

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (placeDetails != null){
                        String status = placeDetails.status;

                        if (status.equals("OK")){
                            if (placeDetails.result != null){
                                name = placeDetails.result.name;
                                address = placeDetails.result.formatted_address;
                                phone = placeDetails.result.formatted_phone_number;
                                final String latitude = Double.toString(placeDetails.result.geometry.location.lat);
                                final String longitude = Double.toString(placeDetails.result.geometry.location.lng);
                                dir.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getGPSlocation();
                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("http://maps.google.com/maps?saddr="+Double.toString(gps.getLatitude())+","+Double.toString(gps.getLongitude())+"&daddr="+latitude+","+longitude));
                                                startActivity(intent);
                                    }
                                });
                             cal.setOnClickListener(new View.OnClickListener() {
                                 @SuppressLint("MissingPermission")
                                 @Override
                                 public void onClick(View v) {
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + phone));
                                    startActivity(callIntent);
                                 }
                             });
                             Log.d("Place", name + address + phone + latitude + longitude);

                                TextView lbl_name = (TextView) findViewById(R.id.name);
                                TextView lbl_address = (TextView) findViewById(R.id.address);
                                TextView lbl_phone = (TextView) findViewById(R.id.phone);
                                TextView lbl_location = (TextView) findViewById(R.id.location);

                                name = name == null ? "Not present" : name;
                                address = address == null ? "Not present" : address;
                                phone = phone == null ? "Not present" : phone;

                                lbl_name.setText(name);
                                lbl_address.setText(address);
                                lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
                            }
                        }
                        else if(status.equals("ZERO_RESULTS")){
                            alert.showAlertDialog(SinglePlaceActivity.this, "Near Places",
                                    "Sorry no place found.",
                                    false);
                        }
                        else if(status.equals("UNKNOWN_ERROR"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry unknown error occured.",
                                    false);
                        }
                        else if(status.equals("OVER_QUERY_LIMIT"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry query limit to google places is reached",
                                    false);
                        }
                        else if(status.equals("REQUEST_DENIED"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured. Request is denied",
                                    false);
                        }
                        else if(status.equals("INVALID_REQUEST"))
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured. Invalid Request",
                                    false);
                        }
                        else
                        {
                            alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured.",
                                    false);
                        }
                    }else{
                        alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                    }

            });
        }
    }
}
