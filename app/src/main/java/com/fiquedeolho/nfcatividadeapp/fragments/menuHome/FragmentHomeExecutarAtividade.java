package com.fiquedeolho.nfcatividadeapp.fragments.menuHome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.AtividadeRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.executarAtividade.AtividadeListAdpter;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.executarAtividade.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.views.DetailsAtividadeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Conteudo abaixo do menu
 */

public class FragmentHomeExecutarAtividade extends Fragment implements View.OnClickListener {

    private String idUsuario;
    private ViewHolderExecutarAtivHome mViewHolderExecAtivHome = new ViewHolderExecutarAtivHome();
    private static final String[] STATUS_ATIVIDADE = new String[]{"Status da Atividade", "Disponível", "Finalizada"};
    private ArrayList<Atividade> listAtividadeExecutar = new ArrayList<Atividade>();
    private ProgressDialog pDialog;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_fazer_ativ, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setTitle(getString(R.string.title_progress_ativ_executar));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.setCancelable(false);
        pDialog.show();
        Bundle budle = getArguments();
        if (budle != null) {
            idUsuario = budle.getString("idUsuario");
        } else {
            //TODO REVER ESSA LOGICA DEPOIS ARRUMAR ISSO
            idUsuario = "1"; //
        }
        getAtivExecutar();
        super.onStart();
    }

    /*@Override
    public void onResume(){
        super.onResume();
        if(pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }*/

    /*@Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }*/

    public void MontaRestanteTela() {
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

        /*OnListClickInteractionListener listenerSeila = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {

            }
        };*/

        android.support.v7.widget.PopupMenu.OnMenuItemClickListener listenerOptionsList = new android.support.v7.widget.PopupMenu.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnu_item_save:
                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.mnu_item_delete:
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
        // 2 - Definir adapter passando listagem de carros e listener
        AtividadeListAdpter atividadeListAdapter = new AtividadeListAdpter(listAtividadeExecutar, listener, listenerOptionsList, getContext());
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

   /* private void getAtivExecutar() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AtividadeRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
                final Call<ArrayList<Atividade>> call = ativiInterface.getAtividadesExecutar(idUsuario);
                try{
                    listAtividadeExecutar = call.execute().body();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //this runs on the UI thread
                            MontaRestanteTela();
                            if(pDialog != null && pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                            //atividadeListAdapter.notifyDataSetChanged();
                        }
                    });
                }catch (Exception e){

                }
            }
        }).start();
        //AtividadeRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        *//*final Call<ArrayList<Atividade>> call = ativiInterface.getAtividadesExecutar(idUsuario);
        try {
            new NetworkCall().execute(call);
        } catch (Exception e) {
            String g = e.getMessage();
        }*//*
    }*/


    private class NetworkCall extends AsyncTask<Call, Void, ArrayList<Atividade>> {

        protected ArrayList<Atividade> doInBackground(Call [] params) {
            try {
                Call<ArrayList<Atividade>> call = params[0];
                return call.execute().body();
                //return response.body().toString();
            } catch (Exception e) {
                Log.d("@@jj@@", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Atividade> result) {
            listAtividadeExecutar = result;
            MontaRestanteTela();
            if(pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }
    private void getAtivExecutar(){
        AtividadeRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(AtividadeRetrofit.class);
        final Call<ArrayList<Atividade>> call = ativiInterface.getAtividadesExecutar(this.idUsuario);
        // TODO: Rever essa logica de Thread, ta gambiarra
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        call.enqueue(new Callback<ArrayList<Atividade>>() {
            @Override
            public void onResponse(Call <ArrayList<Atividade>> call, retrofit2.Response <ArrayList<Atividade>> response) {
                listAtividadeExecutar = response.body();
                MontaRestanteTela();
                if(pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call <ArrayList<Atividade>> call, Throwable t) {
                if(pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
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