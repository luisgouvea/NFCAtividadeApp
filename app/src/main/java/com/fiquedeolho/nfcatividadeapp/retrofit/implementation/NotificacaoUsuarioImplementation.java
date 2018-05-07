package com.fiquedeolho.nfcatividadeapp.retrofit.implementation;


import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuario;
import com.fiquedeolho.nfcatividadeapp.retrofit.ErrorUtils;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.NotificacaoUsuarioRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.util.BaseObjectRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacaoUsuarioImplementation implements BaseObjectRequest {

    /**
     * Result
     */
    private NotificacaoUsuarioRetrofit notificacaoUsuarioRetrofit;
    private ArrayList<Object> listNotificacaoByIdUsuario;
    private APIError error;
    private Boolean insertNotificacao;
    private Boolean updateNotificacao;
    private NotificacaoUsuario notificacaoUsuario;
    private int countNotificacoesNaoVista;

    /**
     * Request
     */
    private Call<Boolean> callAddNotificacaoUsu;
    private Call<Boolean> callUpdateNotificacaoUsu;
    private Call<ArrayList<Object>> callGetAllNotificacaoUsuByIdUsu;
    private Call<Integer> callCountNotificacaoUsu;
    private Call<NotificacaoUsuario> callGetNotificacao;

    public NotificacaoUsuarioImplementation() {
        notificacaoUsuarioRetrofit = BaseUrlRetrofit.retrofit.create(NotificacaoUsuarioRetrofit.class);
    }

    @Override
    public void requestSelectAllObjects(Callback callbackGeneric) {

    }

    @Override
    public ArrayList resultSelectAllObject() {
        return null;
    }

    public void requestSelectAllObjectsByIdUsuario(Callback callbackGeneric, int idUsuario) {
        Callback<ArrayList<Object>> callbackListNotiByIdUsu = (Callback<ArrayList<Object>>) callbackGeneric;
        Call<ArrayList<Object>> call = notificacaoUsuarioRetrofit.getNotificacoesByUsuario(idUsuario);
        call.enqueue(callbackListNotiByIdUsu);
        this.callGetAllNotificacaoUsuByIdUsu = call;
    }

    public ArrayList<Object> resultSelectAllObjectByIdUsuario() {
        return listNotificacaoByIdUsuario;
    }

    public void requestCountNotificacoesNaoVista(Callback callbackGeneric, int idUsuario) {
        Callback<Integer> callbackCountNotificacoesNaoVista = (Callback<Integer>) callbackGeneric;
        Call<Integer> call = notificacaoUsuarioRetrofit.getCountNotificacoesParaVisualizarUsuario(idUsuario);
        call.enqueue(callbackCountNotificacoesNaoVista);
        this.callCountNotificacaoUsu = call;
    }

    public int resultCountNotificacoesNaoVista() {
        return countNotificacoesNaoVista;
    }

    @Override
    public void requestSelectObjectById(Callback callbackGeneric, int id) {

    }

    @Override
    public Object resultSelectObject() {
        return notificacaoUsuario;
    }

    @Override
    public void requestInsertObject(Callback callbackGeneric, Object object) {
        Callback<Boolean> callbackInsertNotiUsu = (Callback<Boolean>) callbackGeneric;
        NotificacaoUsuario notiUsu = (NotificacaoUsuario) object;
        Call<Boolean> call = notificacaoUsuarioRetrofit.addNotificacao(notiUsu);
        call.enqueue(callbackInsertNotiUsu);
        this.callAddNotificacaoUsu = call;
    }

    @Override
    public Boolean resultInsertObject() {
        return insertNotificacao;
    }

    @Override
    public void requestUpdateObject(Callback callbackGeneric, Object object) {
        Callback<Boolean> callbackUpdateNotiUsu = (Callback<Boolean>) callbackGeneric;
        NotificacaoUsuario notiUsu = (NotificacaoUsuario) object;
        Call<Boolean> call = notificacaoUsuarioRetrofit.updateNotificacao(notiUsu);
        call.enqueue(callbackUpdateNotiUsu);
        this.callUpdateNotificacaoUsu = call;
    }

    @Override
    public Boolean resultUpdateObject() {
        return updateNotificacao;
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
        if (call == callAddNotificacaoUsu ||
                call == callGetAllNotificacaoUsuByIdUsu ||
                call == callUpdateNotificacaoUsu ||
                call == callCountNotificacaoUsu) {
            verifyResponse(response);
            if (error != null) {
                return "erro";
            }
            if (call == callAddNotificacaoUsu) {
                return "addNotificacaoUsu";
            }
            if (call == callGetAllNotificacaoUsuByIdUsu) {
                return "getAllNotificacaoUsuByIdUsu";
            }

            if (call == callUpdateNotificacaoUsu) {
                return "updateNotificacao";
            }

            if (call == callCountNotificacaoUsu) {
                return "getCountNotificacoesParaVisualizarUsuario";
            }
        }
        return "";
    }

    @Override
    public APIError verifyResponse(Response response) {
        clearObject();
        if (response != null && response.isSuccessful() && response.code() == 200) {

            String url = response.raw().networkResponse().request().url().url().toString();

            if (url.contains("updateNotificacao")) {
                updateNotificacao = (Boolean) response.body();
            } else if (url.contains("getNotificacoesByUsuario")) {
                listNotificacaoByIdUsuario = (ArrayList<Object>) response.body();
            } else if (url.contains("addNotificacao")) {
                insertNotificacao = (Boolean) response.body();
            } else if (url.contains("getCountNotificacoesParaVisualizarUsuario")) {
                countNotificacoesNaoVista = (Integer) response.body();
            }

            return null;
        } else {
            // Aconteceu algum erro
            error = ErrorUtils.parseError(response);

            //Log.d("error message", error.message());
            return error;
        }
    }

    @Override
    public APIError resultError() {
        return error;
    }

    @Override
    public void clearObject() {
        listNotificacaoByIdUsuario = null;
        error = null;
        notificacaoUsuario = null;
        updateNotificacao = null;
        insertNotificacao = null;
        countNotificacoesNaoVista = -1;
    }
}
