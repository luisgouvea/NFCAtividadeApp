package com.fiquedeolho.nfcatividadeapp.retrofit.interfaces;

import com.fiquedeolho.nfcatividadeapp.models.TAG;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface TagRetrofit {

    @POST("/api/Tag/addTag")
    Call<Boolean> addTag(@Body TAG tag);

    @POST("/api/Tag/getTagsByIdUsuario")
    Call<ArrayList<TAG>> getTagsByIdUsuario(@Body int idUsuario);

    @POST("/api/Tag/getDetalhesTag")
    Call<TAG> getDetalhesTag(@Body int idTarefa);
}
