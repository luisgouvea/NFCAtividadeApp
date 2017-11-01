package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private HomeActivity.ViewHolder homeViewHolder = new ViewHolder();

    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuario = extras.getString("idUsuario");
        }
        this.homeViewHolder.btnAtiviExecutar = (Button) findViewById(R.id.btn_ativExecutar);
        this.homeViewHolder.btnAtiviExecutar.setOnClickListener(this);

    }

    private static class ViewHolder {
        Button btnAtiviExecutar;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_ativExecutar) {

            this.getAtivExecutar();
        }
    }

    private void getAtivExecutar() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        final Context contextoHome = this;
//        pDialog.setMessage("Aguarde, buscando atividades...");
//        pDialog.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        JSONArray params = new JSONArray();
        params.put(this.idUsuario);

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

                        JSONArray listTag = atividadeAPI.getJSONArray("listTag");
                        ArrayList<TAG> listTagsAtividade = new ArrayList();
                        for (int j = 0; j < listTag.length(); j++) {
                            JSONObject tag_target = listTag.getJSONObject(j);
                            JSONArray jsonAntecessores = tag_target.getJSONArray("listAntecessores");

                            ArrayList<String> listAntecessores = new ArrayList();
                            for(int k=0; k < jsonAntecessores.length(); k++){
                                listAntecessores.add(jsonAntecessores.getString(k));
                            }
                            TAG tag = new TAG();
                            tag.setId(tag_target.getInt("Id"));
                            tag.setNome(tag_target.getString("Nome"));
                            tag.setListAntecessores(listAntecessores);
                            listTagsAtividade.add(tag);
                        }
                        atividade.setListTags(listTagsAtividade);
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