package com.example.b3tempoapp2425;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String LOG_TAG=ApiClient.class.getSimpleName();
    private static final String EDFAPI_BASE_URL = "https://api-commerce.edf.fr";

    private ApiClient() { // make sure the class can't be instanciated

    }

    private static Retrofit retrofit = null;

    static Retrofit get() {
        try {
            // Create a HTTP Header interceptor
            Interceptor headerInterceptor = new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json, text/plain, */*")
                            .header("Origin","https://particulier.edf.fr")
                            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.5 Safari/605.1.15")
                            .header("Referer","https://particulier.edf.fr/")
                            .header("Situation-Usage","saison")
                            .header("X-Request-Id", String.valueOf(System.currentTimeMillis()))
                            .header("Application-Origine-Controlee", "site_RC")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            };

            // set BASIC HTTP trace level
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(loggingInterceptor) // put logging interceptor always at the end
                    .build();


            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(EDFAPI_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
            }
        } catch (IllegalArgumentException | IllegalStateException  e) {
            Log.e(LOG_TAG, "Retrofit configuration error: " + e.getMessage());
        }
        return retrofit;
    }
}
