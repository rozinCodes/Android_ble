package com.waltonbd.smartscale.api;

import com.waltonbd.smartscale.constant.ConstantValues;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static final String BASE_URL = ConstantValues.BASE_URL;

    private static APIRequests apiRequests;

    public static APIRequests getInstance() {
        if (apiRequests == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            apiRequests = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(APIRequests.class);
        }
        return apiRequests;
    }
}
