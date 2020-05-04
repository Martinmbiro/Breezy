package com.ahrefs.blizzard.model.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BreezyAPI {
    @GET("1.1244,37.0045?units=si&exclude=hourly,minutely,daily,flags,alerts")
    Call<BreezyResponse> getResponse();
}
