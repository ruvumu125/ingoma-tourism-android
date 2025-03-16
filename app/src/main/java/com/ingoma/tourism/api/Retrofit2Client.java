package com.ingoma.tourism.api;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.ingoma.tourism.constant.Constant.BASE_URL;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Retrofit2Client {

    private Context context;

    // Configure OkHttpClient with the interceptor and custom timeout values
    private OkHttpClient okHttpClient;

    // Create Retrofit builder with the configured OkHttpClient
    private Retrofit.Builder builder;

    private Retrofit retrofit;

    // Constructor to set the context and initialize OkHttpClient and Retrofit
    public Retrofit2Client(Context ctx) {
        this.context = ctx;

        // Initialize OkHttpClient
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)  // 30 seconds connection timeout
                .readTimeout(30, TimeUnit.SECONDS)     // 30 seconds read timeout
                .writeTimeout(30, TimeUnit.SECONDS)       // Add the auth interceptor
                .cache(null)
                .build();

        // Initialize Retrofit builder
        this.builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        this.retrofit = builder.build();
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
