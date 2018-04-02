package com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService;

import com.fiquedeolho.nfcatividadeapp.models.TAG;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TagRetrofit {
    @POST("/api/Tag/getTagsByIdAtividade")
    Call<ArrayList<TAG>> getTarefasByIdAtividade(@Body int idAtividade);
}
