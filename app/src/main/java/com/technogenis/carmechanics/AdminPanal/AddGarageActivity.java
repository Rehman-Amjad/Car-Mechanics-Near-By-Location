package com.technogenis.carmechanics.AdminPanal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.technogenis.carmechanics.Model.GarageModel;
import com.technogenis.carmechanics.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddGarageActivity extends AppCompatActivity {

    ImageView back_Image,cover_Image;
    EditText ed_garageName,ed_garageOwnerName,ed_garageContact,ed_garageBio,ed_garageAddress;
    Button btn_next;

    String garageName,garageOwnerName,garageContact,garageBio,garageKey,coverImageLink
            ,currentTime,currentDate,garageAddress;
    Uri coverImageUri;
    Bitmap coverImageBitmap;

    String userUID,storeCoverLink;

    FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    String key;

    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_garage);

        initViews();

        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(userUID);

        back_Image = findViewById(R.id.back_image);
        back_Image.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        cover_Image.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 16);
        });

        btn_next.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(AddGarageActivity.this, "", "Processing", true);
            garageName = ed_garageName.getText().toString().trim();
            garageOwnerName = ed_garageOwnerName.getText().toString().trim();
            garageContact = ed_garageContact.getText().toString().trim();
            garageBio = ed_garageBio.getText().toString().trim();
            garageAddress = ed_garageAddress.getText().toString().trim();

            if (isValid(garageName,garageOwnerName,garageContact,garageBio,garageAddress))
            {
                if (coverImageLink!=null)
                {
                    saveImageToFirebaseStorage();
                }else
                {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Cover Image required", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void saveImageToFirebaseStorage()
    {
        StorageReference storage = storageReference.child("profileImage");

        StorageReference store = storage.child("coverImage"+coverImageUri.getLastPathSegment());

        store.putFile(coverImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                store.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        storeCoverLink =  uri.toString();
                        saveMarqueeData(garageName,garageOwnerName,garageContact,garageBio,storeCoverLink);

                    }
                });
            }
        });
    }

    private void saveMarqueeData(String garageName, String garageOwnerName,
                                 String garageContact, String garageBio,String coverImageLink)
    {

        currentTime = getTimeWithAmPm();
        currentDate = getCurrentdate();

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Garage");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                key = reference.push().getKey();
                GarageModel model = new GarageModel(garageName,garageOwnerName,garageContact
                        ,garageBio,userUID,key,currentTime,currentDate,coverImageLink,garageAddress);
                assert key != null;
                reference.child(key).setValue(model);
                Toast.makeText(AddGarageActivity.this, "Garage Added", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                marqueeDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void marqueeDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.garage_more_image_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_skip = layout_dialog.findViewById(R.id.btn_skip);
        Button btn_add = layout_dialog.findViewById(R.id.btn_add);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        btn_skip.setOnClickListener(v -> {
            Intent intent = new Intent(AddGarageActivity.this,GarageLocationActivity.class);
            intent.putExtra("garageAddKey",key);
            startActivity(intent);
            dialog.dismiss();
        });

        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(AddGarageActivity.this,MoreGarageImageActivity.class);
            intent.putExtra("garageAddKey",key);
            startActivity(intent);
        });


    }

    private boolean isValid(String garageName, String garageOwnerName,
                            String garageContact, String garageBio, String garageAddress)
    {
        if (garageName.isEmpty())
        {
            ed_garageName.setError("field required");
            ed_garageName.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (garageOwnerName.isEmpty())
        {
            ed_garageOwnerName.setError("field required");
            ed_garageOwnerName.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (garageContact.isEmpty() || garageContact.length() <10)
        {
            ed_garageContact.setError("field required");
            ed_garageContact.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (garageBio.isEmpty())
        {
            ed_garageBio.setError("field required");
            ed_garageBio.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (garageAddress.isEmpty())
        {
            ed_garageAddress.setError("field required");
            ed_garageAddress.requestFocus();
            progressDialog.dismiss();
            return false;
        }
        return true;
    }

    private void initViews()
    {
        back_Image = findViewById(R.id.back_Image);
        cover_Image = findViewById(R.id.cover_Image);
        ed_garageName = findViewById(R.id.ed_garageName);
        ed_garageOwnerName = findViewById(R.id.ed_garageOwnerName);
        ed_garageContact = findViewById(R.id.ed_garageContact);
        ed_garageBio = findViewById(R.id.ed_garageBio);
        btn_next = findViewById(R.id.btn_next);
        ed_garageAddress = findViewById(R.id.ed_garageAddress);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 16 && resultCode == RESULT_OK && data != null) {
            coverImageUri = data.getData();
            try {
                coverImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), coverImageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                coverImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                coverImageLink = Base64.encodeToString(bytes, Base64.DEFAULT);
                cover_Image.setImageBitmap(coverImageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
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