package com.technogenis.carmechanics.AdminAppMain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.Adapter.AppGarageAdapter;
import com.technogenis.carmechanics.Model.GarageModel;
import com.technogenis.carmechanics.R;

import java.util.ArrayList;

public class LogGarageActivity extends AppCompatActivity {

    RecyclerView recycleView;
    ImageView back_image;
    AppGarageAdapter adapter;
    ArrayList<GarageModel> mDataList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_garage);

        recycleView = findViewById(R.id.recycleView);

        back_image = findViewById(R.id.back_image);
        back_image.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });


        progressDialog = ProgressDialog.show(LogGarageActivity.this, "", "Processing", true);

        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        mDataList=new ArrayList<>();
        adapter = new AppGarageAdapter(this,mDataList);
        recycleView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Garage");

        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    GarageModel model = snapshot.getValue(GarageModel.class);
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
}