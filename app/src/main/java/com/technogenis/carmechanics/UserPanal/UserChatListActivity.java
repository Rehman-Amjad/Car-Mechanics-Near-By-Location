package com.technogenis.carmechanics.UserPanal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.technogenis.carmechanics.Model.AdminChatListModel;
import com.technogenis.carmechanics.R;
import com.technogenis.carmechanics.UserAdapter.UserChatListAdapter;

import java.util.ArrayList;

public class UserChatListActivity extends AppCompatActivity {

    RecyclerView recycleView;
    FirebaseAuth mAuth;
    String userUID;
    ImageView back_image;
    UserChatListAdapter adapter;
    ArrayList<AdminChatListModel> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat_list);

        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getUid();
        initiViews();

        back_image = findViewById(R.id.back_image);
        back_image.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getuserChatLists();

    }

    private void getuserChatLists()
    {
        mDataList = new ArrayList<>();
        adapter = new UserChatListAdapter(this,mDataList);
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
                    Toast.makeText(UserChatListActivity.this, "No Chat Found", Toast.LENGTH_SHORT).show();
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
    }
}