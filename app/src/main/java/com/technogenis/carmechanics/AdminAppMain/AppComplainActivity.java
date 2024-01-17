package com.technogenis.carmechanics.AdminAppMain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.Adapter.ChatListAdapter;
import com.technogenis.carmechanics.Model.AdminChatListModel;
import com.technogenis.carmechanics.R;

import java.util.ArrayList;

public class AppComplainActivity extends AppCompatActivity {

    RecyclerView recycleView;
    String userUID;
    ImageView back_image;
    ChatListAdapter adapter;
    ArrayList<AdminChatListModel> mDataList;
    TextView garage_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_complain);

        initiViews();

        back_image = findViewById(R.id.back_image);
        back_image.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getuserChatLists();

        garage_delete.setVisibility(View.GONE);
    }

    private void getuserChatLists()
    {
        mDataList = new ArrayList<>();
        adapter = new ChatListAdapter(this,mDataList);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        recycleView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AppOwnerChats");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists())
                {
                    AdminChatListModel model = snapshot.getValue(AdminChatListModel.class);
                    mDataList.add(model);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(AppComplainActivity.this, "No Chat Found", Toast.LENGTH_SHORT).show();
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

    private void initiViews()
    {
        recycleView = findViewById(R.id.recycleView);
        garage_delete = findViewById(R.id.garage_delete);

    }
}