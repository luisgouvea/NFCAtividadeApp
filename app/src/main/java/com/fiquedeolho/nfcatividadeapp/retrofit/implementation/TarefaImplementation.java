package com.fiquedeolho.nfcatividadeapp.retrofit.implementation;

import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.retrofit.ErrorUtils;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.TarefaRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.util.BaseObjectRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TarefaImplementation implements BaseObjectRequest {

    private TarefaRetrofit tarefaRetrofit;
    private APIError error;
    private Tarefa tarefa;

    private ArrayList<ArrayList<Tarefa>> listTarefasByIdAtividade;

    private Call<ArrayList<Tarefa>> callGetListTarefasByIdAtividade;


    public TarefaImplementation() {
        tarefaRetrofit = BaseUrlRetrofit.retrofit.create(TarefaRetrofit.class);
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


    public void requestSelectAllObjectsByIdAtividade(Callback callbackGeneric, int idAtividade) {
        Callback<ArrayList<Tarefa>> callbackGetAllTarefaByIdAtiv = (Callback<ArrayList<Tarefa>>) callbackGeneric;
        Call<ArrayList<Tarefa>> call = tarefaRetrofit.getTarefasByIdAtividade(idAtividade);
        call.enqueue(callbackGetAllTarefaByIdAtiv);
        this.callGetListTarefasByIdAtividade = call;
    }

    public ArrayList resultSelectAllObjectsByIdAtividade() {
        return listTarefasByIdAtividade;
    }

    @Override
    public String findResponse(Call call, Response response) {
        if (call == callGetListTarefasByIdAtividade) {
            verifyResponse(response);
            if (error != null) {
                return "erro";
            }
            if (call == callGetListTarefasByIdAtividade) {
                return "getTarefasByIdAtividade";
            }
        }
        return "";
    }

    @Override
    public APIError verifyResponse(Response response) {
        clearObject();
        if (response != null && response.isSuccessful() && response.code() == 200) {

            String url = response.raw().networkResponse().request().url().url().toString();

            if (url.contains("getTarefasByIdAtividade")) {
                listTarefasByIdAtividade = (ArrayList<ArrayList<Tarefa>>) response.body();
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
        listTarefasByIdAtividade = null;
        error = null;
        tarefa = null;
    }
}
