package com.technogenis.carmechanics.NearByPlace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.technogenis.carmechanics.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducationActivity extends AppCompatActivity {

    public static final String TAG = EducationActivity.class.getSimpleName();

    PlacesAdapter placesAdapter;
    List<Result> resultList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;

    String name, latitude, longitude, distance, address;
    String real_latitude, real_longitude;
    SharedPref yourPreference;

    String intentLatitude,intentLongitude;

    private int radius = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        Intent intent  =getIntent();

        intentLatitude = intent.getStringExtra("latitude");
        intentLongitude = intent.getStringExtra("longitude");

//        Toast.makeText(this, intentLatitude, Toast.LENGTH_SHORT).show();

        yourPreference = SharedPref.getInstance(EducationActivity.this);

        recyclerView = findViewById(R.id.education_recyclerview);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        real_latitude = yourPreference.getData(CONSTANTS.KEY_LATITUDE);
        real_longitude = yourPreference.getData(CONSTANTS.KEY_LONGITUDE);

        getPlaces(intentLatitude,intentLongitude);
    }

    public void getPlaces(String lati, String longi) {

        ApiInterface apiInterface = ApiClient.getInstanceFourSquire();
        Call<JsonObject> call = apiInterface
                .fetchNearbyPlaces(CONSTANTS.Authorization, lati + "," + longi, "garage", 50);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if (!response.isSuccessful() && response.body() == null) {
                    try {
                        Log.e(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                JsonArray jsonArray = response.body().getAsJsonObject().getAsJsonArray("results");

                for (int i = 0; i < jsonArray.size(); i++) {
                    try {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                        JsonObject geoCodes = jsonObject.getAsJsonObject("geocodes").getAsJsonObject("main");
                        JsonObject locationObj = jsonObject.getAsJsonObject("location");
                        //todo::If any field is null
                        try {
                            name = jsonObject.get("name").toString();
                            latitude = geoCodes.get("latitude").toString();
                            longitude = geoCodes.get("longitude").toString();
                            distance = jsonObject.get("distance").toString();
                            address = locationObj.get("address").toString();

                            if (address != null) {
                                resultList.add(new Result(name, latitude, longitude, distance, address));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "Json Value: " + jsonObject.get("fsq_id"));
                        Log.d(TAG, "Json Value: " + jsonObject.get("distance"));
                        Log.d(TAG, "Json Value: " + jsonObject.get("name"));
                        Log.d(TAG, "Json Value: " + locationObj.get("address"));
                        Log.d(TAG, "Json Value: " + locationObj.get("locality"));
                        Log.d(TAG, "Json Value: " + geoCodes.get("latitude"));
                        Log.d(TAG, "Json Value: " + geoCodes.get("longitude"));
                        Log.d(TAG, "========================================");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                placesAdapter = new PlacesAdapter(resultList, EducationActivity.this);
                recyclerView.setAdapter(placesAdapter);

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}