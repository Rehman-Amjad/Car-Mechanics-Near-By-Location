package com.technogenis.carmechanics.AdminAppMain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.R;

public class AppAdminLoginActivity extends AppCompatActivity {

    EditText ed_userName,ed_password;
    Button btn_login;
    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_admin_login);

        initViews();

        btn_login.setOnClickListener(v -> {
            username = ed_userName.getText().toString().trim();
            password = ed_password.getText().toString().trim();
            if (isValid(username,password))
            {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AdminLogin");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            String checkUser = snapshot.child("username").getValue(String.class);
                            String checkpass = snapshot.child("password").getValue(String.class);
                            if (checkUser.equals(username))
                            {
                                if (checkpass.equals(password))
                                {
                                    startActivity(new Intent(AppAdminLoginActivity.this,Dashboard.class));
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(AppAdminLoginActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(AppAdminLoginActivity.this, "wrong username", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private boolean isValid(String username, String password)
    {
        if (username.isEmpty())
        {
            ed_userName.setError("field required!");
            ed_userName.requestFocus();
            return false;
        }

        if (password.isEmpty())
        {
            ed_password.setError("field required!");
            ed_password.requestFocus();
            return false;
        }
        return true;
    }

    private void initViews()
    {
        ed_userName = findViewById(R.id.ed_userName);
        ed_password = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);
    }
}