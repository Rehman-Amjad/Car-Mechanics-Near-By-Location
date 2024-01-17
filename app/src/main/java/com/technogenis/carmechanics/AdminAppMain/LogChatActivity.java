package com.technogenis.carmechanics.AdminAppMain;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.Adapter.AdminChatAdapter;
import com.technogenis.carmechanics.Model.ChatModel;
import com.technogenis.carmechanics.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LogChatActivity extends AppCompatActivity {

    String marqueeAddKey,ownerUserUID,senderUID,userEmail;

    RecyclerView chat_recycleView;
    ImageView back_image;
    EditText ed_massage;
    Button btn_send;

    String massage;

    String userUID = "-ajsdajmdkkndkakdamd";

    String chatType = "owner";
    ArrayList<ChatModel> mDataList;
    AdminChatAdapter adapter;
    TextView title_text;
    String marqueeOwnerName = "",marqueeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_chat);

        initViews();
        getIntentValue();
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
                ChatModel model = new ChatModel(massage,userUID,ownerUserUID,currentTime,currentDate,key,marqueeOwnerName
                        ,chatType);
                reference.child(senderUID).child(marqueeAddKey).child(key).setValue(model);
                saveAdminDetails(userUID,marqueeAddKey,key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveAdminDetails(String userUID,
                                  String marqueeAddKey, String key)
    {
        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("UsersChats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> map = new HashMap<>();
                map.put("senderUID",userUID);
                map.put("marqueeAddKey",marqueeAddKey);
                map.put("ownerUserUID",ownerUserUID);
                map.put("userEmail",userEmail);
                map.put("marqueeName",marqueeName);
                reference.child(senderUID).setValue(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews()
    {
        chat_recycleView = findViewById(R.id.chat_recycleView);
        back_image = findViewById(R.id.back_image);
        btn_send = findViewById(R.id.btn_send);
        ed_massage = findViewById(R.id.ed_massage);
        title_text = findViewById(R.id.title_text);
    }

    private void getChats()
    {
        mDataList = new ArrayList<>();
        adapter = new AdminChatAdapter(this,mDataList);
        chat_recycleView.setLayoutManager(new LinearLayoutManager(this));

        chat_recycleView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ComplainChats")
                .child(senderUID).child(marqueeAddKey);

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
                    Toast.makeText(LogChatActivity.this, "No Chat Found", Toast.LENGTH_SHORT).show();
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

    private void getIntentValue()
    {
        Intent intent = getIntent();
        marqueeAddKey = intent.getStringExtra("garageAddKey");
        ownerUserUID = intent.getStringExtra("ownerUserUID");
        senderUID = intent.getStringExtra("senderUID");
        userEmail = intent.getStringExtra("userEmail");
        marqueeName = intent.getStringExtra("garageName");

        title_text.setText(userEmail);
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