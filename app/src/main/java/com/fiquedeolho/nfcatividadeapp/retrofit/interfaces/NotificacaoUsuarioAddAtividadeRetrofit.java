package com.fiquedeolho.nfcatividadeapp.retrofit.interfaces;

import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioAddAtividade;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificacaoUsuarioAddAtividadeRetrofit {

    @POST("/api/NotificacaoAddAtividade/addNotificacaoAddAtividade")
    Call<Boolean> addNotificacaoAddAtividade(@Body NotificacaoUsuarioAddAtividade notificacao);
}
