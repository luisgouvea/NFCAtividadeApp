package com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.util;

import com.fiquedeolho.nfcatividadeapp.models.APIError;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface BaseObjectRequest<ObjectTarget> {

    void requestSelectAllObjects(Callback<ObjectTarget> callbackGeneric);
    ArrayList<ObjectTarget> resultSelectAllObject();

    void requestSelectObjectById(Callback<ObjectTarget> callbackGeneric, int id);
    ObjectTarget resultSelectObject();

    void requestInsertObject(Callback<ObjectTarget> callbackGeneric, Object object);
    Boolean resultInsertObject();

    void requestUpdateObject(Callback<ObjectTarget> callbackGeneric, Object object);
    Boolean resultUpdateObject();

    void requestDeleteObject(Callback<ObjectTarget> callbackGeneric, Object object);
    Boolean resultDeleteObject();

    String findResponse(Call<ObjectTarget> call, Response<ObjectTarget> response);
    APIError verifyResponse(Response<ObjectTarget> response);
    APIError resultError();
    void clearObject();
}
