package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.util.ConstantsURIAPI;

import org.json.JSONArray;

import java.util.ArrayList;

public class ListAtividadesActivity extends AppCompatActivity  implements View.OnClickListener{

    private static final String[] STATUS_ATIVIDADE = new String[]{"Status da Atividade", "Não executada", "Executada"};
    private TableLayout tableAtividades;
    private String idUsuario;
    public ArrayList<Atividade> listAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_atividades);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuario = extras.getString("idUsuario");
            this.listAtividade = extras.getParcelable("listAtividade");
        }

        Spinner combo = (Spinner) findViewById(R.id.status_spinner);
        ArrayAdapter adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, STATUS_ATIVIDADE);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        combo.setAdapter(adp);

        tableAtividades = (TableLayout) findViewById(R.id.table_layout);

        for (int i = 0; i < 2; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);

            TableRow row = (TableRow) inflater.inflate(R.layout.activity_list_atividades_inflater, tableAtividades, false);

            Button botao = (Button) row.findViewById(R.id.botao);
            botao.setId(i);
            botao.setText("Teste_" + i);
            botao.setOnClickListener(this);
            tableAtividades.addView(row);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_filtrar_atividade){
           Spinner spinner = (Spinner) findViewById(R.id.status_spinner);
           String statusAtividade = spinner.getSelectedItem().toString();
           this.filtrarAtividades(statusAtividade);
        }else{
            //usuario clicou em verificar a atividade
            //fazer get das tags e roteiros
        }
    }

    private void filtrarAtividades(String status) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        final Context contextoListAtividades = this;
//        pDialog.setMessage("Aguarde, buscando atividades...");
//        pDialog.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        JSONArray params = new JSONArray();
        params.put(this.idUsuario);
        params.put(status);

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST, ConstantsURIAPI.GETATIVIDADESEXECUTAR, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                tableAtividades.removeAllViews();
                //para cada atividade recebida
                //criar as tables rows
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
