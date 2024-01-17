package com.technogenis.carmechanics.NearByPlace;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    static ApiInterface INSTANCE_FOUR_SQUIRE = null;

    //todo::For FourSquire
    public static ApiInterface getInstanceFourSquire() {
        if (INSTANCE_FOUR_SQUIRE == null) {
            INSTANCE_FOUR_SQUIRE = ApiClient.getClientFourSquire().create(ApiInterface.class);
        }
        return INSTANCE_FOUR_SQUIRE;
    }

    private static Retrofit getClientFourSquire() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(CONSTANTS.BASE_URL_FOUR_SQUIRE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

}
