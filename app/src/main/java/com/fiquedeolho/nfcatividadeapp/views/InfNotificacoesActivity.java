package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.SharedPreferences.SavePreferences;
import com.fiquedeolho.nfcatividadeapp.dialog.DialogDefaultErro;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioAddAtividade;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuarioProblemaTarefa;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infNotificacoes.NotificacaoListAdapter;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infNotificacoes.NotificacaoListViewHolder;
import com.fiquedeolho.nfcatividadeapp.retrofit.implementation.NotificacaoUsuarioImplementation;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfNotificacoesActivity<T> extends AppCompatActivity implements NotificacaoListViewHolder.ClickListener, Callback<T> {

    private ViewHolderInfNotificacoes mViewHolderInfNotificacoes = new ViewHolderInfNotificacoes();
    public ArrayList<Object> listaNotificacoes;
    private DialogDefaultErro dialogDefaultErro;
    private NotificacaoListViewHolder.ClickListener listener = this;
    private NotificacaoListAdapter notificacaoListAdapter;
    private ProgressDialog progressDialogListNoti;
    private NotificacaoUsuarioImplementation notificacaoUsuarioImplementation = new NotificacaoUsuarioImplementation();
    private Callback<T> requestRetrofit = this;

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
        progressDialogListNoti = new ProgressDialog(this);
        progressDialogListNoti.setMessage(getString(R.string.message_progress_dialog));
        progressDialogListNoti.setCancelable(false);
        progressDialogListNoti.show();
        SavePreferences save = new SavePreferences(this);
        int idUsuario = save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO);
        notificacaoUsuarioImplementation.requestSelectAllObjectsByIdUsuario(requestRetrofit, idUsuario);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        APIError error = null;
        String typeResponse = notificacaoUsuarioImplementation.findResponse(call, response);
        if (typeResponse != "") {
            switch (typeResponse) {
                case "erro":
                    if (progressDialogListNoti != null && progressDialogListNoti.isShowing()) {
                        progressDialogListNoti.dismiss();
                    }
                    error = notificacaoUsuarioImplementation.resultError();
                    if (error.message() != null) {
                        Toast.makeText(getApplicationContext(), error.message(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
                    }
                    break;
                case "getAllNotificacaoUsuByIdUsu":
                    listaNotificacoes = notificacaoUsuarioImplementation.resultSelectAllObjectByIdUsuario();
                    if (listaNotificacoes == null || listaNotificacoes.size() == 0) {
                        this.mViewHolderInfNotificacoes.mViewTextListNotificacoesVazia.setVisibility(View.VISIBLE);
                    } else {
                        SetarRecyclerView();
                    }
                    if (progressDialogListNoti != null && progressDialogListNoti.isShowing()) {
                        progressDialogListNoti.dismiss();
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (progressDialogListNoti != null && progressDialogListNoti.isShowing()) {
            progressDialogListNoti.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialogListNoti != null && progressDialogListNoti.isShowing()) {
            progressDialogListNoti.dismiss();
        }
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
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        NotificacaoUsuarioProblemaTarefa notificacaoUsuarioProblemaTarefa = null;
        NotificacaoUsuarioAddAtividade notificacaoUsuarioAddAtividade = null;
        Object obj = listaNotificacoes.get(position);
        String json = gson.toJson(obj);

        Intent intent;
        InfNotificacaoAddAtividadeActivity.notificacaoUsuarioAddAtividade = gson.fromJson(json, NotificacaoUsuarioAddAtividade.class);
        InfNotificacaoProblemaTarefaActivity.notificacaoUsuarioProblemaTarefa = gson.fromJson(json, NotificacaoUsuarioProblemaTarefa.class);
        if (InfNotificacaoAddAtividadeActivity.notificacaoUsuarioAddAtividade.getIdAtividade() != 0) {
            intent = new Intent(this, InfNotificacaoAddAtividadeActivity.class);
            InfNotificacaoProblemaTarefaActivity.notificacaoUsuarioProblemaTarefa = null;
        }else{
            intent = new Intent(this, InfNotificacaoProblemaTarefaActivity.class);
            InfNotificacaoAddAtividadeActivity.notificacaoUsuarioAddAtividade = null;
        }
        startActivity(intent);
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderInfNotificacoes {

        private RecyclerView mViewRecyclerViewNotificacoes;
        private TextView mViewTextListNotificacoesVazia;
    }
}
