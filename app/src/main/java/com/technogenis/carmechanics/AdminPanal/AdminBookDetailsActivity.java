package com.technogenis.carmechanics.AdminPanal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.R;

public class AdminBookDetailsActivity extends AppCompatActivity {

    ImageView back_image;
    TextView orderDate_text,orderTime_text,order_status_text,bookingKey_text,fullName_text
            ,phoneNumber_text,persons_text,message_text,bookDate_text,bookTime_text,marqueeName_text
            ,marqueeAddress_text;

    String name,phoneNumber,persons,message,bookDate,bookTime,bookKey,userUID,orderDate,orderTime
            ,bookRequest,bookMarqueeName,bookMarqueeAddress,bookMarqueeContact;

    TextView pending_text,cancel_text;
    ProgressDialog progressDialog;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book_details);

        initViews();
        getIntentValues();

        back_image.setOnClickListener(v -> {
            startActivity(new Intent(AdminBookDetailsActivity.this,AdminBookingActivity.class));
            finish();
        });

        pending_text.setOnClickListener(v -> {

            progressDialog = ProgressDialog.show(this, "", "Processing", true);

            pending_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_blue));
            pending_text.setTextColor(ContextCompat.getColor(this,R.color.white));

            cancel_text.setBackground(ContextCompat.getDrawable(this, R.drawable.border_layout));
            cancel_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
            cancel_text.setTextColor(ContextCompat.getColor(this,R.color.black));

            response  = "pending";
            saveResponseToDataBase(response);
        });

        cancel_text.setOnClickListener(v -> {

            progressDialog = ProgressDialog.show(this, "", "Processing", true);

            pending_text.setBackground(ContextCompat.getDrawable(this, R.drawable.border_layout));
            pending_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
            pending_text.setTextColor(ContextCompat.getColor(this,R.color.black));

            cancel_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_blue));
            cancel_text.setTextColor(ContextCompat.getColor(this,R.color.white));

            response  = "cancel";
            saveResponseToDataBase(response);
        });
    }

    @SuppressLint("SetTextI18n")
    private void getIntentValues()
    {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phoneNumber = intent.getStringExtra("phoneNumber");
        persons = intent.getStringExtra("persons");
        message = intent.getStringExtra("message");
        bookDate = intent.getStringExtra("bookDate");
        bookTime = intent.getStringExtra("bookTime");
        bookKey = intent.getStringExtra("bookKey");
        userUID = intent.getStringExtra("userUID");
        orderDate = intent.getStringExtra("orderDate");
        orderTime = intent.getStringExtra("orderTime");
        bookRequest = intent.getStringExtra("bookRequest");
        bookMarqueeName = intent.getStringExtra("bookGarageName");
        bookMarqueeAddress = intent.getStringExtra("bookGarageAddress");
        bookMarqueeContact = intent.getStringExtra("bookGarageContact");

        fullName_text.setText(name);
        phoneNumber_text.setText(phoneNumber);
        persons_text.setText("Total Persons " +persons);
        message_text.setText(message);
        bookDate_text.setText(bookDate);
        bookTime_text.setText(bookTime);
        bookingKey_text.setText(bookKey);
        orderDate_text.setText(orderDate);
        orderTime_text.setText(orderTime);
        order_status_text.setText(bookRequest);
        marqueeName_text.setText(bookMarqueeName);
        marqueeAddress_text.setText(bookMarqueeAddress);
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        orderDate_text = findViewById(R.id.orderDate_text);
        orderTime_text = findViewById(R.id.orderTime_text);
        order_status_text = findViewById(R.id.order_status_text);
        bookingKey_text = findViewById(R.id.bookingKey_text);
        fullName_text = findViewById(R.id.fullName_text);
        phoneNumber_text = findViewById(R.id.phoneNumber_text);
        persons_text = findViewById(R.id.persons_text);
        message_text = findViewById(R.id.message_text);
        bookDate_text = findViewById(R.id.bookDate_text);
        bookTime_text = findViewById(R.id.bookTime_text);
        marqueeName_text = findViewById(R.id.marqueeName_text);
        marqueeAddress_text = findViewById(R.id.marqueeAddress_text);

        pending_text = findViewById(R.id.pending_text);
        cancel_text = findViewById(R.id.cancel_text);
    }

    private void saveResponseToDataBase(String response)
    {

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("ordersAdmin").child(bookKey);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    reference.child("bookRequest").setValue(response);

                    saveCustomerResponseData(response);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveCustomerResponseData(String response)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Orders").child(userUID)
                .child(bookKey);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    reference.child("bookRequest").setValue(response);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}