package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.UsuarioRetrofit;
import com.fiquedeolho.nfcatividadeapp.util.ConstantsURIAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder loginViewHolder = new ViewHolder();
    private ProgressDialog pDialog;

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
    protected void onPause(){
        super.onPause();
        if(pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    private static class ViewHolder {
        EditText login;
        EditText senha;
        Button btnLogin;
    }

    private void autenticarUsuario(String login, String senha){
        final Context contextoLogin = this;
        UsuarioRetrofit usuInterface = BaseUrlRetrofit.retrofit.create(UsuarioRetrofit.class);
        ArrayList <String> list = new ArrayList<>();
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
                Intent intent = new Intent(contextoLogin, InitialNavigationActivity.class);
                intent.putExtra("idUsuario", Integer.toString(idUsuario));
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Falha: " + String.valueOf(t.getMessage()), Toast.LENGTH_LONG).show();
                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }
            }
        });
    }

    /*private void autenticarUsuario(String login, String senha) {
        final Context contextoLogin = this;
        RequestQueue rq = Volley.newRequestQueue(this);
        JSONObject params = new JSONObject();
        try {
            params.put("login", login);
            params.put("senha", senha);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ConstantsURIAPI.AUTENTICACAO, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String idUsuario = response.getString("Id");
                    Log.d("ResultJSONLogin", response.toString());
                    Intent intent = new Intent(contextoLogin, InitialNavigationActivity.class);
                    intent.putExtra("idUsuario", idUsuario);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Erro", "Error: " + error.getMessage());
                pDialog.hide();
                Toast t = Toast.makeText(contextoLogin, "Por favor, tente novamente!", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        rq.add(jsonObjReq);
    }*/
}