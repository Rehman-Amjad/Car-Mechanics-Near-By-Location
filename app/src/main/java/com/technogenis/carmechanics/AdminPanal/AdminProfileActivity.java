package com.technogenis.carmechanics.AdminPanal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.technogenis.carmechanics.Model.AdminProfileModel;
import com.technogenis.carmechanics.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfileActivity extends AppCompatActivity {

    ImageView back_Image,cover_image;
    CircleImageView profile_image;
    EditText ed_fullName,ed_bio,ed_experience;
    Button btn_save;

    String fullName = "",bio = "",experience = "",coverImageLink,profileImageLink,userUID;
    Uri coverImageUri,profileImageUri;
    Bitmap coverImageBitmap,profileImageBitmap;

    FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;

    String storeCoverLink;
    String storeProfileLink;

    boolean coverCheck = false;
    boolean profileCheck = false;
    boolean bothCheck = false;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        initViews();


        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(userUID);

        getsavedValue();

        back_Image.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        cover_image.setOnClickListener(v -> {

            coverCheck = false;

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 16);
        });

        profile_image.setOnClickListener(v -> {

            profileCheck = false;

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 17);
        });

        btn_save.setOnClickListener(v -> {

            progressDialog = ProgressDialog.show(AdminProfileActivity.this, "", "Processing", true);
            fullName = ed_fullName.getText().toString().trim();
            bio = ed_bio.getText().toString().trim();
            experience = ed_experience.getText().toString().trim();

            saveLinkToStorage(fullName,bio,experience);
        });
    }

    private void getsavedValue()
    {
        progressDialog = ProgressDialog.show(AdminProfileActivity.this, "", "Processing", true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GarageOwnerAccounts")
                .child(userUID).child("profile");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    ed_fullName.setText(snapshot.child("fullName").getValue(String.class));
                    ed_bio.setText(snapshot.child("bio").getValue(String.class));
                    ed_experience.setText(snapshot.child("experience").getValue(String.class));
                    coverImageLink = snapshot.child("coverImageLink").getValue(String.class);
                    profileImageLink = snapshot.child("profileImageLink").getValue(String.class);

                    if (coverImageLink.equals(" "))
                    {
                        progressDialog.dismiss();
                        cover_image.setImageResource(R.drawable.cover_image);
                    }else
                    {
                        Glide.with(AdminProfileActivity.this)
                                .load(coverImageLink).into(cover_image);
                        progressDialog.dismiss();
                    }
                    if (profileImageLink.equals(""))
                    {
                        progressDialog.dismiss();
                        profile_image.setImageResource(R.drawable.profile_image);
                    }
                    else
                    {
                        Glide.with(AdminProfileActivity.this)
                                .load(profileImageLink).into(profile_image);
                        progressDialog.dismiss();
                    }



                }else{
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveLinkToStorage(String fullName, String bio, String experience)
    {
        if (profileImageUri!=null && coverImageUri!=null)
        {
            bothCheck = true;
            profileImageStore();
        } else if (profileImageUri!=null)
        {
            profileCheck = true;
            profileImageStore();
        }
        else if (coverImageUri!=null)
        {
            coverCheck  = true;
            coverImageStore();
        }
        else
        {
            coverImageLink = "";
            profileImageLink = "";
            saveDataToFirebase(fullName,bio,experience,coverImageLink,profileImageLink);
        }


    }

    private void profileImageStore()
    {
        StorageReference storage = storageReference.child("profileImage");

        StorageReference store = storage.child("ProfileImage"+profileImageUri.getLastPathSegment());

        store.putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                store.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        storeProfileLink =  uri.toString();
                        if (bothCheck == true)
                        {
                            coverImageStore();
                        }else
                        {
                            saveDataToFirebase(fullName,bio,experience,storeProfileLink,storeCoverLink);
                        }


                    }
                });
            }
        });
    }

    private void coverImageStore()
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
                        saveDataToFirebase(fullName,bio,experience,storeProfileLink,storeCoverLink);
                    }
                });
            }
        });
    }

    private void saveDataToFirebase(String fullName, String bio, String experience,
                                    String profileImageLink, String coverImageLink)
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GarageOwnerAccounts")
                .child(userUID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    AdminProfileModel model = new AdminProfileModel(fullName,bio,experience,coverImageLink
                            ,profileImageLink);

                    reference.child("profile").setValue(model);
                    Toast.makeText(AdminProfileActivity.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else
                {
                    progressDialog.dismiss();
                    Toast.makeText(AdminProfileActivity.this, "Account Doesn't exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initViews()
    {
        back_Image = findViewById(R.id.back_Image);
        cover_image = findViewById(R.id.cover_image);
        profile_image = findViewById(R.id.profile_image);
        ed_fullName = findViewById(R.id.ed_fullName);
        ed_bio = findViewById(R.id.ed_bio);
        ed_experience = findViewById(R.id.ed_experience);
        btn_save = findViewById(R.id.btn_save);
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
                cover_image.setImageBitmap(coverImageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 17 && resultCode == RESULT_OK && data != null) {
            profileImageUri = data.getData();
            try {
                profileImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profileImageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                profileImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                profileImageLink = Base64.encodeToString(bytes, Base64.DEFAULT);
                profile_image.setImageBitmap(profileImageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}