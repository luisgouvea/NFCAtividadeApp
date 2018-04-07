package com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService;

import com.fiquedeolho.nfcatividadeapp.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuarioRetrofit {
    @POST("/api/Usuario/LogarUsuario")
    Call<Integer> logarUsuario(@Body List<String> value);

    @POST("/api/Usuario/ListAllUsuarioAddAtivVincExecutor")
    Call<ArrayList<Usuario>> listAllUsuarios(@Body int value);
}
