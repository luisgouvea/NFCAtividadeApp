package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

public class DetailsAtividadeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_atividade);
        Atividade ativ = null;
        int idAtividade = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAtividade = extras.getInt("IdAtividade");
        }
        findViewById(R.id.realizar_Check_nfc).setOnClickListener(this);
        getDetailsAtividade(idAtividade);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.realizar_Check_nfc) {
            Intent intent = new Intent(this, CheckNFCActivity.class);
            startActivity(intent);
        }
    }

    private void getDetailsAtividade(int idAtividade) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        final Context contextoDetails = this;
        RequestQueue rq = Volley.newRequestQueue(this);
        JSONArray params = new JSONArray();
        params.put(idAtividade);

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST, ConstantsURIAPI.GETDETAILSROTEIROEXEC, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //adicionar o Response para a proxima pagina
                ArrayList<TAG> listTag = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject tagAPI = response.getJSONObject(i);
                        //cria tag
                        TAG tag = new TAG();
                        tag.setNome(tagAPI.getString("Nome"));
                        tag.setId(tagAPI.getInt("Id"));
                        //cria tag

                        listTag.add(tag);


                    } catch (Exception e) {
                        Log.d("Erro getAtivExecutar ->", e.getMessage());
                    }
                }
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_detais_ativ);
                ScrollView scroll = (ScrollView) findViewById(R.id.ScrollRoteiroTags);
                StringBuilder antecessoresConcat = new StringBuilder();
                for (int i = 0; i < listTag.size(); i++) {
                    TAG tag = listTag.get(i);
                    LayoutInflater inflater = LayoutInflater.from(contextoDetails);

                    Button botao = (Button) inflater.inflate(R.layout.activity_details_atividade_botao_inflater, linearLayout, false);
                    botao.setId(tag.getId());
                    botao.setText(tag.getNome());
                    linearLayout.addView(botao);

                    //TextView text = (TextView) inflater.inflate(R.layout.activity_details_atividade_text_inflater, scroll, false);
                    ArrayList<String> listAntecessores = tag.getListAntecessores();
                    //StringBuilder antecessoresConcat = new StringBuilder();
                    for (int j = 0; j < listAntecessores.size(); j++) {
                        antecessoresConcat.append(listAntecessores.get(i) + "\n");
                    }
//            text.setText(antecessoresConcat.toString()); // nome da TAG
//            linearLayout.addView(text);
                }
                LayoutInflater inflater = LayoutInflater.from(contextoDetails);
                TextView text = (TextView) inflater.inflate(R.layout.activity_details_atividade_text_inflater, scroll, false);
                text.setText(antecessoresConcat.toString()); // nome da TAG
                linearLayout.addView(text);
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
