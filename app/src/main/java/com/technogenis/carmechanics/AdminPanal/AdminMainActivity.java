package com.technogenis.carmechanics.AdminPanal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.Adapter.ImageSliderAdapter;
import com.technogenis.carmechanics.R;
import com.technogenis.carmechanics.Start.ConfirmActivity;

import java.util.Timer;
import java.util.TimerTask;

public class AdminMainActivity extends AppCompatActivity {

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

    CardView addMarque_card,serviceList_card,booking_card;
    TextView notification_text;
    LinearLayout notification_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mAuth  = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userUID = mAuth.getUid();

        initviews();
//        getUserValues();
        drawerCode();
        sliderCode();
        getNotifications();

        notification_layout.setVisibility(View.GONE);

        addMarque_card.setOnClickListener(v -> {
//            startActivity(new Intent(AdminMainActivity.this,AddGarageActivity.class));
            startActivity(new Intent(AdminMainActivity.this,GarageListActivity.class));
        });

        serviceList_card.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this,ServicesActivity.class));
        });

        booking_card.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this,AdminBookingActivity.class));
        });
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

    private void getUserValues()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GarageOwnerAccounts")
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

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this,ConfirmActivity.class));
            finish();
        });

        btn_logout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent( AdminMainActivity.this, ConfirmActivity.class));
            finish();
        });
    }

    private void initviews()
    {
        dashText1 = findViewById(R.id.dashText1);
        viewPager = findViewById(R.id.header_slider);
        addMarque_card = findViewById(R.id.addMarque_card);
        serviceList_card = findViewById(R.id.serviceList_card);
        booking_card = findViewById(R.id.booking_card);
        notification_layout = findViewById(R.id.notification_layout);
        notification_text = findViewById(R.id.notification_text);
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
                    startActivity(new Intent(AdminMainActivity.this, AdminMainActivity.class));
                    drayerLayout.closeDrawer(GravityCompat.START);
                }
                    // Commenting out the block for nav_myProfile as it's currently disabled
                    /*
                    else if (itemId == R.id.nav_myProfile) {
                        startActivity(new Intent(AdminMainActivity.this, AdminProfileActivity.class));
                        drayerLayout.closeDrawer(GravityCompat.START);
                    }
                    */
                else if (itemId == R.id.nav_marquee) {
                    startActivity(new Intent(AdminMainActivity.this, AddGarageActivity.class));
                    drayerLayout.closeDrawer(GravityCompat.START);
                } else if (itemId == R.id.nav_logout) {
                    mAuth.signOut();
                    startActivity(new Intent(AdminMainActivity.this, ConfirmActivity.class));
                    finish();
                    drayerLayout.closeDrawer(GravityCompat.START);
                }
// No need for a break statement in this structure



//                switch (item.getItemId())
//                {
//
//                    case R.id.nav_home:
//                        finish();
//                        startActivity(new Intent(AdminMainActivity.this,AdminMainActivity.class));
//                        drayerLayout.closeDrawer(GravityCompat.START);
//                        break;
//
////                    case R.id.nav_myProfile:
////                        startActivity(new Intent(AdminMainActivity.this,AdminProfileActivity.class));
////                        drayerLayout.closeDrawer(GravityCompat.START);
////                        break;
//
//                    case R.id.nav_marquee:
//                        startActivity(new Intent(AdminMainActivity.this,AddGarageActivity.class));
//                        drayerLayout.closeDrawer(GravityCompat.START);
//                        break;
//
//                    case R.id.nav_logout:
//                        mAuth.signOut();
//                        startActivity(new Intent(AdminMainActivity.this, ConfirmActivity.class));
//                        finish();
//                        drayerLayout.closeDrawer(GravityCompat.START);
//                        break;
//
//                }
                return false;
            }
        });
    }

    private void sliderCode()
    {

        adapter = new ImageSliderAdapter(this);
        viewPager.setAdapter(adapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            AdminMainActivity.this.runOnUiThread(new Runnable()
            {

                @Override
                public void run() {

                    myanim = AnimationUtils.loadAnimation(AdminMainActivity.this,R.anim.left_to_right_anim);

                    if(viewPager.getCurrentItem() == 0)
                    {
                        viewPager.setCurrentItem(1);
                        dashText1.setText("Check Garage Details");
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
                        dashText1.setText("Live chat with Admin");
                        dashText1.startAnimation(myanim);

                    }

                    else {
                        viewPager.setCurrentItem(0);
                        dashText1.setText("Manage your Garage details");
                        dashText1.startAnimation(myanim);

                    }

                }
            });

        }
    }
}