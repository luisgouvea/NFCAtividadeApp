package com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService;

import com.fiquedeolho.nfcatividadeapp.models.TarefaCheck;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface TarefaCheckRetrofit {

    @POST("/api/TarefaCheck/getAllRegistroCheckNFC")
    Call<ArrayList<TarefaCheck>> getAllRegistroCheckNFC(@Body int idAtividade);

    @GET("/api/TarefaCheck/realizarCheck")
    Call<Boolean> realizarCheck(@Query("idTagCheck") int idTagCheck, @Query("idTarefa") int idTarefa);

}
