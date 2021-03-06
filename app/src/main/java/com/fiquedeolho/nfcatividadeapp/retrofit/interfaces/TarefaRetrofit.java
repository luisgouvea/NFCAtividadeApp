package com.fiquedeolho.nfcatividadeapp.retrofit.interfaces;

import com.fiquedeolho.nfcatividadeapp.models.Tarefa;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TarefaRetrofit {
    @POST("/api/Tarefa/getTarefasByIdAtividade")
    Call<ArrayList<Tarefa>> getTarefasByIdAtividade(@Body int idAtividade);

    @POST("/api/Tarefa/getTarefasRoteiroByIdAtividade")
    Call<ArrayList<Tarefa>> getTarefasRoteiroByIdAtividade(@Body int idAtividade);

    @POST("/api/Tarefa/addTarefa")
    Call<Boolean> addTarefa(@Body Tarefa tarefa);
}
