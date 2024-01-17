package com.technogenis.carmechanics.AdminPanal;

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
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.Adapter.MyGarageAdapter;
import com.technogenis.carmechanics.Model.GarageModel;
import com.technogenis.carmechanics.R;

import java.util.ArrayList;

public class GarageListActivity extends AppCompatActivity {

    TextView tv_addNew;
    RecyclerView marqueeRecycleView;
    MyGarageAdapter adapter;
    ArrayList<GarageModel> mDataList;
    ProgressDialog progressDialog;
    ImageView back_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_list);

        progressDialog = ProgressDialog.show(GarageListActivity.this, "", "Processing", true);
        initViews();
        GarageList();

        back_Image.setOnClickListener(v -> {
            onBackPressed();
        });

        tv_addNew.setOnClickListener(v -> {
            startActivity(new Intent(GarageListActivity.this,AddGarageActivity.class));
        });
    }

    private void GarageList()
    {
        marqueeRecycleView.setHasFixedSize(true);
        marqueeRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mDataList=new ArrayList<>();
        adapter = new MyGarageAdapter(this,mDataList);
        marqueeRecycleView.setAdapter(adapter);

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

    private void initViews()
    {
        tv_addNew = findViewById(R.id.tv_addNew);
        marqueeRecycleView = findViewById(R.id.marqueeRecycleView);
        back_Image = findViewById(R.id.back_Image);
    }
}