package com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService;


import com.fiquedeolho.nfcatividadeapp.models.Atividade;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AtividadeRetrofit {
    @POST("/api/Atividade/getAtivExecutar")
    Call<ArrayList<Atividade>> getAtividadesExecutar(@Body int idUsuario);

    @POST("/api/Atividade/getAtivAdicionadas")
    Call<ArrayList<Atividade>> getAtividadesAdicionadas(@Body int idUsuario);

    @POST("/api/Atividade/criarAtividade")
    Call<Boolean> criarAtividade(@Body Atividade ativ);

    @POST("/api/Atividade/realizarCheck")
    Call<Boolean> realizarCheck(@Body int idTag);
}
