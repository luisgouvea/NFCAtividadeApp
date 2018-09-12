package com.fiquedeolho.nfcatividadeapp.retrofit.interfaces;


import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.models.DetalhesAtividade;
import com.fiquedeolho.nfcatividadeapp.models.FiltroPesquisaHome;

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

    @POST("/api/Atividade/getAtividadeById")
    Call<Atividade> getAtividadeById(@Body int idAtividade);

    @POST("/api/Atividade/filtrarAtividadesAdicionar")
    Call<ArrayList<Atividade>> filtrarAtividadesAdicionar(@Body FiltroPesquisaHome filtro);

    @POST("/api/Atividade/filtrarAtividadesExecutar")
    Call<ArrayList<Atividade>> filtrarAtividadesExecutar(@Body FiltroPesquisaHome filtro);

    @POST("/api/Atividade/getDetalhesAtividade")
    Call<DetalhesAtividade> getDetalhesAtividade(@Body int idAtividade);

    @POST("/api/Atividade/alterarAtividade")
    Call<Boolean> alterarAtividade(@Body Atividade atividade);

    @POST("/api/Atividade/removerAtividadeById")
    Call<Boolean> removerAtividadeById(@Body int idAtividade);
}
