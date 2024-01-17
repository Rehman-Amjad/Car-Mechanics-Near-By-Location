package com.technogenis.carmechanics.AdminPanal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technogenis.carmechanics.Model.ServicesModel;
import com.technogenis.carmechanics.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddNewServicesActivity extends AppCompatActivity {

    ImageView back_Image;
    TextView serviceCategory_text;
    EditText servicesName_ed,servicesDes_ed,Charges_ed;
    Button btn_save;

    String serviceCategory,serviceProviderName,serviceCharges,serviceDetails,serviceDate,serviceTime;

    FirebaseAuth mAuth;
    String userUID,garageAddKey,garageOwnerName;
    ProgressDialog progressDialog;
    String screenType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_services);

        mAuth = FirebaseAuth.getInstance();
        userUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        initViews();

        back_Image.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        Intent intentValue = getIntent();
        screenType = intentValue.getStringExtra("screenType");
        if (screenType.equals("Activity"))
        {
            btn_save.setText("Save");
            garageAddKey = intentValue.getStringExtra("garageAddKey");
            garageOwnerName = intentValue.getStringExtra("garageName");
        }
        if (screenType.equals("Adapter"))
        {
            btn_save.setText("Update");
            garageAddKey = intentValue.getStringExtra("garageAddKey");
            garageOwnerName = intentValue.getStringExtra("garageName");
            serviceCategory = intentValue.getStringExtra("serviceCategory");
            serviceProviderName = intentValue.getStringExtra("serviceProviderName");
            serviceCharges = intentValue.getStringExtra("serviceCharges");
            serviceDetails = intentValue.getStringExtra("serviceDetails");

            serviceCategory_text.setText(serviceCategory);
            servicesName_ed.setText(serviceProviderName);
            Charges_ed.setText(serviceCharges);
            servicesDes_ed.setText(serviceDetails);
        }




        serviceCategory_text.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoryListActivity.class);
            startActivityForResult(intent, 1);
        });

        btn_save.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(AddNewServicesActivity.this, "", "Processing", true);
            serviceDate = getCurrentdate();
            serviceTime = getTimeWithAmPm();

            serviceCategory  = serviceCategory_text.getText().toString().trim();
            serviceProviderName  = servicesName_ed.getText().toString().trim();
            serviceCharges  = Charges_ed.getText().toString().trim();
            serviceDetails  = servicesDes_ed.getText().toString().trim();

            if (isValid(serviceCategory,serviceProviderName,serviceCharges,serviceDetails))
            {
                if (screenType.equals("Activity"))
                {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Services");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String key = reference.push().getKey();
                            ServicesModel model = new ServicesModel(serviceCategory,serviceProviderName
                                    ,serviceCharges,serviceDetails,key,userUID,serviceDate,serviceTime,garageAddKey
                                    ,garageOwnerName);
                            assert key != null;
                            reference.child(key).setValue(model);
                            addServiceDialog();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if (screenType.equals("Adapter"))
                {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Services")
                            .child(garageAddKey);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ServicesModel model = new ServicesModel(serviceCategory,serviceProviderName
                                    ,serviceCharges,serviceDetails,garageAddKey,userUID,serviceDate,serviceTime,garageAddKey
                                    ,garageOwnerName);
                            reference.child(garageAddKey).setValue(model);
                            progressDialog.dismiss();
                            Toast.makeText(AddNewServicesActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
    }

    private void addServiceDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.account_check_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_back = layout_dialog.findViewById(R.id.btn_back);
        Button btn_logout = layout_dialog.findViewById(R.id.btn_logout);
        TextView male_text = layout_dialog.findViewById(R.id.male_text);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        male_text.setText("Serviced Add");
        btn_back.setText("back to menu");
        btn_logout.setText("Add More");

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent( AddNewServicesActivity.this, AdminMainActivity.class));
            finish();
        });

        btn_logout.setOnClickListener(v -> {
            serviceCategory_text.setText("");
            servicesDes_ed.setText("");
            Charges_ed.setText("");
            servicesName_ed.setText("");
            dialog.dismiss();
        });
    }

    private boolean isValid(String serviceCategory, String serviceProviderName,
                            String serviceCharges, String serviceDetails)
    {
        if (serviceCategory.isEmpty())
        {
            Toast.makeText(this, "Choose services category", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

        if (serviceProviderName.isEmpty())
        {
            servicesName_ed.setError("field required");
            servicesName_ed.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (serviceCharges.isEmpty())
        {
            Charges_ed.setError("field required");
            Charges_ed.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (serviceDetails.isEmpty())
        {
            servicesDes_ed.setError("field required");
            servicesDes_ed.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        return true;
    }

    private void initViews()
    {
        back_Image = findViewById(R.id.back_Image);
        serviceCategory_text = findViewById(R.id.serviceCategory_text);
        servicesName_ed = findViewById(R.id.servicesName_ed);
        servicesDes_ed = findViewById(R.id.servicesDes_ed);
        Charges_ed = findViewById(R.id.Charges_ed);
        btn_save = findViewById(R.id.btn_save);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                serviceCategory = data.getStringExtra("categoryListIntent");
                serviceCategory_text.setText(serviceCategory);
            }
        }
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