package com.fiquedeolho.nfcatividadeapp.retrofit.interfaces;

import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuario;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificacaoUsuarioRetrofit {

    @POST("/api/Notificacao/addNotificacao")
    Call<Boolean> addNotificacao(@Body NotificacaoUsuario notificacao);

    @POST("/api/Notificacao/getNotificacoesByUsuario")
    Call<ArrayList<NotificacaoUsuario>> getNotificacoesByUsuario(@Body int idUsuario);

    @POST("/api/Notificacao/updateNotificacao")
    Call<Boolean> updateNotificacao(@Body NotificacaoUsuario notificacaoUsuario);

    @POST("/api/Notificacao/getCountNotificacoesParaVisualizarUsuario")
    Call<Integer> getCountNotificacoesParaVisualizarUsuario(@Body int idUsuario);
}
