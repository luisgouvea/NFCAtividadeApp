package com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService;


import com.fiquedeolho.nfcatividadeapp.models.Atividade;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AtividadeRetrofit {
    @POST("/api/Atividade/getAtivExecutar")
    Call<ArrayList<Atividade>> getAtividadesExecutar(@Body String idUsuario);

    @POST("/api/Atividade/getAtivAdicionadas")
    Call<ArrayList<Atividade>> getAtividadesAdicionadas(@Body String idUsuario);

    @POST("/api/Atividade/criarAtividade")
    Call<ArrayList<Atividade>> criarAtividade(@Body String idUsuario);
}
