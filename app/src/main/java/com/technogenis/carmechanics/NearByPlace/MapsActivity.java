package com.technogenis.carmechanics.NearByPlace;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.technogenis.carmechanics.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG=MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    float zoomLevel = 17.0f;

    ImageView back_image;

    String placeName;
    double latitude,current_lat;
    double longitude,current_longi;

    SharedPref yourPreference;
    List<LatLng> latLngs;

    TextView tvPlaceName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        tvPlaceName = findViewById(R.id.tvPlaceName);
        back_image = findViewById(R.id.back_image);

        yourPreference= SharedPref.getInstance(MapsActivity.this);
        latLngs=new ArrayList<>();

        String la = getIntent().getStringExtra("latitude");
        String lo = getIntent().getStringExtra("longitude");

        placeName = getIntent().getStringExtra("placeName");

        String currLat=yourPreference.getData(CONSTANTS.KEY_LATITUDE);
        String current_long=yourPreference.getData(CONSTANTS.KEY_LONGITUDE);

        //todo::Place Latitude & longitude
        latitude = Double.parseDouble(la);
        longitude = Double.parseDouble(lo);

        //todo::User current latitude & longitude
        current_lat=Double.parseDouble(currLat);
        current_longi=Double.parseDouble(current_long);

        Log.d(TAG, "onCreate: "+latitude);
        Log.d(TAG, "onCreate: "+longitude);

        tvPlaceName.setText(placeName);

        latLngs.add(new LatLng(latitude,longitude));
        latLngs.add(new LatLng(current_lat,current_longi));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.addMarker(new MarkerOptions().position(latLng).title(placeName)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }
}