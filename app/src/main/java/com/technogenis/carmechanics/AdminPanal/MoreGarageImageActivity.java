package com.technogenis.carmechanics.AdminPanal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.technogenis.carmechanics.Adapter.MultiImageAdapter;
import com.technogenis.carmechanics.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MoreGarageImageActivity extends AppCompatActivity {

    ImageView back_image;
    RecyclerView image_recyclerView;
    ArrayList<Uri> uriImageList = new ArrayList<>();
    MultiImageAdapter adapter;
    TextView save_text,photoCount_text;
    ImageView open_image;
    private static final int READ_PERMISSION = 101;

    String garageAddKey;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference storageReference;
    private int UPLOAD_COUNT = 0;

    FirebaseAuth mAuth;
    String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_garage_image);

        mAuth = FirebaseAuth.getInstance();
        userUID  = mAuth.getUid();

        initView();
        getIntentData();



        save_text.setVisibility(View.INVISIBLE);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(userUID);

        back_image.setOnClickListener(v -> {
           getOnBackPressedDispatcher().onBackPressed();
        });

        adapter = new MultiImageAdapter(this,uriImageList);
        image_recyclerView.setLayoutManager(new GridLayoutManager(MoreGarageImageActivity.this,4));
        image_recyclerView.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(MoreGarageImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MoreGarageImageActivity.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },READ_PERMISSION);
        }

        open_image.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"select Picture"),1);

            save_text.setVisibility(View.VISIBLE);

        });

        save_text.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(MoreGarageImageActivity.this, "Please wait", "Processing", true);
            saveDataToFirebaseStorage();

        });
    }


    private void saveDataToFirebaseStorage()
    {
        StorageReference storage = storageReference.child("garageImages");

        for (UPLOAD_COUNT = 0; UPLOAD_COUNT < uriImageList.size(); UPLOAD_COUNT++)
        {
            Uri imageUri = uriImageList.get(UPLOAD_COUNT);
            StorageReference store = storage.child("garageImages"+imageUri.getLastPathSegment());

            store.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    store.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url =  uri.toString();
                            storeLink(url);
                        }
                    });
                }
            });
        }
    }

    private void storeLink(String url)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Garage");
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("garageImages",url);
        if (garageAddKey!=null)
        {
            reference.child(garageAddKey).child("moreImages").push().setValue(hashMap);
            Toast.makeText(this, "Images saved", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MoreGarageImageActivity.this,GarageLocationActivity.class);
            intent.putExtra("garageAddKey",garageAddKey);
            startActivity(intent);
            finish();
        }else
        {
            Toast.makeText(this, "Data error", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    private void getIntentData()
    {
        garageAddKey = getIntent().getStringExtra("garageAddKey");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            if (data.getClipData() != null)
            {
                int x = data.getClipData().getItemCount();

                for (int i=0; i<x;i++)
                {
                    uriImageList.add(data.getClipData().getItemAt(i).getUri());
                }
                adapter.notifyDataSetChanged();
                photoCount_text.setText("Photo (" + uriImageList.size()+")");
            }
            else if (data.getClipData() != null)
            {
                String imageURL = data.getData().getPath();
                uriImageList.add(Uri.parse(imageURL));
            }
        }
    }


    private void initView()
    {
        image_recyclerView = findViewById(R.id.image_recyclerView);
        save_text = findViewById(R.id.save_text);
        photoCount_text = findViewById(R.id.photoCount_text);
        open_image = findViewById(R.id.open_image);
        back_image = findViewById(R.id.back_image);
    }
}