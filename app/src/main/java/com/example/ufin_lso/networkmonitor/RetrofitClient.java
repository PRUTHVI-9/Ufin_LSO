package com.example.ufin_lso.networkmonitor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    public static NetworkApiService getApiService() {

        if (retrofit == null) {

            HttpLoggingInterceptor logging =
                    new HttpLoggingInterceptor(message ->
                            android.util.Log.d(
                                    "API_HTTP",
                                    message
                            )
                    );

            // Request + Response body दाखवण्यासाठी
            logging.setLevel(
                    HttpLoggingInterceptor.Level.BODY
            );

            OkHttpClient client =
                    new OkHttpClient.Builder()
                            .addInterceptor(logging)
                            .build();

            retrofit =
                    new Retrofit.Builder()
                            .baseUrl("http://123.201.117.218:105/")
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit.create(
                NetworkApiService.class
        );
    }
}