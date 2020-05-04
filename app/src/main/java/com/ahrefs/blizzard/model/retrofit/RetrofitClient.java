package com.ahrefs.blizzard.model.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*A class for getting an instance of Retrofit*/
public class RetrofitClient {
    private static Retrofit mInstance;
    private static final String BASE_URL = "https://api.darksky.net/forecast/2da2170fb61faad4a2270216f2c21b40/";

    public static Retrofit getInstance(){
        if (mInstance == null){
            mInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mInstance;
    }
}
