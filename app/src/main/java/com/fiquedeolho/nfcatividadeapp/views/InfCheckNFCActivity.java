package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.AtividadeTarefaCheckRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.AtividadeTarefaCheck;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infCheckNFC.RegistroCheckListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class InfCheckNFCActivity extends Activity {

    private ViewHolderInfCheck mViewHolderInfCheck = new ViewHolderInfCheck();
    private ArrayList<AtividadeTarefaCheck> listaCheck;
    private RegistroCheckListAdapter registroCheckListAdapter;
    private ProgressDialog pDialog;
    private int IdAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_check_nfc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
        }

        this.mViewHolderInfCheck.mViewTextListCheckVaziaInfCheck = findViewById(R.id.textListAtividadeTarefaCheckRegistroCheck);
    }

    private void getListChecks() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //pDialog.setTitle(getString(R.string.title_progress_tag_list));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        AtividadeTarefaCheckRetrofit ativTarefaInterface = BaseUrlRetrofit.retrofit.create(AtividadeTarefaCheckRetrofit.class);
        final Call<ArrayList<AtividadeTarefaCheck>> call = ativTarefaInterface.getRegistroCheckNFC(this.IdAtividade);
        call.enqueue(new Callback<ArrayList<AtividadeTarefaCheck>>() {
            @Override
            public void onResponse(Call<ArrayList<AtividadeTarefaCheck>> call, retrofit2.Response<ArrayList<AtividadeTarefaCheck>> response) {
                listaCheck = response.body();
                if (listaCheck == null || listaCheck.size() == 0) {
                    mViewHolderInfCheck.mViewTextListCheckVaziaInfCheck.setVisibility(View.VISIBLE);
                } else {
                    mViewHolderInfCheck.mViewTextListCheckVaziaInfCheck.setVisibility(View.GONE);
                    SetarRecyclerView();
                }
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AtividadeTarefaCheck>> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderInfCheck.mViewRecyclerViewInfCheck = findViewById(R.id.recyclerViewRegistroCheck);

        // 2 - Definir adapter passando listagem de tarefas e listener
        registroCheckListAdapter = new RegistroCheckListAdapter(listaCheck);
        this.mViewHolderInfCheck.mViewRecyclerViewInfCheck.setAdapter(registroCheckListAdapter);

        this.mViewHolderInfCheck.mViewRecyclerViewInfCheck.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderInfCheck.mViewRecyclerViewInfCheck.setLayoutManager(linearLayoutManager);
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderInfCheck {

        private RecyclerView mViewRecyclerViewInfCheck;
        private TextView mViewTextListCheckVaziaInfCheck;
    }
}
