package com.technogenis.carmechanics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.Adapter.ImageSliderAdapter;
import com.technogenis.carmechanics.Model.GarageModel;
import com.technogenis.carmechanics.NearByPlace.CONSTANTS;
import com.technogenis.carmechanics.NearByPlace.EducationActivity;
import com.technogenis.carmechanics.NearByPlace.SharedPref;
import com.technogenis.carmechanics.Start.ConfirmActivity;
import com.technogenis.carmechanics.UserAdapter.GarageListAdapter;
import com.technogenis.carmechanics.UserPanal.BookingActivity;
import com.technogenis.carmechanics.UserPanal.UserChatListActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    ViewPager viewPager;
    ImageSliderAdapter adapter;

    NavigationView navMenu;
    ActionBarDrawerToggle toggle;
    DrawerLayout drayerLayout;

    TextView dashText1;
    Animation myanim;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String userUID;

    String accountCreationKey,email,name,password,phoneNumber,key;

    public static final int ERROR_DIALOG_REQUEST_DOWNLOAD = 101;
    public static final int PERMISSION_REQUEST_ENABLE_GPS = 102;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 103;
    private boolean mLocationPermissionGranted = false;

//    SearchView myserachview;
//    ListView myList;
//
//    ArrayList<String> list;
//    ArrayAdapter<String> adapter;
    //todo::get user current location

    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;

    SharedPref sharedPref;

    RecyclerView garageRecycleView;
    GarageListAdapter garageAdapter;
    ArrayList<GarageModel> mDataList;

    ProgressBar progressbar;

    TextView notification_text;
    LinearLayout notification_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth  = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userUID = mAuth.getUid();

        initviews();
//        getUserValues();
        drawerCode();
        sliderCode();
        getNotifications();
        notification_layout.setVisibility(View.GONE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sharedPref = SharedPref.getInstance(this);

        getPermission();
        checkAppPermissions();
        getLocation();

        getGarageData();
    }

    private void getNotifications()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String notification;
                    notification_layout.setVisibility(View.VISIBLE);
                    notification = snapshot.child("userNotifications").getValue(String.class);
                    notification_text.setText(notification);
                }
                else
                {
                    notification_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getGarageData()
    {
        garageRecycleView.setHasFixedSize(true);
        garageRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mDataList=new ArrayList<>();
        garageAdapter = new GarageListAdapter(this,mDataList);
        garageRecycleView.setAdapter(garageAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Garage");

        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    GarageModel model = snapshot.getValue(GarageModel.class);
                    mDataList.add(model);
                    garageAdapter.notifyDataSetChanged();
                    progressbar.setVisibility(View.GONE);
                }else{
                    progressbar.setVisibility(View.VISIBLE);
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


    private void getUserValues()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersAccounts")
                .child(userUID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    accountCreationKey = snapshot.child("accountCreationKey").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    name = snapshot.child("name").getValue(String.class);
                    password = snapshot.child("password").getValue(String.class);
                    phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                }else
                {
                    accountCheckDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void accountCheckDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.account_check_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_back = layout_dialog.findViewById(R.id.btn_back);
        Button btn_logout = layout_dialog.findViewById(R.id.btn_logout);
        TextView male_text = layout_dialog.findViewById(R.id.male_text);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        male_text.setText("user account not found");

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent( MainActivity.this, ConfirmActivity.class));
            finish();
        });

        btn_logout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent( MainActivity.this, ConfirmActivity.class));
            finish();
        });


    }

    private void initviews()
    {
        dashText1 = findViewById(R.id.dashText1);
        viewPager = findViewById(R.id.header_slider);
        garageRecycleView = findViewById(R.id.marqueeRecycleView);
        progressbar = findViewById(R.id.progressbar);
        notification_text = findViewById(R.id.notification_text);
        notification_layout = findViewById(R.id.notification_layout);
    }

    private void drawerCode()
    {
        Toolbar toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        navMenu=findViewById(R.id.navMenu);
        drayerLayout=findViewById(R.id.drawerlayout);


        toggle=new ActionBarDrawerToggle(this,drayerLayout,toolbar,R.string.app_name,R.string.app_name);
        drayerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    drayerLayout.closeDrawer(GravityCompat.START);
                }
                    // Commenting out the block for nav_myProfile as it's currently disabled
                    /*
                    else if (itemId == R.id.nav_myProfile) {
                        startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                        drayerLayout.closeDrawer(GravityCompat.START);
                    }
                    */
                else if (itemId == R.id.nav_mybooking) {
                    startActivity(new Intent(MainActivity.this, BookingActivity.class));
                    drayerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.nav_nearbyMarquee) {
                    Intent intent = new Intent(getApplicationContext(), EducationActivity.class);
                    intent.putExtra("latitude", String.valueOf(latitude));
                    intent.putExtra("longitude", String.valueOf(longitude));
                    startActivity(intent);
                    drayerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.nav_chats) {
                    startActivity(new Intent(getApplicationContext(), UserChatListActivity.class));
                    drayerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.nav_logout) {
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, ConfirmActivity.class));
                    finish();
                    drayerLayout.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
    }

    private void sliderCode()
    {

        adapter = new ImageSliderAdapter(this);
        viewPager.setAdapter(adapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MainActivity.MyTimerTask(), 2000, 4000);
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable()
            {

                @Override
                public void run() {

                    myanim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.left_to_right_anim);

                    if(viewPager.getCurrentItem() == 0)
                    {
                        viewPager.setCurrentItem(1);
                        dashText1.setText("Check Booking Details for your marquee");
                        dashText1.startAnimation(myanim);


                    }
                    else if(viewPager.getCurrentItem() == 1)
                    {
                        viewPager.setCurrentItem(2);
                        dashText1.setText("Grow Up and Build a Bright Future");
                        dashText1.startAnimation(myanim);

                    }

                    else if (viewPager.getCurrentItem() == 2)
                    {
                        viewPager.setCurrentItem(3);
                        dashText1.setText("Live chat with their customers");
                        dashText1.startAnimation(myanim);

                    }

                    else {
                        viewPager.setCurrentItem(0);
                        dashText1.setText("Manage your marquee details");
                        dashText1.startAnimation(myanim);

                    }

                }
            });

        }
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {

            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    }, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    private boolean checkAppPermissions() {

        if (isServiceOK()) {
            if (isLocationEnables()) {

                return true;
            }

        }

        return false;
    }

    public boolean isServiceOK() {
//        Log.d(TAG,"isServiceOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
//            Log.d(TAG,"isServiceOK: Google Play Services is working properly");
            return true;

        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
//            Log.d(TAG,"isServiceOK: Google Play Services can install and we can fix it");

            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST_DOWNLOAD);
            dialog.show();

        } else {
//            Log.d(TAG,"isServiceOK: You can't make map request");

            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();

        }


        return false;
    }

    private boolean isLocationEnables() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGPS();
            return false;

        }

        return true;
    }

    private void buildAlertMessageNoGPS() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSION_REQUEST_ENABLE_GPS);

                    }
                });

        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    protected void getLocation() {
        if (isLocationEnabled(MainActivity.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                sharedPref.saveData(CONSTANTS.KEY_LATITUDE,String.valueOf(latitude));
                sharedPref.saveData(CONSTANTS.KEY_LONGITUDE,String.valueOf(longitude));

//                Toast.makeText(this, String.valueOf(latitude)
//                        + "," + String.valueOf(longitude), Toast.LENGTH_SHORT).show();

            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }
        else {
            //prompt user to enable location....
            //.................
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        sharedPref.saveData(CONSTANTS.KEY_LATITUDE,String.valueOf(latitude));
        sharedPref.saveData(CONSTANTS.KEY_LONGITUDE,String.valueOf(longitude));

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}