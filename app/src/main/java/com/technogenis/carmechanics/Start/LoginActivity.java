package com.technogenis.carmechanics.Start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.technogenis.carmechanics.AdminPanal.AdminMainActivity;
import com.technogenis.carmechanics.PageAdapter.LoginPagerAdapter;
import com.technogenis.carmechanics.R;

public class LoginActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    LoginPagerAdapter adapter;
    LinearLayout signIn_layout,signUp_layout;
    TextView signIn_text,signUp_text;
    View signIn_view,signUp_view;
    ImageView back_image;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = mAuth.getCurrentUser();
        if (currentUser!=null)
        {
            startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        mAuth = FirebaseAuth.getInstance();

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        adapter  = new LoginPagerAdapter(this);
        viewPager.setUserInputEnabled(true);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        signIn_text.setTextColor(ContextCompat.getColor(this,R.color.white));
        signIn_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));

        signUp_text.setTextColor(ContextCompat.getColor(this,R.color.gray));
        signUp_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,getTheme()));

        signIn_layout.setOnClickListener(v -> {
            viewPager.setCurrentItem(0,true);
        });

        signUp_layout.setOnClickListener(v -> {
            viewPager.setCurrentItem(1,true);
        });
    }

    private void onChangeTab(int position)
    {
        if (position == 0)
        {
            signIn_text.setTextColor(ContextCompat.getColor(this,R.color.white));
            signIn_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));

            signUp_text.setTextColor(ContextCompat.getColor(this,R.color.gray));
            signUp_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,getTheme()));
        }
        if (position == 1)
        {
            signIn_text.setTextColor(ContextCompat.getColor(this,R.color.gray));
            signIn_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,getTheme()));

            signUp_text.setTextColor(ContextCompat.getColor(this,R.color.white));
            signUp_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
        }
    }

    private void initViews()
    {
        viewPager = findViewById(R.id.viewPager);
        signIn_layout = findViewById(R.id.signIn_layout);
        signUp_layout = findViewById(R.id.signUp_layout);
        signIn_text = findViewById(R.id.signIn_text);
        signUp_text = findViewById(R.id.signUp_text);
        signIn_view = findViewById(R.id.signIn_view);
        signUp_view = findViewById(R.id.signUp_view);
        back_image = findViewById(R.id.back_image);
    }
}