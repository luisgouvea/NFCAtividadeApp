package com.fiquedeolho.nfcatividadeapp.retrofit.implementation;

import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.retrofit.ErrorUtils;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.AtividadeRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.util.BaseObjectRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtividadeImplementation implements BaseObjectRequest {

    private AtividadeRetrofit atividadeRetrofit;
    private APIError error;
    private Atividade atividade;

    private Call<Atividade> callGetAtividade;

    public AtividadeImplementation() {
        atividadeRetrofit = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
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
        Callback<Atividade> callbackGetAtividadeById = (Callback<Atividade>) callbackGeneric;
        Call<Atividade> call = atividadeRetrofit.getAtividadeById(id);
        call.enqueue(callbackGetAtividadeById);
        this.callGetAtividade = call;
    }

    @Override
    public Atividade resultSelectObject() {
        return atividade;
    }

    @Override
    public void requestInsertObject(Callback callbackGeneric, Object object) {

    }

    @Override
    public Boolean resultInsertObject() {
        return null;
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
        if (call == callGetAtividade) {
            verifyResponse(response);
            if (error != null) {
                return "erro";
            }
            if (call == callGetAtividade) {
                return "getAtividadeById";
            }
        }
        return "";
    }

    @Override
    public APIError verifyResponse(Response response) {
        clearObject();
        if (response != null && response.isSuccessful() && response.code() == 200) {

            String url = response.raw().networkResponse().request().url().url().toString();

            if (url.contains("getAtividadeById")) {
                atividade = (Atividade) response.body();
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
        error = null;
        atividade = null;
    }
}
