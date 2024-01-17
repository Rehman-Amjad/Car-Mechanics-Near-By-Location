package com.technogenis.carmechanics.AdminAppMain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.Model.UserModel;
import com.technogenis.carmechanics.R;
import com.technogenis.carmechanics.Start.ConfirmActivity;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    TextView total_garage_account_text,total_user_account_text;
    CardView garage_card,user_card,complain_card,garageList_card,notification_card;
    ArrayList<UserModel> garageList;
    ArrayList<UserModel> userList;
    ImageView notification_image,logout_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        getGarageCount();
        getCustomerCounts();

        garageList_card.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this,LogGarageActivity.class));
        });

        complain_card.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this,AppComplainActivity.class));
        });

        notification_card.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this,AppNotificationActivity.class));
        });

        garage_card.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this, GarageActivity.class));
        });

        user_card.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this, UserActivity.class));
        });

        logout_image.setOnClickListener(v -> {
            startActivity(new Intent(Dashboard.this, ConfirmActivity.class));
            finish();
        });
    }

    private void getCustomerCounts()
    {
        userList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UsersAccounts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    UserModel model = snapshot.getValue(UserModel.class);
                    userList.add(model);
                    total_user_account_text.setText(String.valueOf(userList.size()));
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

    private void getGarageCount()
    {

        garageList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GarageOwnerAccounts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    UserModel model = snapshot.getValue(UserModel.class);
                    garageList.add(model);
                    total_garage_account_text.setText(String.valueOf(garageList.size()));
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

    private void initViews()
    {
        total_garage_account_text = findViewById(R.id.total_garage_account_text);
        total_user_account_text = findViewById(R.id.total_user_account_text);
        garage_card = findViewById(R.id.garage_card);
        user_card = findViewById(R.id.user_card);
        complain_card = findViewById(R.id.complain_card);
        notification_image = findViewById(R.id.notification_image);
        garageList_card = findViewById(R.id.garageList_card);
        notification_card = findViewById(R.id.notification_card);
        logout_image = findViewById(R.id.logout_image);
    }
}