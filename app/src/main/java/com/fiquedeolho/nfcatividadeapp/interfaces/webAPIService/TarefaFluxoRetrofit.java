package com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService;


import com.fiquedeolho.nfcatividadeapp.models.Tarefa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TarefaFluxoRetrofit {

    @POST("/api/TarefaFluxo/setarFluxoTarefa")
    Call<Boolean> setarFluxoTarefa(@Body Tarefa tarefaTarget);
}
