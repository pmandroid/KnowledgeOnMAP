package com.vfig.konm.knowledgeonmap.network;

import android.content.Context;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WebServiceHandler {


    public static String callApi(Context context, String url, HashMap<String,String> paramsHashMap){

        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newBuilder().connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
        .build();

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }

    }

