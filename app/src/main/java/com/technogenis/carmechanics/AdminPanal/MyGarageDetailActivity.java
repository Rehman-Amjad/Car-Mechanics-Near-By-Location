package com.technogenis.carmechanics.AdminPanal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.technogenis.carmechanics.Adapter.ImageAdapter;
import com.technogenis.carmechanics.Model.GarageModel;
import com.technogenis.carmechanics.Model.ImageModel;
import com.technogenis.carmechanics.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyGarageDetailActivity extends AppCompatActivity {

    EditText ed_marqueName,ed_marqueOwnerName,ed_marqueContact,ed_marqueBio,ed_marqueAddress;
    ImageView back_Image;
    Button btn_update,btn_delete;
    RecyclerView imageSlider_recycleView;
    ArrayList<ImageModel> mDataList;
    ImageAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    String garageName,garageOwnerName,garageContactNumber,garageBio,garageAddKey,garageCoverLink
            ,currentTime,currentDate,garageAddress,ownerUserUID,screenType = "Activity";

    ProgressDialog progressDialog;

    TextView title_text;
    Button btn_addServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garage_detail);

        progressDialog = ProgressDialog.show(MyGarageDetailActivity.this, "", "Processing", true);

        initViews();
        getIntentValues();
        checkUpdateValue();

        back_Image.setOnClickListener(v -> {
           getOnBackPressedDispatcher().onBackPressed();
        });

        btn_delete.setOnClickListener(v -> {
            showDeletealertDialog();
        });

        btn_addServices.setOnClickListener(v -> {
            Intent intent = new Intent(MyGarageDetailActivity.this,AddNewServicesActivity.class);
            intent.putExtra("garageAddKey",garageAddKey);
            intent.putExtra("garageName",garageName);
            intent.putExtra("screenType",screenType);
            startActivity(intent);
        });

        btn_update.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(MyGarageDetailActivity.this, "", "Processing", true);
            garageName = ed_marqueName.getText().toString().trim();
            garageOwnerName = ed_marqueOwnerName.getText().toString().trim();
            garageContactNumber = ed_marqueContact.getText().toString().trim();
            garageBio = ed_marqueBio.getText().toString().trim();
            garageAddress = ed_marqueAddress.getText().toString().trim();

            if (isValid(garageName,garageOwnerName,garageContactNumber,garageBio,garageAddress))
            {
                updateMarqueeDetails();
            }

        });


    }

    private void showDeletealertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.garage_delete_dialog, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_back = layout_dialog.findViewById(R.id.btn_back);
        Button btn_new = layout_dialog.findViewById(R.id.btn_new);
        TextView male_text = layout_dialog.findViewById(R.id.male_text);


        male_text.setText("are your sure!");
        btn_back.setText("No");
        btn_new.setText("Yes");

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        btn_back.setOnClickListener(v -> {
            dialog.dismiss();

        });

        btn_new.setOnClickListener(v -> {
            deleteValues();
            dialog.dismiss();
        });
    }

    private void deleteValues()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Garage")
                .child(garageAddKey);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    snapshot.getRef().removeValue();
                    showRemoveDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUpdateValue()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Garage")
                .child(garageAddKey).child("");
    }

    private boolean isValid(String marqueeName, String marqueeOwnerName,
                            String marqueeContactNumber, String marqueeBio, String marqueeAddress)
    {
        if (marqueeName.isEmpty())
        {
            ed_marqueName.setError("field required");
            ed_marqueName.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (marqueeOwnerName.isEmpty())
        {
            ed_marqueOwnerName.setError("field required");
            ed_marqueOwnerName.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (marqueeContactNumber.isEmpty() || marqueeContactNumber.length() <10)
        {
            ed_marqueContact.setError("field required");
            ed_marqueContact.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (marqueeBio.isEmpty())
        {
            ed_marqueBio.setError("field required");
            ed_marqueBio.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (marqueeAddress.isEmpty())
        {
            ed_marqueAddress.setError("field required");
            ed_marqueAddress.requestFocus();
            progressDialog.dismiss();
            return false;
        }
        return true;
    }

    private void updateMarqueeDetails()
    {
        currentTime = getTimeWithAmPm();
        currentDate = getCurrentdate();

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Garage")
                .child(garageAddKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GarageModel model = new GarageModel(garageName,garageOwnerName,garageContactNumber
                        ,garageBio,ownerUserUID,garageAddKey,currentTime,currentDate,garageCoverLink,garageAddress);

                reference.child("updateData").setValue(model);
                Toast.makeText(MyGarageDetailActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showRemoveDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.garage_delete_dialog, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_back = layout_dialog.findViewById(R.id.btn_back);
        Button btn_new = layout_dialog.findViewById(R.id.btn_new);


        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        btn_back.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(MyGarageDetailActivity.this,GarageListActivity.class));
            finish();
        });

        btn_new.setOnClickListener(v -> {
            startActivity(new Intent(MyGarageDetailActivity.this,AddGarageActivity.class));
            finish();
            dialog.dismiss();
        });


    }

    private void getIntentValues()
    {
        Intent intent = getIntent();
        garageName = intent.getStringExtra("garageName");
        garageOwnerName = intent.getStringExtra("garageOwnerName");
        garageContactNumber = intent.getStringExtra("garageContactNumber");
        garageBio = intent.getStringExtra("garageBio");
        ownerUserUID = intent.getStringExtra("ownerUserUID");
        garageAddKey = intent.getStringExtra("garageAddKey");
        currentTime = intent.getStringExtra("currentTime");
        currentDate = intent.getStringExtra("currentDate");
        garageCoverLink = intent.getStringExtra("garageCoverLink");
        garageAddress = intent.getStringExtra("garageAddress");

        title_text.setText(garageName);
        ed_marqueName.setText(garageName);
        ed_marqueOwnerName.setText(garageOwnerName);
        ed_marqueContact.setText(garageContactNumber);
        ed_marqueBio.setText(garageBio);
        ed_marqueAddress.setText(garageAddress);

        if (garageAddKey!=null)
        {
            getImageSlider(garageAddKey,ownerUserUID);
        }else
        {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }


    }

    private void getImageSlider(String garageAddKey, String ownerUserUID)
    {
        ImageModel model = new ImageModel();
        model.setGarageImages(garageCoverLink);

        mDataList = new ArrayList<>();
        mDataList.add(model);

        adapter = new ImageAdapter(this,mDataList);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        imageSlider_recycleView.setLayoutManager(linearLayoutManager);
        imageSlider_recycleView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Garage")
                .child(garageAddKey).child("moreImages");
        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    ImageModel model = snapshot.getValue(ImageModel.class);
                    mDataList.add(model);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else
                {
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
//
                }
                progressDialog.dismiss();

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
        progressDialog.dismiss();
    }

    private void initViews()
    {
        ed_marqueName = findViewById(R.id.ed_marqueName);
        ed_marqueOwnerName = findViewById(R.id.ed_marqueOwnerName);
        ed_marqueContact = findViewById(R.id.ed_marqueContact);
        ed_marqueBio = findViewById(R.id.ed_marqueBio);
        ed_marqueAddress = findViewById(R.id.ed_marqueAddress);
        back_Image = findViewById(R.id.back_Image);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        imageSlider_recycleView = findViewById(R.id.imageSlider_recycleView);
        title_text = findViewById(R.id.title_text);
        btn_addServices = findViewById(R.id.btn_addServices);
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