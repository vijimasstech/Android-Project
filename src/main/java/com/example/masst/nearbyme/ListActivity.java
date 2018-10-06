package com.example.masst.nearbyme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity {
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();
    GooglePlaces googlePlaces;
    PlaceList nearPlaces;
    GPSTracker gps;
    Button showmap;
    ProgressDialog progressDialog;
    ListView lv;
    ArrayList<HashMap<String, String>> placeListItems =  new ArrayList<HashMap<String, String>>();

    public static String KEY_REFERENCE = "reference";
    public static String KEY_NAME = "name";
    public static String KEY_VICINITY = "vicinity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent){
            alert.showAlertDialog(ListActivity.this, "Internet" +
                    " Connection Error",
                    "Please connnect to working Internet connection",false);
            return;
        }
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {

            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude:" + gps.getLongitude());

        }else{
            alert.showAlertDialog(ListActivity.this, "GPS Status", "Couldn't get location information. Please enable GPS",false);
            return;
        }
        lv = (ListView) findViewById(R.id.list);
        showmap = (Button) findViewById(R.id.show_map);
        new LoadPlaces().execute();

        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PlacesMapActivity.class);
                i.putExtra("user_latitude", Double.toString(gps.getLatitude()));
                i.putExtra("user_longitude", Double.toString(gps.getLongitude()));
                i.putExtra("near_places", String.valueOf(nearPlaces));
                startActivity(i);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
                Intent in = new Intent(getApplicationContext(), SinglePlaceActivity.class);
                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
            }
        });
    }
public class LoadPlaces extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ListActivity.this);
            progressDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            googlePlaces = new GooglePlaces();
            try{
                Intent i = getIntent();
                String searchType = i.getStringExtra("SEARCH_TYPE");
                String types = searchType;
                double radius = 5000;

                nearPlaces = googlePlaces.search(gps.getLatitude(), gps.getLongitude(), radius, types);
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
                    String status = nearPlaces.status;
                    if (status.equals("OK")) {
                        if (nearPlaces.results != null) {
                            for (Place p : nearPlaces.results) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(KEY_REFERENCE, p.reference);
                                map.put(KEY_NAME, p.name);
                                placeListItems.add(map);
                            }
                            ListAdapter adapter = new SimpleAdapter(ListActivity.this, placeListItems,
                                    R.layout.list_item,
                                    new String[]{KEY_REFERENCE, KEY_NAME}, new int[]{
                                    R.id.reference, R.id.name});

                            lv.setAdapter(adapter);
                        }
                    }
                    else if(status.equals("ZERO_RESULTS")){
                        // Zero results found
                        alert.showAlertDialog(ListActivity.this, "Near Places",
                                "Sorry no places found. Try to change the types of places",
                                false);
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
                        alert.showAlertDialog(ListActivity.this, "Places Error",
                                "Sorry unknown error occured.",
                                false);
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
                        alert.showAlertDialog(ListActivity.this, "Places Error",
                                "Sorry query limit to google places is reached",
                                false);
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
                        alert.showAlertDialog(ListActivity.this, "Places Error",
                                "Sorry error occured. Request is denied",
                                false);
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
                        alert.showAlertDialog(ListActivity.this, "Places Error",
                                "Sorry error occured. Invalid Request",
                                false);
                    }
                    else
                    {
                        alert.showAlertDialog(ListActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                }

            });
        }
    }
}
