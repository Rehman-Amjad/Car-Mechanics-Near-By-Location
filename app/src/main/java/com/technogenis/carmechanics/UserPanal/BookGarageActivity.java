package com.technogenis.carmechanics.UserPanal;

import static java.util.Calendar.DAY_OF_MONTH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.MainActivity;
import com.technogenis.carmechanics.Model.BookModel;
import com.technogenis.carmechanics.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookGarageActivity extends AppCompatActivity {

    EditText ed_name,ed_phoneNumber,ed_persons,ed_message;
    TextView ed_bookingDate,ed_bookingTime,title_text;
    Button btn_bookNow;
    ImageView back_Image;

    String name,phoneNumber,persons,message = "",bookingDate,bookingTime,orderDate,orderTime,userUID
            ,bookRequest = "pending",marqueeAddress,marqueeName,marqueeContact;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_garage);

        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getUid();
        initViews();

        Intent intent = getIntent();
        marqueeAddress = intent.getStringExtra("marqueeAddress");
        marqueeName = intent.getStringExtra("marqueeName");
        marqueeContact = intent.getStringExtra("marqueeContact");

        title_text.setText(marqueeName);

        back_Image.setOnClickListener(v -> {
           getOnBackPressedDispatcher().onBackPressed();
        });

        ed_bookingDate.setOnClickListener(v -> {
            datepickup(ed_bookingDate);
        });

        ed_bookingTime.setOnClickListener(v -> {
            timePicUp(ed_bookingTime);
        });

        btn_bookNow.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(BookGarageActivity.this, "", "Processing", true);

            name = ed_name.getText().toString().trim();
            phoneNumber = ed_phoneNumber.getText().toString().trim();
            persons = ed_persons.getText().toString().trim();
            message = ed_message.getText().toString().trim();
            bookingDate = ed_bookingDate.getText().toString().trim();
            bookingTime = ed_bookingTime.getText().toString().trim();

            if (isValid(name,phoneNumber,persons,bookingDate,bookingTime))
            {

                orderDate = getCurrentdate();
                orderTime = getTimeWithAmPm();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders")
                        .child(userUID);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String key = reference.push().getKey();
                        BookModel model = new BookModel(name,phoneNumber,persons,message,bookingDate
                                ,bookingTime,key,userUID,orderDate,orderTime,bookRequest,marqueeName,marqueeAddress,marqueeContact);
                        assert key != null;
                        reference.child(key).setValue(model);

                        saveAdminOrders(key);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

    }

    private void saveAdminOrders(String key)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ordersAdmin");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                BookModel model = new BookModel(name,phoneNumber,persons,message,bookingDate
                        ,bookingTime,key,userUID,orderDate,orderTime,bookRequest,marqueeName,
                        marqueeAddress,marqueeContact);
                assert key != null;
                reference.child(key).setValue(model);
                progressDialog.dismiss();
                orderDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void orderDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.order_place_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_viewBook = layout_dialog.findViewById(R.id.btn_viewBook);
        Button btn_backMenu = layout_dialog.findViewById(R.id.btn_backMenu);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        btn_viewBook.setOnClickListener(v -> {
            startActivity(new Intent(BookGarageActivity.this,BookingActivity.class));
            finish();
            dialog.dismiss();
        });

        btn_backMenu.setOnClickListener(v -> {
            startActivity(new Intent(BookGarageActivity.this, MainActivity.class));
            finish();
            dialog.dismiss();
        });
    }


    private void timePicUp(TextView ed_bookingTime)
    {
        final Calendar calendar=Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh-mm-a");
                ed_bookingTime.setText(simpleDateFormat.format(calendar.getTime()));
                bookingTime = ed_bookingTime.getText().toString();
            }
        };
        new TimePickerDialog(BookGarageActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }

    private void datepickup(TextView ed_bookingDate)
    {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MMM/yyyy");
                ed_bookingDate.setText(simpleDateFormat.format(calendar.getTime()));
                bookingDate = ed_bookingDate.getText().toString();
            }
        };
        new DatePickerDialog(BookGarageActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean isValid(String name, String phoneNumber,
                            String persons, String bookingDate, String bookingTime)
    {
        if (name.isEmpty())
        {
            ed_name.setError("field required");
            ed_name.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (phoneNumber.isEmpty())
        {
            ed_phoneNumber.setError("field required");
            ed_phoneNumber.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (persons.isEmpty())
        {
            ed_persons.setError("field required");
            ed_persons.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (bookingDate.isEmpty())
        {
            ed_bookingDate.setError("field required");
            ed_bookingDate.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (bookingTime.isEmpty())
        {
            ed_bookingTime.setError("field required");
            ed_bookingTime.requestFocus();
            progressDialog.dismiss();
            return false;
        }
        return true;
    }

    private void initViews()
    {
        ed_name = findViewById(R.id.ed_name);
        ed_phoneNumber = findViewById(R.id.ed_phoneNumber);
        ed_persons = findViewById(R.id.ed_persons);
        ed_message = findViewById(R.id.ed_message);
        ed_bookingDate = findViewById(R.id.ed_bookingDate);
        ed_bookingTime = findViewById(R.id.ed_bookingTime);
        btn_bookNow = findViewById(R.id.btn_bookNow);
        title_text = findViewById(R.id.title_text);
        back_Image = findViewById(R.id.back_Image);
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