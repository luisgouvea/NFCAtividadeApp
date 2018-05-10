package com.fiquedeolho.nfcatividadeapp.retrofit.interfaces;

import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioProblemaTarefa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificacaoUsuarioProblemaTarefaRetrofit {

    @POST("/api/NotificacaoProblemaTarefa/addNotificacaoProblemaTarefa")
    Call<Boolean> addNotificacaoProblemaTarefa(@Body NotificacaoUsuarioProblemaTarefa notificacao);
}
