package com.fiquedeolho.nfcatividadeapp.retrofit.interfaces;


import com.fiquedeolho.nfcatividadeapp.models.Atividade;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface AtividadeRetrofit {
    @POST("/api/Atividade/getAtivExecutar")
    Call<ArrayList<Atividade>> getAtividadesExecutar(@Body int idUsuario);

    @POST("/api/Atividade/getAtivAdicionadas")
    Call<ArrayList<Atividade>> getAtividadesAdicionadas(@Body int idUsuario);

    @POST("/api/Atividade/criarAtividade")
    Call<Boolean> criarAtividade(@Body Atividade ativ);
}
