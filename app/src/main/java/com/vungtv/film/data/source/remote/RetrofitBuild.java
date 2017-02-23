package com.vungtv.film.data.source.remote;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitBuild {
    private static final String TAG = RetrofitBuild.class.getSimpleName();
    private static final String DEFAULT_API_URL = "http://api.vungtv.com/api/v1/";

    public static Retrofit build() {
        return new Retrofit.Builder()
                .baseUrl(DEFAULT_API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        return chain.proceed(ongoing.build());
                    }
                })
                .build();
    }
}
