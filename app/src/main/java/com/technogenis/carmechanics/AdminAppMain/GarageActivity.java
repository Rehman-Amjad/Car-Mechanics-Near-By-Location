package com.technogenis.carmechanics.AdminAppMain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.Adapter.GarageOwnerAdapter;
import com.technogenis.carmechanics.Model.UserModel;
import com.technogenis.carmechanics.R;

import java.util.ArrayList;

public class GarageActivity extends AppCompatActivity {

    RecyclerView recycleView;
    ImageView back_image;
    GarageOwnerAdapter adapter;
    ArrayList<UserModel> mDataList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);

        initViews();

        back_image.setOnClickListener(v -> {
            startActivity(new Intent(GarageActivity.this,Dashboard.class));
            finish();
        });

        progressDialog = ProgressDialog.show(GarageActivity.this, "", "Processing", true);

        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        mDataList=new ArrayList<>();
        adapter = new GarageOwnerAdapter(this,mDataList);
        recycleView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GarageOwnerAccounts");

        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    UserModel model = snapshot.getValue(UserModel.class);
                    mDataList.add(model);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else{
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
    private void initViews()
    {
        recycleView = findViewById(R.id.recycleView);
        back_image = findViewById(R.id.back_image);
    }
}