package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.dialog.DialogCheckNFCRead;
import com.fiquedeolho.nfcatividadeapp.dialog.DialogDefaultErro;
import com.fiquedeolho.nfcatividadeapp.models.APIError;
import com.fiquedeolho.nfcatividadeapp.retrofit.ErrorUtils;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.TarefaRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTarefas.TarefasListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class InfTarefasExecutorActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {

    private ViewHolderInfTarefasExecutor mViewHolderInfTarefasExecutor = new ViewHolderInfTarefasExecutor();
    private int IdAtividade;
    private ProgressDialog pDialog;
    private ArrayList<Tarefa> listTarefas;
    private DialogCheckNFCRead dialogCheck;
    private int idTarefa;
    private TarefasListAdapter tarefasListAdapter;
    private DialogDefaultErro dialogDefaultErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_tarefas_executor);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialogCheck = DialogCheckNFCRead.newInstance();

        dialogDefaultErro = DialogDefaultErro.newInstance();

        this.mViewHolderInfTarefasExecutor.mViewTextListTarefaVaziaInfTarefasExecutor = findViewById(R.id.textListTarefaVaziaInfTarefasExecutor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
        }
        getListTarefas();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dialogCheck.intentNFCTag(intent, idTarefa);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if(dialogDefaultErro != null && dialogDefaultErro.isVisible()){
            dialogDefaultErro.dismiss();
        }
    }

    private void getListTarefas() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setTitle(getString(R.string.title_progress_tarefa_list));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TarefaRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(TarefaRetrofit.class);
        final Call<ArrayList<Tarefa>> call = ativiInterface.getTarefasByIdAtividade(this.IdAtividade);
        call.enqueue(new Callback<ArrayList<Tarefa>>() {
            @Override
            public void onResponse(Call<ArrayList<Tarefa>> call, retrofit2.Response<ArrayList<Tarefa>> response) {
                if(response.code() == 200) {
                    listTarefas = response.body();
                    if (listTarefas == null || listTarefas.size() == 0) {
                        mViewHolderInfTarefasExecutor.mViewTextListTarefaVaziaInfTarefasExecutor.setVisibility(View.VISIBLE);
                    } else {
                        mViewHolderInfTarefasExecutor.mViewTextListTarefaVaziaInfTarefasExecutor.setVisibility(View.GONE);
                        SetarRecyclerView();
                    }
                }
                else{
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    APIError error = ErrorUtils.parseError(response);
                    dialogDefaultErro.setTextErro(error.message());
                    dialogDefaultErro.show(getSupportFragmentManager(),"dialog");
                }
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tarefa>> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                dialogDefaultErro.setTextErro(t.getMessage());
                dialogDefaultErro.show(getSupportFragmentManager(),"dialog");
            }
        });
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderInfTarefasExecutor.mViewRecyclerViewInfTarefasExecutor = findViewById(R.id.recyclerViewInfTarefasExecutor);

        /**
         * OnListClickInteractionListenerView interface QUE CRIEI
         Implementacao da acao dos menus na listagem das atividade dentro do RecyclerView
         Parametro: O viewTarget em questao, representa o Text (tres pontinhos) clicado
         */
        OnListClickInteractionListenerView listenerOptionsList = new OnListClickInteractionListenerView() {
            @Override
            public void onClick(final View viewTarget) {
                final int idTarefaTarget = viewTarget.getId();
                PopupMenu popupMenu = new PopupMenu(viewTarget.getContext(), viewTarget);
                popupMenu.inflate(R.menu.options_list_tarefa_executor);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnu_item_realizar_check_tarefa:
                                idTarefa = idTarefaTarget;
                                dialogCheck.show(getSupportFragmentManager(), "dialog");
                                break;
                            case R.id.mnu_item_detalhes_tarefa_executor:
                                DetalhesTarefaActivity.idTarefa = idTarefaTarget;
                                Intent intentDetalhesTarefa = new Intent(getApplicationContext(), DetalhesTarefaActivity.class);
                                startActivity(intentDetalhesTarefa);
                                break;
                            case R.id.mnu_item_sinalizar_problema_tarefa:
                                SinalizarProblemaTarefaActivity.idTarefa = idTarefaTarget;
                                Intent intentSinalizarProblemaExecutor = new Intent(getApplicationContext(), SinalizarProblemaTarefaActivity.class);
                                startActivity(intentSinalizarProblemaExecutor);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            }
        };

        // 2 - Definir adapter passando listagem de tarefas e listener
        tarefasListAdapter = new TarefasListAdapter(listTarefas, listenerOptionsList);
        this.mViewHolderInfTarefasExecutor.mViewRecyclerViewInfTarefasExecutor.setAdapter(tarefasListAdapter);

        this.mViewHolderInfTarefasExecutor.mViewRecyclerViewInfTarefasExecutor.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderInfTarefasExecutor.mViewRecyclerViewInfTarefasExecutor.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        getListTarefas();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderInfTarefasExecutor {

        private RecyclerView mViewRecyclerViewInfTarefasExecutor;
        private TextView mViewTextListTarefaVaziaInfTarefasExecutor;
    }
}
