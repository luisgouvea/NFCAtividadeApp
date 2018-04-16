package com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService;

import com.fiquedeolho.nfcatividadeapp.models.AtividadeTarefaCheck;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AtividadeTarefaCheckRetrofit {

    @POST("/api/AtividadeTarefaCheckRetrofit/getRegistroCheckNFC")
    Call<ArrayList<AtividadeTarefaCheck>> getRegistroCheckNFC(@Body int idAtividade);

}
