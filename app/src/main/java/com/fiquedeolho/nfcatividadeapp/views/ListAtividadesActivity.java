package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.util.ConstantsURIAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListAtividadesActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] STATUS_ATIVIDADE = new String[]{"Status da Atividade", "Não executada", "Executada"};
    private TableLayout tableAtividades;
    private String idUsuario;
    ArrayList<Atividade> listAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_atividades);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuario = extras.getString("idUsuario");
            listAtividade = extras.getParcelableArrayList("listAtividade");
        }

        Spinner combo = (Spinner) findViewById(R.id.status_spinner);
        ArrayAdapter adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, STATUS_ATIVIDADE);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        combo.setAdapter(adp);
        Button botao = (Button) findViewById(R.id.btn_filtrar_atividade);
        botao.setOnClickListener(this);
        this.createView();
    }

    public void createView() {

        tableAtividades = (TableLayout) findViewById(R.id.table_layout);

        for (int i = 0; i < this.listAtividade.size(); i++) {
            Atividade atividade = this.listAtividade.get(i);
            LayoutInflater inflater = LayoutInflater.from(this);

            TableRow row = (TableRow) inflater.inflate(R.layout.activity_list_atividades_inflater, tableAtividades, false);

            Button botao = (Button) row.findViewById(R.id.botao);
            botao.setId(atividade.getId()); // ID da Atividade
            botao.setText(atividade.getNome()); // Nome da Atividade
            botao.setOnClickListener(this);
            tableAtividades.addView(row);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_filtrar_atividade) {
            Spinner spinner = (Spinner) findViewById(R.id.status_spinner);
            String statusAtividade = spinner.getSelectedItem().toString();
            this.filtrarAtividades(statusAtividade);
        } else {
            //usuario clicou em verificar a atividade
            //fazer get das tags e roteiros
            Atividade ativ = this.listAtividade.get(id);
            Intent intent = new Intent(this, DetailsAtividadeActivity.class);
            intent.putExtra("atividadeForDetalhar", ativ);
            startActivity(intent);
        }
    }

    private void filtrarAtividades(String status) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        final Context contextoListAtividades = this;
        final View.OnClickListener listenerBotao = this;
//        pDialog.setMessage("Aguarde, buscando atividades...");
//        pDialog.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        JSONArray params = new JSONArray();
        params.put(this.idUsuario);
        params.put(status);

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST, ConstantsURIAPI.GETATIVIDADESEXECUTAR, params, new Response.Listener<JSONArray>() {

            public void createView() {

                tableAtividades = (TableLayout) findViewById(R.id.table_layout);
                for (int i = 0; i < listAtividade.size(); i++) {
                    Atividade atividade = listAtividade.get(i);
                    LayoutInflater inflater = LayoutInflater.from(contextoListAtividades);

                    TableRow row = (TableRow) inflater.inflate(R.layout.activity_list_atividades_inflater, tableAtividades, false);

                    Button botao = (Button) row.findViewById(R.id.botao);
                    botao.setId(atividade.getId()); // ID da Atividade
                    botao.setText(atividade.getNome()); // Nome da Atividade
                    botao.setOnClickListener(listenerBotao);
                    tableAtividades.addView(row);
                }
            }

            @Override
            public void onResponse(JSONArray response) {
                //adicionar o Response para a proxima pagina
                ArrayList<Atividade> list = new ArrayList<>();
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
                        list.add(atividade);
                    } catch (Exception e) {
                        Log.d("Erro getAtivExecutar ->", e.getMessage());
                    }
                }
                //remover as rows
                tableAtividades.removeAllViews();
                //para cada atividade recebida
                //criar as tables rows
                listAtividade = list;
                this.createView();
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
