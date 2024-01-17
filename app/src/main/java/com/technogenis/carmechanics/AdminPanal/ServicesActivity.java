package com.technogenis.carmechanics.AdminPanal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.Adapter.MyServicesAdapter;
import com.technogenis.carmechanics.Model.ServicesModel;
import com.technogenis.carmechanics.R;

import java.util.ArrayList;
import java.util.Objects;

public class ServicesActivity extends AppCompatActivity {

    ImageView back_Image;
    TextView tv_addNew;
    RecyclerView garageRecycleView;
    MyServicesAdapter adapter;
    ArrayList<ServicesModel> mDataList;
    FirebaseAuth mAuth;
    String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);


        mAuth = FirebaseAuth.getInstance();
        userUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        initViews();
        getServicesValues();

        back_Image.setOnClickListener(v -> {
          getOnBackPressedDispatcher().onBackPressed();
        });


        tv_addNew.setOnClickListener(v -> {
            Intent intent = new Intent(ServicesActivity.this,AddNewServicesActivity.class);
            intent.putExtra("screenType","Activity");
            startActivity(intent);
        });
    }

    private void getServicesValues()
    {
        mDataList=new ArrayList<>();
        adapter = new MyServicesAdapter(this,mDataList);

        garageRecycleView.setHasFixedSize(true);
        garageRecycleView.setLayoutManager(new LinearLayoutManager(this));
        garageRecycleView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Services");

        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    ServicesModel model = snapshot.getValue(ServicesModel.class);

                    assert model != null;
                    if (model.getUserUID().equals(userUID))
                    {
                        mDataList.add(model);
                        adapter.notifyDataSetChanged();
                    }

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
        back_Image = findViewById(R.id.back_Image);
        tv_addNew = findViewById(R.id.tv_addNew);
        garageRecycleView = findViewById(R.id.garageRecycleView);
    }
}