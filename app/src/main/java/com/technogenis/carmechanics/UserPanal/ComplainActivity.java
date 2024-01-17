package com.technogenis.carmechanics.UserPanal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.Model.ChatModel;
import com.technogenis.carmechanics.Model.ComplainModel;
import com.technogenis.carmechanics.R;
import com.technogenis.carmechanics.UserAdapter.ComplainChatAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ComplainActivity extends AppCompatActivity {

    ImageView back_image;
    RecyclerView chat_recycleView;
    EditText ed_massage;
    Button btn_send;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userUID,userEmail,massage;
    String chatType = "user";
    ArrayList<ChatModel> mDataList;
    ComplainChatAdapter adapter;

    String garageAddKey,ownerUserUID,garageName,garageContactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        mAuth  = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        userEmail = currentUser.getEmail();
        userUID  = mAuth.getCurrentUser().getUid();

        initViews();
        getIntentValues();
        getChats();

        back_image.setOnClickListener(v -> {
          getOnBackPressedDispatcher().onBackPressed();
        });

        btn_send.setOnClickListener(v -> {
            massage = ed_massage.getText().toString();

            if (massage.isEmpty())
            {
                ed_massage.setError("Type massage");
                ed_massage.requestFocus();
            }
            else
            {
                ed_massage.setText("");
                saveChatToDatabase(massage);
            }
        });
    }

    private void getChats()
    {
        mDataList = new ArrayList<>();
        adapter = new ComplainChatAdapter(this,mDataList);
        chat_recycleView.setLayoutManager(new LinearLayoutManager(this));

        chat_recycleView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ComplainChats")
                .child(userUID).child(garageAddKey);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists())
                {
                    ChatModel model = snapshot.getValue(ChatModel.class);
                    mDataList.add(model);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(ComplainActivity.this, "No Chat Found", Toast.LENGTH_SHORT).show();
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



    private void getIntentValues()
    {
        Intent intent = getIntent();
        garageAddKey = intent.getStringExtra("garageAddKey");
        ownerUserUID = intent.getStringExtra("ownerUserUID");
        garageName = intent.getStringExtra("garageName");
        garageContactNumber = intent.getStringExtra("garageContactNumber");
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        chat_recycleView = findViewById(R.id.chat_recycleView);
        ed_massage = findViewById(R.id.ed_massage);
        btn_send = findViewById(R.id.btn_send);
    }

    private void saveChatToDatabase(String massage)
    {
        String currentTime = getTimeWithAmPm();
        String currentDate = getCurrentdate();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ComplainChats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long time= System.currentTimeMillis();
                String key = String.valueOf(time);
                ComplainModel model = new ComplainModel(massage,userUID,ownerUserUID,currentTime,currentDate,key,garageName
                        ,chatType,garageContactNumber);
                reference.child(userUID).child(garageAddKey).child(key).setValue(model);
                saveAdminDetails(userUID,garageAddKey,key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveAdminDetails(String userUID, String garageKey, String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AppOwnerChats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> map = new HashMap<>();
                map.put("senderUID", userUID);
                map.put("garageAddKey", garageKey);
                map.put("ownerUserUID", ownerUserUID);
                map.put("userEmail", userEmail);
                map.put("garageName", garageName);
                map.put("garageContactNumber", garageContactNumber);
                reference.child(ownerUserUID).setValue(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getTimeWithAmPm()
    {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private String getCurrentdate()
    {
        return new SimpleDateFormat("dd/LLL/yyyy", Locale.getDefault()).format(new Date());
    }
}