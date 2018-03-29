package com.fiquedeolho.nfcatividadeapp.fragments.menuHome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.executarAtividade.AtividadeListAdpter;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.executarAtividade.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.util.ConstantsURIAPI;
import com.fiquedeolho.nfcatividadeapp.views.DetailsAtividadeActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Conteudo abaixo do menu
 */

public class FragmentHomeExecutarAtividade extends Fragment implements View.OnClickListener {

    private String idUsuario;
    private ViewHolderExecutarAtivHome mViewHolderExecAtivHome = new ViewHolderExecutarAtivHome();
    private static final String[] STATUS_ATIVIDADE = new String[]{"Status da Atividade", "Disponível", "Finalizada"};
    private ArrayList<Atividade> listAtividadeExecutar = new ArrayList<Atividade>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home_fazer_ativ, container, false);

        Bundle budle = getArguments();
        if (budle != null) {
            idUsuario = budle.getString("idUsuario");
        } else {
            //TODO REVER ESSA LOGICA DEPOIS ARRUMAR ISSO
            idUsuario = "1"; //
        }
        getAtivExecutar(rootView);

        return rootView;
    }

    public void MontaRestanteTela(final View rootView){
        this.mViewHolderExecAtivHome.mViewSpinnerAtivFazer = rootView.findViewById(R.id.status_spinner_atividade_fazer);
        ArrayAdapter adp = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, STATUS_ATIVIDADE);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        this.mViewHolderExecAtivHome.mViewSpinnerAtivFazer.setAdapter(adp);

        this.mViewHolderExecAtivHome.mVieBtnFiltrarAtivFazer = (Button) rootView.findViewById(R.id.btn_filtrar_atividade_fazer);
        this.mViewHolderExecAtivHome.mVieBtnFiltrarAtivFazer.setOnClickListener(this);

        // 1 - Obter a recyclerview
        this.mViewHolderExecAtivHome.mViewRecyclerViewAtividadeFazer = rootView.findViewById(R.id.recyclerViewAtividadeFazer);


        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt("IdAtividade", id);

                Intent intent = new Intent(rootView.getContext(), DetailsAtividadeActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        };

        // 2 - Definir adapter passando listagem de carros e listener
        AtividadeListAdpter atividadeListAdapter = new AtividadeListAdpter(listAtividadeExecutar, listener);
        this.mViewHolderExecAtivHome.mViewRecyclerViewAtividadeFazer.setAdapter(atividadeListAdapter);

        this.mViewHolderExecAtivHome.mViewRecyclerViewAtividadeFazer.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        this.mViewHolderExecAtivHome.mViewRecyclerViewAtividadeFazer.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        /*if (id == R.id.btn_ativExecutar) {

            this.getAtivExecutar();
        } else if (id == R.id.btn_addFloatingAction) {
            Intent intent = new Intent(contextoInitial, AddAtividadeActivity.class);
            startActivity(intent);
        }*/
    }

    private void getAtivExecutar(final View rootView) {
        final ProgressDialog pDialog = new ProgressDialog(rootView.getContext());
        pDialog.setMessage("Aguarde, buscando atividades...");
        pDialog.show();
        RequestQueue rq = Volley.newRequestQueue(rootView.getContext());
        JSONArray params = new JSONArray();
        params.put(this.idUsuario);
        /*ArrayList<com.fiquedeolho.nfcatividadeapp.models.TAG> aj = new ArrayList<TAG>();
        TAG tg = new TAG();
        tg.setNome("fdsf");
        aj.add(tg);

        Gson f = new Gson();
        String gg = f.toJson(aj);
        params.put(gg);*/
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST, ConstantsURIAPI.GETATIVIDADESEXECUTAR, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject atividadeAPI = response.getJSONObject(i);
                        //cria atividade
                        Atividade atividade = new Atividade();
                        atividade.setNome(atividadeAPI.getString("Nome"));
                        atividade.setId(atividadeAPI.getInt("Id"));
                        //cria atividade
                        listAtividadeExecutar.add(atividade);
                        MontaRestanteTela(rootView);
                    } catch (Exception e) {
                        Log.d("Erro getAtivExecutar ->", e.getMessage());
                    }
                }
                pDialog.hide();
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

        // TODO: Rever esse uso, pode existir uma outra forma e ainda, isso pode dar problema, pesquisar...
        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            String g = e.getMessage();
        }
    }


    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderExecutarAtivHome {

        private Spinner mViewSpinnerAtivFazer;
        private Button mVieBtnFiltrarAtivFazer;
        private RecyclerView mViewRecyclerViewAtividadeFazer;
    }
}