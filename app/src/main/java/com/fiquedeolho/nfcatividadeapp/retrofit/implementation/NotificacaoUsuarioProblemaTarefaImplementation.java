package com.fiquedeolho.nfcatividadeapp.retrofit.implementation;

import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioProblemaTarefa;
import com.fiquedeolho.nfcatividadeapp.retrofit.ErrorUtils;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.NotificacaoUsuarioProblemaTarefaRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.util.BaseObjectRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacaoUsuarioProblemaTarefaImplementation implements BaseObjectRequest {

    private NotificacaoUsuarioProblemaTarefaRetrofit notificacaoUsuarioProblemaTarefaRetrofit;

    /**
     * Result
     */
    private Boolean insertNotificacaoProblemTarefa;
    private APIError error;

    /**
     * Request
     */
    private Call<Boolean> callAddNotificacaoUsuProblemaTarefa;

    public NotificacaoUsuarioProblemaTarefaImplementation() {
        notificacaoUsuarioProblemaTarefaRetrofit = BaseUrlRetrofit.retrofit.create(NotificacaoUsuarioProblemaTarefaRetrofit.class);
    }


    @Override
    public void requestSelectAllObjects(Callback callbackGeneric) {

    }

    @Override
    public ArrayList resultSelectAllObject() {
        return null;
    }

    @Override
    public void requestSelectObjectById(Callback callbackGeneric, int id) {

    }

    @Override
    public Object resultSelectObject() {
        return null;
    }

    @Override
    public void requestInsertObject(Callback callbackGeneric, Object object) {
        Callback<Boolean> callbackInsertNotiProblema = (Callback<Boolean>) callbackGeneric;
        NotificacaoUsuarioProblemaTarefa notiProblema = (NotificacaoUsuarioProblemaTarefa) object;
        Call<Boolean> call = notificacaoUsuarioProblemaTarefaRetrofit.addNotificacaoProblemaTarefa(notiProblema);
        call.enqueue(callbackInsertNotiProblema);
        this.callAddNotificacaoUsuProblemaTarefa = call;
    }

    @Override
    public Boolean resultInsertObject() {
        return insertNotificacaoProblemTarefa;
    }

    @Override
    public void requestUpdateObject(Callback callbackGeneric, Object object) {

    }

    @Override
    public Boolean resultUpdateObject() {
        return null;
    }

    @Override
    public void requestDeleteObject(Callback callbackGeneric, Object object) {

    }

    @Override
    public Boolean resultDeleteObject() {
        return null;
    }

    @Override
    public String findResponse(Call call, Response response) {
        if (call == callAddNotificacaoUsuProblemaTarefa) {
            verifyResponse(response);
            if (error != null) {
                return "erro";
            }
            if (call == callAddNotificacaoUsuProblemaTarefa) {
                return "addNotificacaoProblemaTarefa";
            }
        }
        return "";
    }

    @Override
    public APIError verifyResponse(Response response) {
        clearObject();
        if (response != null && response.isSuccessful() && response.code() == 200) {

            String url = response.raw().networkResponse().request().url().url().toString();

            if (url.contains("addNotificacaoProblemaTarefa")) {
                insertNotificacaoProblemTarefa = (Boolean) response.body();
            }

            return null;
        } else {
            // Aconteceu algum erro
            error = ErrorUtils.parseError(response);

            return error;
        }
    }

    @Override
    public APIError resultError() {
        return error;
    }

    @Override
    public void clearObject() {
        error = null;
        insertNotificacaoProblemTarefa = null;
    }
}
