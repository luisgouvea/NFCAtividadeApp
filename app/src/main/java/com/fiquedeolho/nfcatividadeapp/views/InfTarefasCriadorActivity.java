package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
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

public class InfTarefasCriadorActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolderInfTarefasCriador mViewHolderInfTarefasCriador = new ViewHolderInfTarefasCriador();
    private int IdAtividade;
    private ArrayList<Tarefa> listTarefas = new ArrayList<>();
    private TarefasListAdapter tarefasListAdapter;
    private ProgressDialog pDialog;
    private DialogDefaultErro dialogDefaultErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_tarefas_criador);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogDefaultErro = DialogDefaultErro.newInstance();

        /**
         * Pegando os elementos da Activity
         */
        this.mViewHolderInfTarefasCriador.mViewBtnDefinirRegras = findViewById(R.id.btn_definir_regras_tarefas);
        this.mViewHolderInfTarefasCriador.mViewFloatingActionButtonAddTarefa = findViewById(R.id.btn_addFloatingAction_add_tarefa);
        this.mViewHolderInfTarefasCriador.mViewTextListTarefaVaziaInfTarefas = findViewById(R.id.textListTarefaVaziaInfTarefas);
        this.mViewHolderInfTarefasCriador.mViewLinearContentBtnDefinirRegras = findViewById(R.id.linear_contet_btns_inf_tarefas);

        /**
         * Comportamento dos botoes
         */
        this.mViewHolderInfTarefasCriador.mViewBtnDefinirRegras.setOnClickListener(this);
        this.mViewHolderInfTarefasCriador.mViewFloatingActionButtonAddTarefa.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade"); // sempre vem da activity fragExecAtividade
        }
        getListTarefas();
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
                        mViewHolderInfTarefasCriador.mViewTextListTarefaVaziaInfTarefas.setVisibility(View.VISIBLE);
                    } else {
                        mViewHolderInfTarefasCriador.mViewTextListTarefaVaziaInfTarefas.setVisibility(View.GONE);
                        mViewHolderInfTarefasCriador.mViewLinearContentBtnDefinirRegras.setVisibility(View.VISIBLE);
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
        this.mViewHolderInfTarefasCriador.mViewRecyclerViewInfTarefas = findViewById(R.id.recyclerViewInfTarefas);

        this.mViewHolderInfTarefasCriador.mViewRecyclerViewInfTarefas.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mViewHolderInfTarefasCriador.mViewFloatingActionButtonAddTarefa.getVisibility() == View.VISIBLE) {
                    mViewHolderInfTarefasCriador.mViewFloatingActionButtonAddTarefa.hide();
                } else if (dy < 0 && mViewHolderInfTarefasCriador.mViewFloatingActionButtonAddTarefa.getVisibility() != View.VISIBLE) {
                    mViewHolderInfTarefasCriador.mViewFloatingActionButtonAddTarefa.show();
                }
            }

            /*@Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mViewHolderInfTarefasCriador.mViewFloatingActionButtonAddTarefa.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }*/
        });


        /**
         * OnListClickInteractionListenerView interface QUE CRIEI
         Implementacao da acao dos menus na listagem das atividade dentro do RecyclerView
         Parametro: O viewTarget em questao, representa o Text (tres pontinhos) clicado
         */
        OnListClickInteractionListenerView listenerOptionsList = new OnListClickInteractionListenerView() {
            @Override
            public void onClick(final View viewTarget) {
                final int idTarefa = viewTarget.getId();
                PopupMenu popupMenu = new PopupMenu(viewTarget.getContext(), viewTarget);
                popupMenu.inflate(R.menu.options_list_tarefa_criador);
                popupMenu.show();
                final Bundle bundle = new Bundle();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnu_item_detalhes_tarefa:
                                DetalhesTarefaActivity.idTarefa = idTarefa;
                                Intent intentDetalhesTarefa = new Intent(getApplicationContext(), DetalhesTarefaActivity.class);
                                startActivity(intentDetalhesTarefa);
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
        this.mViewHolderInfTarefasCriador.mViewRecyclerViewInfTarefas.setAdapter(tarefasListAdapter);

        this.mViewHolderInfTarefasCriador.mViewRecyclerViewInfTarefas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderInfTarefasCriador.mViewRecyclerViewInfTarefas.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_definir_regras_tarefas) {

            Bundle bundle = new Bundle();
            bundle.putInt("IdAtividade", IdAtividade);
            Intent intent = new Intent(this, RegrasTarefasActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            //finish();

        } else if (id == R.id.btn_addFloatingAction_add_tarefa) {

            Bundle bundle = new Bundle();
            bundle.putInt("IdAtividade", IdAtividade);
            Intent intent = new Intent(this, AddTarefaActivity.class);
            intent.putExtras(bundle);

            startActivity(intent);
            //finish();
        }
    }

    private int descobrePositionArrayListAtiv(int idTarefa) {
        for (int i = 0; i < listTarefas.size(); i++) {
            Tarefa tarefa = listTarefas.get(i);
            if (tarefa.getIdTarefa() == idTarefa) {
                return i;
            }
        }
        return 0;
    }

    private void ObservableRecycler() {
        tarefasListAdapter.notifyDataSetChanged();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderInfTarefasCriador {

        private RecyclerView mViewRecyclerViewInfTarefas;
        private FloatingActionButton mViewFloatingActionButtonAddTarefa;
        private Button mViewBtnDefinirRegras;
        private TextView mViewTextListTarefaVaziaInfTarefas;
        private LinearLayout mViewLinearContentBtnDefinirRegras;
    }
}
