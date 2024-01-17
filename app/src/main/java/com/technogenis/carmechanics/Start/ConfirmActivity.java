package com.technogenis.carmechanics.Start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.technogenis.carmechanics.AdminAppMain.LogActivity;
import com.technogenis.carmechanics.R;
import com.technogenis.carmechanics.UserStart.UserLoginActivity;

public class ConfirmActivity extends AppCompatActivity {

    Button btn_owner,btn_user;
    Button btn_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        initViews();

        btn_owner.setOnClickListener(v -> {
            Intent ownerIntent = new Intent(ConfirmActivity.this,LoginActivity.class);
            startActivity(ownerIntent);
        });

        btn_user.setOnClickListener(v -> {
            Intent userIntent = new Intent(ConfirmActivity.this, UserLoginActivity.class);
            startActivity(userIntent);
        });

        btn_admin.setOnClickListener(v -> {
            Intent userIntent = new Intent(ConfirmActivity.this, LogActivity.class);
            startActivity(userIntent);
        });
    }

    private void initViews()
    {
        btn_owner = findViewById(R.id.btn_owner);
        btn_user = findViewById(R.id.btn_user);
        btn_admin = findViewById(R.id.btn_admin);
    }
}