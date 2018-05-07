package com.fiquedeolho.nfcatividadeapp.retrofit.implementation;

import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioAddAtividade;
import com.fiquedeolho.nfcatividadeapp.retrofit.ErrorUtils;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.NotificacaoUsuarioAddAtividadeRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.util.BaseObjectRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacaoUsuarioAddAtividadeImplementation implements BaseObjectRequest {

    private NotificacaoUsuarioAddAtividadeRetrofit notificacaoUsuarioAddAtividadeRetrofit;

    /**
     * Result
     */
    private Boolean insertNotificacaoAddAtividade;
    private APIError error;

    /**
     * Request
     */
    private Call<Boolean> callAddNotificacaoUsuAddAtividade;

    public NotificacaoUsuarioAddAtividadeImplementation(){
        notificacaoUsuarioAddAtividadeRetrofit = BaseUrlRetrofit.retrofit.create(NotificacaoUsuarioAddAtividadeRetrofit.class);
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
        Callback<Boolean> callbackInsertNotiUsu = (Callback<Boolean>) callbackGeneric;
        NotificacaoUsuarioAddAtividade notiUsu = (NotificacaoUsuarioAddAtividade) object;
        Call<Boolean> call = notificacaoUsuarioAddAtividadeRetrofit.addNotificacaoAddAtividade(notiUsu);
        call.enqueue(callbackInsertNotiUsu);
        this.callAddNotificacaoUsuAddAtividade = call;
    }

    @Override
    public Boolean resultInsertObject() {
        return insertNotificacaoAddAtividade;
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
        if (call == callAddNotificacaoUsuAddAtividade) {
            verifyResponse(response);
            if (error != null) {
                return "erro";
            }
            if (call == callAddNotificacaoUsuAddAtividade) {
                return "addNotificacaoAddAtividade";
            }
        }
        return "";
    }

    @Override
    public APIError verifyResponse(Response response) {
        clearObject();
        if (response != null && response.isSuccessful() && response.code() == 200) {

            String url = response.raw().networkResponse().request().url().url().toString();

            if (url.contains("addNotificacaoAddAtividade")) {
                insertNotificacaoAddAtividade = (Boolean) response.body();
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
        insertNotificacaoAddAtividade = null;
    }
}
