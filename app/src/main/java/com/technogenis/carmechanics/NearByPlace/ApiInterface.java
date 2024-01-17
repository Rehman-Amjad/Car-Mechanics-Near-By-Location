package com.technogenis.carmechanics.NearByPlace;



import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface ApiInterface {

    //todo::for Foursquare
    @GET("nearby")
    Call<JsonObject> fetchNearbyPlaces(
            @Header("Authorization") String AuthKey,
            @Query("ll") String LatiLong,
            @Query("query") String PlaceName,
            @Query("limit") int limit
    );


}
