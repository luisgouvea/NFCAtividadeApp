package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.util.ConstantsURIAPI;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private HomeActivity.ViewHolder homeViewHolder = new ViewHolder();

    private String idUsuario;
    private Context contextoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        contextoHome = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuario = extras.getString("idUsuario");
        }
        this.homeViewHolder.btnAtiviExecutar = (Button) findViewById(R.id.btn_ativExecutar);
        this.homeViewHolder.addFloatingAction = (FloatingActionButton) findViewById(R.id.btn_addFloatingAction);
        this.homeViewHolder.addFloatingAction.setOnClickListener(this);
        this.homeViewHolder.btnAtiviExecutar.setOnClickListener(this);

    }

    private static class ViewHolder {
        Button btnAtiviExecutar;
        FloatingActionButton addFloatingAction;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_ativExecutar) {

            this.getAtivExecutar();
        }
        else if(id == R.id.btn_addFloatingAction) {
            Intent intent = new Intent(contextoHome, WelcomeActivity.class);
            startActivity(intent);
        }
    }

    private void getAtivExecutar() {
        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Aguarde, buscando atividades...");
//        pDialog.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        JSONArray params = new JSONArray();
        params.put(this.idUsuario);
        ArrayList<TAG> aj = new ArrayList<TAG>();
        TAG tg = new TAG();
        tg.setNome("fdsf");
        aj.add(tg);

        Gson f = new Gson();
        String gg = f.toJson(aj);
        params.put(gg);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST, ConstantsURIAPI.GETATIVIDADESEXECUTAR, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //adicionar o Response para a proxima pagina
                ArrayList<Atividade> listAtividade = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject atividadeAPI = response.getJSONObject(i);
                        //cria atividade
                            Atividade atividade = new Atividade();
                            atividade.setNome(atividadeAPI.getString("Nome"));
                            atividade.setId(atividadeAPI.getInt("Id"));
                        //cria atividade
                        listAtividade.add(atividade);
                    } catch (Exception e) {
                        Log.d("Erro getAtivExecutar ->", e.getMessage());
                    }
                }
                Intent intent = new Intent(contextoHome, ListAtividadesActivity.class);
                intent.putExtra("idUsuario", idUsuario);
                intent.putParcelableArrayListExtra("listAtividade", listAtividade);
                startActivity(intent);
                //pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String h = "fjfjf";
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        rq.add(jsonObjReq);
    }
}