package com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService;

import com.fiquedeolho.nfcatividadeapp.util.ConstantsURIAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface BaseUrlRetrofit {



    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ConstantsURIAPI.BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
