package com.fiquedeolho.nfcatividadeapp.retrofit.interfaces;

import com.fiquedeolho.nfcatividadeapp.util.ConstantsURIAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface BaseUrlRetrofit {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ConstantsURIAPI.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
