package com.fiquedeolho.nfcatividadeapp.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.dialog.DialogDefaultErro;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioAddAtividade;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioProblemaTarefa;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infNotificacoes.NotificacaoListAdapter;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infNotificacoes.NotificacaoListViewHolder;
import com.google.gson.Gson;

import java.util.ArrayList;

public class InfNotificacoesActivity extends AppCompatActivity implements NotificacaoListViewHolder.ClickListener {

    private ViewHolderInfNotificacoes mViewHolderInfNotificacoes = new ViewHolderInfNotificacoes();
    public static ArrayList<Object> listaNotificacoes;
    private DialogDefaultErro dialogDefaultErro;
    private NotificacaoListViewHolder.ClickListener listener = this;
    private NotificacaoListAdapter notificacaoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_notificacoes);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogDefaultErro = DialogDefaultErro.newInstance();

        this.mViewHolderInfNotificacoes.mViewTextListNotificacoesVazia = findViewById(R.id.textListNotificacoesVazia);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SetarRecyclerView();
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderInfNotificacoes.mViewRecyclerViewNotificacoes = findViewById(R.id.recyclerViewNotificacoes);

        // 2 - Definir adapter passando listagem de tarefas e listener
        notificacaoListAdapter = new NotificacaoListAdapter(listaNotificacoes, listener);
        this.mViewHolderInfNotificacoes.mViewRecyclerViewNotificacoes.setAdapter(notificacaoListAdapter);

        this.mViewHolderInfNotificacoes.mViewRecyclerViewNotificacoes.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderInfNotificacoes.mViewRecyclerViewNotificacoes.setLayoutManager(linearLayoutManager);
    }

    /**
     * Click no botao voltar da activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToInitialNavigation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void backToInitialNavigation() {
        Intent resultIntent = new Intent(this, InitialNavigationActivity.class);
        startActivity(resultIntent);
        finish();
    }

    @Override
    public void containerRowClicked(View v, int position) {
        Gson gson = new Gson();
        NotificacaoUsuarioProblemaTarefa notificacaoUsuarioProblemaTarefa = null;
        NotificacaoUsuarioAddAtividade notificacaoUsuarioAddAtividade = null;
        Object obj = listaNotificacoes.get(position);
        String json = gson.toJson(obj);
        try {
            notificacaoUsuarioAddAtividade = gson.fromJson(json, NotificacaoUsuarioAddAtividade.class);
        } catch (Exception e) {
            notificacaoUsuarioProblemaTarefa = gson.fromJson(json, NotificacaoUsuarioProblemaTarefa.class);
        }
        if (notificacaoUsuarioAddAtividade != null) {
            // notificacao add atividade
            if(notificacaoUsuarioAddAtividade.getVisualizada() == false){
                InitialNavigationActivity.countNotificacoesUsu -= 1;
            }
        } else {
            // notificacao problema
            if(notificacaoUsuarioProblemaTarefa.getVisualizada() == false){
                InitialNavigationActivity.countNotificacoesUsu -= 1;
            }
        }
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderInfNotificacoes {

        private RecyclerView mViewRecyclerViewNotificacoes;
        private TextView mViewTextListNotificacoesVazia;
    }
}
