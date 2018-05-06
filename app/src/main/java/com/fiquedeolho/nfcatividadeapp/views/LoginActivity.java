package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.SharedPreferences.SavePreferences;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.retrofit.implementation.NotificacaoUsuarioImplementation;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.UsuarioRetrofit;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity<T> extends AppCompatActivity implements View.OnClickListener, Callback<T> {

    private ViewHolder loginViewHolder = new ViewHolder();
    private ProgressDialog pDialog;
    private NotificacaoUsuarioImplementation notificacaoUsuarioImplementation = new NotificacaoUsuarioImplementation();
    private Callback<T> requestRetrofit = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.loginViewHolder.btnLogin = (Button) findViewById(R.id.btn_login);
        this.loginViewHolder.senha = (EditText) findViewById(R.id.input_password);
        this.loginViewHolder.login = (EditText) findViewById(R.id.input_email);

        this.loginViewHolder.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            final String senha = this.loginViewHolder.senha.getText().toString();
            final String login = this.loginViewHolder.login.getText().toString();
            this.autenticarUsuario(login, senha);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    private static class ViewHolder {
        EditText login;
        EditText senha;
        Button btnLogin;
    }

    private void autenticarUsuario(String login, String senha) {
        final Context contextoLogin = this;
        UsuarioRetrofit usuInterface = BaseUrlRetrofit.retrofit.create(UsuarioRetrofit.class);
        ArrayList<String> list = new ArrayList<>();
        list.add(login);
        list.add(senha);
        final Call<Integer> call = usuInterface.logarUsuario(list);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle(getString(R.string.title_progress_aut_login));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.setCancelable(false);
        pDialog.show();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                int idUsuario = response.body();
                SavePreferences save = new SavePreferences(contextoLogin);
                save.saveInt(KeysSharedPreference.ID_USUARIO_LOGADO, idUsuario);
                getNotificacoesNaoVista();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Falha: " + String.valueOf(t.getMessage()), Toast.LENGTH_LONG).show();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    private void getNotificacoesNaoVista() {
        SavePreferences save = new SavePreferences(this);
        int idUsuario = save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO);
        notificacaoUsuarioImplementation.requestCountNotificacoesNaoVista(requestRetrofit, idUsuario);


    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        APIError error = null;
        int countNotificacoes = 0;
        String typeResponse = notificacaoUsuarioImplementation.findResponse(call, response);
        if (typeResponse != "") {
            switch (typeResponse) {
                case "erro":
                    error = notificacaoUsuarioImplementation.resultError();
                    if (error.message() != null) {
                        Toast.makeText(getApplicationContext(), error.message(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro genérico", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "getCountNotificacoesParaVisualizarUsuario":
                    countNotificacoes = notificacaoUsuarioImplementation.resultCountNotificacoesNaoVista();
                    break;
            }
        }
        Context contextoLogin = this;
        Bundle bundle = new Bundle();
        bundle.putInt("countNotificacoes", countNotificacoes);
        Intent intent = new Intent(contextoLogin, InitialNavigationActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
}