package com.technogenis.carmechanics.UserPanal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.Adapter.ImageAdapter;
import com.technogenis.carmechanics.Model.ImageModel;
import com.technogenis.carmechanics.Model.ServicesModel;
import com.technogenis.carmechanics.NearByPlace.CONSTANTS;
import com.technogenis.carmechanics.NearByPlace.PlaceOnMapActivity;
import com.technogenis.carmechanics.NearByPlace.SharedPref;
import com.technogenis.carmechanics.R;
import com.technogenis.carmechanics.UserAdapter.ServicesAdapter;

import java.util.ArrayList;

public class GarageDetailsActivity extends AppCompatActivity {

    TextView marqueeName_text;
    ImageView back_Image;
    RecyclerView imageSlider_recycleView,service_recycleView;
    ArrayList<ImageModel> mDataList;
    ImageAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    LinearLayout services_layout;
    ArrayList<ServicesModel> serviceList;
    ServicesAdapter servicesAdapter;

    String garageName,garageOwnerName,garageContactNumber,garageBio,
            ownerUserUID,garageAddKey,currentTime,currentDate,
            garageCoverLink,garageAddress,placeLatitude,placeLongitude
            ,userLatitude,userLongitude;
    ProgressDialog progressDialog;

    LinearLayout chatNow_layout,booking_layout,viewDistance_layout,getDirection_layout;
    TextView ownerName_text,phone_text,marqueeAddress_text,marqueeBio_text,name_text;
    ImageView whatsapp_image,phoneCall_image,complain_image;

    //initilize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    double currentLat = 0, currentLong = 0;
    GoogleMap map;

    SharedPref yourPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_details);

        progressDialog = ProgressDialog.show(GarageDetailsActivity.this, "", "Processing", true);

        initViews();
        getIntentValue();

        yourPreference= SharedPref.getInstance(GarageDetailsActivity.this);

        userLatitude=yourPreference.getData(CONSTANTS.KEY_LATITUDE);
        userLongitude=yourPreference.getData(CONSTANTS.KEY_LONGITUDE);

        back_Image.setOnClickListener(v -> {
           getOnBackPressedDispatcher().onBackPressed();
        });

        viewDistance_layout.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlaceOnMapActivity.class);
            intent.putExtra("latitude", placeLatitude);
            intent.putExtra("longitude", placeLongitude);
            intent.putExtra("placeName", garageName);
            startActivity(intent);
        });

        getDirection_layout.setOnClickListener(v -> {
            setGooglePath(placeLatitude,placeLongitude,userLatitude,userLongitude);
        });

        whatsapp_image.setOnClickListener(v -> {
            meesageToWhatsapp(garageContactNumber);
        });

        phoneCall_image.setOnClickListener(v -> {
            phoneCall(garageContactNumber);
        });

        chatNow_layout.setOnClickListener(v -> {
            Intent intent = new Intent(GarageDetailsActivity.this, ComplainActivity.class);
            intent.putExtra("garageAddKey",garageAddKey);
            intent.putExtra("ownerUserUID",ownerUserUID);
            intent.putExtra("garageName",garageName);
            intent.putExtra("garageContactNumber",garageContactNumber);
            startActivity(intent);
        });

        booking_layout.setOnClickListener(v -> {
            Intent intent = new Intent(GarageDetailsActivity.this, BookGarageActivity.class);
            intent.putExtra("garageAddress",garageAddress);
            intent.putExtra("garageName",garageName);
            intent.putExtra("garageContact",garageContactNumber);
            startActivity(intent);
        });
    }

    private void phoneCall(String marqueeContactNumber)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ marqueeContactNumber));//change the number
        startActivity(callIntent);
    }

    private void meesageToWhatsapp(String phoneNumber)
    {
        try {
            String text = "HI,";// Replace with your message.
//           String toNumber = "xxxxxxxxxx"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phoneNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initViews()
    {
        marqueeName_text = findViewById(R.id.marqueeName_text);
        imageSlider_recycleView = findViewById(R.id.imageSlider_recycleView);
        chatNow_layout = findViewById(R.id.chatNow_layout);
        booking_layout = findViewById(R.id.booking_layout);
        viewDistance_layout = findViewById(R.id.viewDistance_layout);
        getDirection_layout = findViewById(R.id.getDirection_layout);
        ownerName_text = findViewById(R.id.ownerName_text);
        phone_text = findViewById(R.id.phone_text);
        marqueeAddress_text = findViewById(R.id.marqueeAddress_text);
        marqueeBio_text = findViewById(R.id.marqueeBio_text);
        whatsapp_image = findViewById(R.id.whatsapp_image);
        phoneCall_image = findViewById(R.id.phoneCall_image);
        back_Image = findViewById(R.id.back_Image);
        name_text = findViewById(R.id.name_text);
        services_layout = findViewById(R.id.services_layout);
        service_recycleView = findViewById(R.id.service_recycleView);
        complain_image = findViewById(R.id.complain_image);

    }



    private void getIntentValue()
    {
        Intent intent = getIntent();
        garageName = intent.getStringExtra("garageName");
        garageOwnerName = intent.getStringExtra("garageOwnerName");
        garageContactNumber = intent.getStringExtra("garageContactNumber");
        garageBio = intent.getStringExtra("garageBio");
        ownerUserUID = intent.getStringExtra("ownerUserUID");
        garageAddKey = intent.getStringExtra("garageAddKey");
        currentTime = intent.getStringExtra("currentTime");
        currentDate = intent.getStringExtra("currentDate");
        garageCoverLink = intent.getStringExtra("garageCoverLink");
        garageAddress = intent.getStringExtra("garageAddress");

        marqueeName_text.setText(garageName);
        name_text.setText(garageName);
        marqueeBio_text.setText(garageBio);
        ownerName_text.setText(garageOwnerName);
        phone_text.setText(garageContactNumber);
        marqueeAddress_text.setText(garageAddress);

        getImageSlider(garageAddKey,ownerUserUID);
        getLocationValues(garageAddKey,ownerUserUID);
        getServices(garageAddKey,ownerUserUID);


    }

    private void getServices(String garageAddKey, String ownerUserUID)
    {

        serviceList = new ArrayList<>();
        servicesAdapter = new ServicesAdapter(this,serviceList);
        service_recycleView.setLayoutManager(new LinearLayoutManager(GarageDetailsActivity.this));
        service_recycleView.setAdapter(servicesAdapter);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Services");
        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//               String checkID =snapshot.child("userUID").getValue(String.class);
                ServicesModel model = snapshot.getValue(ServicesModel.class);
                assert model != null;
                if (model.getUserUID().equals(ownerUserUID))
                {
                    services_layout.setVisibility(View.VISIBLE);
                    serviceList.add(model);
                    servicesAdapter.notifyDataSetChanged();
                }else
                {
                    services_layout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLocationValues(String marqueeAddKey, String ownerUserUID)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Garage")
                .child(marqueeAddKey).child("garageLocation");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    placeLatitude = snapshot.child("marqueeLatitude").getValue(String.class);
                    placeLongitude = snapshot.child("marqueeLongitude").getValue(String.class);
                    setMarkOnMap(placeLatitude,placeLongitude);
                }else
                {
                    Toast.makeText(GarageDetailsActivity.this, "No Location Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setMarkOnMap(String placeLatitude, String placeLongitude)
    {
//Assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //initilize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        //check permission
        if (ActivityCompat.checkSelfPermission(GarageDetailsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            //when permission granted
            //Call Method
            createFalgOnMap(placeLatitude,placeLongitude);

        }
        else
        {
            //when permission denied
            //requst permission
            ActivityCompat.requestPermissions(GarageDetailsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }


    }

    private void createFalgOnMap(String placeLatitude, String placeLongitude)
    {
//        LatLng destinationPosition = new LatLng(Double.parseDouble(placeLatitude), Double.parseDouble(placeLongitude));
//
//        // for destination
//        map.addMarker(new MarkerOptions().position(destinationPosition)
//                        .title(marqueeName)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                        .snippet("Destination")
//                        .alpha(1f))
//                .showInfoWindow();
//
//        map.moveCamera(CameraUpdateFactory.newLatLng(destinationPosition));
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationPosition, 13.0f));
//        map.getUiSettings().setCompassEnabled(true);
//        map.getUiSettings().setZoomControlsEnabled(true);

        //initilize task location
        @SuppressLint("MissingPermission")
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                //when Success
                if (location != null)
                {

                    //when location in not equal to null
                    //Get current Latitude
                    currentLat = location.getLatitude();

                    //get Current Longitude
                    currentLong = location.getLongitude();

                    double placeLat = Double.parseDouble(placeLatitude);
                    double placeLong = Double.parseDouble(placeLongitude);

                    //synMap
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            //when map is ready
                            map = googleMap;

                            //    Just show current location
                            //initilize lat lag
                            LatLng latLng = new LatLng(Double.parseDouble(placeLatitude),Double.parseDouble(placeLongitude));

                            //craete marker option
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title(garageName);

                            //zoom map
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(placeLat,placeLong),10));

                            //add marker on map
                            googleMap.addMarker(options);

                        }
                    });
                }
            }
        });
    }

    private void getImageSlider(String marqueeAddKey, String ownerUserUID)
    {
        ImageModel model = new ImageModel();
        model.setGarageImages(garageCoverLink);

        mDataList = new ArrayList<>();
        mDataList.add(model);

        adapter = new ImageAdapter(this,mDataList);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        imageSlider_recycleView.setLayoutManager(linearLayoutManager);
        imageSlider_recycleView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Garage")
                .child(marqueeAddKey).child("moreImages");
        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    ImageModel model = snapshot.getValue(ImageModel.class);
                    mDataList.add(model);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else
                {
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        progressDialog.dismiss();
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