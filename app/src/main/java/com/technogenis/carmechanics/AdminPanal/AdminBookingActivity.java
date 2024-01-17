package com.technogenis.carmechanics.AdminPanal;

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

import com.technogenis.carmechanics.Adapter.BookingTabsAdapter;
import com.technogenis.carmechanics.R;

public class AdminBookingActivity extends AppCompatActivity {

    ViewPager2 booking_viewpager;
    LinearLayout booking_pending_layout,booking_cancel_layout;
    TextView booking_pending_text,booking_cancel_text;
    View booking_pending_view,booking_cancel_view;

    ImageView back_image;
    BookingTabsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking);

        initViews();

        back_image.setOnClickListener(v -> {
            startActivity(new Intent(AdminBookingActivity.this,AdminMainActivity.class));
            finish();
        });

        adapter = new BookingTabsAdapter(this);
        booking_viewpager.setUserInputEnabled(true);
        booking_viewpager.setAdapter(adapter);

        booking_viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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

        booking_pending_text.setTextColor(ContextCompat.getColor(this,R.color.dark_blue));
        booking_pending_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.dark_blue,this.getTheme()));

        booking_cancel_text.setTextColor(ContextCompat.getColor(this,R.color.gray));
        booking_cancel_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,this.getTheme()));


        booking_pending_layout.setOnClickListener(v -> {
            booking_viewpager.setCurrentItem(0,true);
        });

        booking_cancel_layout.setOnClickListener(v -> {
            booking_viewpager.setCurrentItem(1,true);
        });
    }

    private void initViews()
    {
        booking_viewpager = findViewById(R.id.booking_viewpager);
        booking_pending_layout = findViewById(R.id.booking_pending_layout);
        booking_cancel_layout = findViewById(R.id.booking_cancel_layout);
        booking_cancel_text = findViewById(R.id.booking_cancel_text);
        booking_pending_text = findViewById(R.id.booking_pending_text);
        booking_pending_view = findViewById(R.id.booking_pending_view);
        booking_cancel_view = findViewById(R.id.booking_cancel_view);
        back_image = findViewById(R.id.back_image);
    }

    private void onChangeTab(int position)
    {
        if (position == 0)
        {
            booking_pending_text.setTextColor(ContextCompat.getColor(this,R.color.dark_blue));
            booking_pending_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.dark_blue,this.getTheme()));

            booking_cancel_text.setTextColor(ContextCompat.getColor(this,R.color.gray));
            booking_cancel_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,this.getTheme()));

        }

        if (position == 1)
        {
            booking_pending_text.setTextColor(ContextCompat.getColor(this,R.color.gray));
            booking_pending_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,this.getTheme()));

            booking_cancel_text.setTextColor(ContextCompat.getColor(this,R.color.dark_blue));
            booking_cancel_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.dark_blue,this.getTheme()));


        }

    }
}