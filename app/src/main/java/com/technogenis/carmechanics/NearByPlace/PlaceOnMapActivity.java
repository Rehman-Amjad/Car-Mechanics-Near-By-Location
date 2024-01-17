package com.technogenis.carmechanics.NearByPlace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.technogenis.carmechanics.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG=PlaceOnMapActivity.class.getSimpleName();

    // variable
    private GoogleMap googleMap;

    SharedPref yourPreference;
    String placeName;

    //Destination Latitude and Longitude
    String placeLatitude;
    String placeLongitude;

    //User Latitude and Longitude
    String userLatitude;
    String userLongitude;
    Button btn_direction;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_on_map);


        btn_direction = findViewById(R.id.btn_direction);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        placeLatitude = getIntent().getStringExtra("latitude");
        placeLongitude = getIntent().getStringExtra("longitude");

        placeName = getIntent().getStringExtra("placeName");

        yourPreference= SharedPref.getInstance(PlaceOnMapActivity.this);

        userLatitude=yourPreference.getData(CONSTANTS.KEY_LATITUDE);
        userLongitude=yourPreference.getData(CONSTANTS.KEY_LONGITUDE);

        btn_direction.setOnClickListener(v -> {
            setGooglePath(placeLatitude,placeLongitude,userLatitude,userLongitude);
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (placeLatitude!=null && placeLongitude!=null)
        {
            showDistance();
        }
        else
        {
            Toast.makeText(this, "location not found", Toast.LENGTH_SHORT).show();
        }

    }


    private void showDistance() {
        LatLng destinationPosition = new LatLng(Double.parseDouble(placeLatitude), Double.parseDouble(placeLongitude));
        LatLng currentPosition = new LatLng(Double.parseDouble(userLatitude), Double.parseDouble(userLongitude)); // user location

        // for destination
        googleMap.addMarker(new MarkerOptions().position(destinationPosition)
                        .title(placeName)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .snippet("Destination")
                        .alpha(1f))
                .showInfoWindow();

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(destinationPosition));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationPosition, 13.0f));
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // for current
        googleMap.addMarker(new MarkerOptions().position(currentPosition)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .alpha(1f))
                .showInfoWindow();

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13.0f));
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        String url = getDirectionsUrl(currentPosition, destinationPosition);


        Log.d(TAG, "showDistance: "+url);


        FetchUrl fetchUrl = new FetchUrl();

        fetchUrl.execute(url);
        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=" + this.getResources().getString(R.string.google_maps_key);

        // Output format
        String output = "json";

        // Building the url to the web service
        Log.d("finalUrl", "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters);

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    @SuppressLint("StaticFieldLeak")
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DirectionsJSONParser parser = new DirectionsJSONParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                googleMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    private void setGooglePath(String placeLatitude, String placeLongitude,
                               String userLatitude, String userLongitude)
    {
        //if the device dost not have a map installed, thne directed
        String sSource = userLatitude + "," + userLongitude;
        String sDestination = placeLatitude + "," + placeLongitude;
        try {
            //when google map is installed
            //initilzied uri
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource +"/"
                    + sDestination);

            //initilize intent action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            //set package
            intent.setPackage("com.google.android.apps.maps");

            //set Flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
        catch (ActivityNotFoundException e)
        {
            //when google map is not installed
            //initilize uri
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            // initlize intentt
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            //set Flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
    }
}