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

import com.ingoma.tourism.utils.LoginPreferencesManager;

public class Retrofit2Client {

    private Context context;

    // Create an interceptor to add the Authorization header dynamically
    private Interceptor authInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            // Retrieve the token from LoginPreferencesManager
            LoginPreferencesManager loginPreferencesManager = new LoginPreferencesManager(context);
            String token = loginPreferencesManager.getToken(); // Fetch the token dynamically

            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder();

            // Add the token to the Authorization header if it's available
            if (token != null && !token.isEmpty()) {
                builder.header("Authorization", "Bearer " + token);
            }

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    };

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
                .writeTimeout(30, TimeUnit.SECONDS)    // 30 seconds write timeout
                .addInterceptor(authInterceptor)       // Add the auth interceptor
                .cache(null)
                .build();

        // Initialize Retrofit builder
        this.builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)                  // Add the OkHttpClient with interceptor
                .addConverterFactory(GsonConverterFactory.create());

        this.retrofit = builder.build();
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}