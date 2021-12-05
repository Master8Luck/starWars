package com.example.starwars.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageAPI {
    @GET("w342/{path}")
    Call<ResponseBody> getImage(@Path("path") String path);
}
