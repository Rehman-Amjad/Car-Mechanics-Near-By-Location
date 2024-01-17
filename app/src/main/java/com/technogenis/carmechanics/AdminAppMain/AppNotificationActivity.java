package com.technogenis.carmechanics.AdminAppMain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.R;

import java.util.HashMap;
import java.util.Map;

public class AppNotificationActivity extends AppCompatActivity {

    TextView notification_text;
    EditText ed_massage;
    Button btn_send;
    String notification;
    ImageView back_image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_notification);

        notification_text = findViewById(R.id.notification_text);
        ed_massage = findViewById(R.id.ed_massage);
        btn_send = findViewById(R.id.btn_send);
        back_image = findViewById(R.id.back_image);


        getNotifications();


        back_image.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });


        btn_send.setOnClickListener(v -> {
            notification = ed_massage.getText().toString().trim();

            if (notification.isEmpty())
            {
                ed_massage.setError("field required");
                ed_massage.requestFocus();
                return;
            }
            else
            {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String,String> map = new HashMap<>();
                        map.put("userNotifications",notification);
                        reference.setValue(map);
                        getNotifications();
                        notification_text.setText(" ");
                        Toast.makeText(AppNotificationActivity.this, "Notifications Send.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
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
                    notification_text.setVisibility(View.VISIBLE);
                    notification = snapshot.child("userNotifications").getValue(String.class);
                    notification_text.setText(notification);
                }
                else
                {
                    notification_text.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}