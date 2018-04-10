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
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TagRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTarefas.TarefasListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class InfTarefasActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolderInfTarefas mViewHolderInfTarefas = new ViewHolderInfTarefas();
    private int IdAtividade;
    private ArrayList<TAG> listTags = new ArrayList<>();
    private TarefasListAdapter tarefasListAdapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_tarefas);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * Pegando os elementos da Activity
         */
        this.mViewHolderInfTarefas.mViewBtnDefinirRegras = findViewById(R.id.btn_definir_regras_tarefas);
        this.mViewHolderInfTarefas.mViewFloatingActionButtonAddTarefa = findViewById(R.id.btn_addFloatingAction_add_tarefa);
        this.mViewHolderInfTarefas.mViewTextListTarefaVaziaInfTarefas = findViewById(R.id.textListTarefaVaziaInfTarefas);
        this.mViewHolderInfTarefas.mViewLinearContentBtnDefinirRegras = findViewById(R.id.linear_contet_btns_inf_tarefas);

        /**
         * Comportamento dos botoes
         */
        this.mViewHolderInfTarefas.mViewBtnDefinirRegras.setOnClickListener(this);
        this.mViewHolderInfTarefas.mViewFloatingActionButtonAddTarefa.setOnClickListener(this);
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

    private void getListTarefas() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setTitle(getString(R.string.title_progress_tarefa_list));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TagRetrofit ativiInterface = BaseUrlRetrofit.retrofit.create(TagRetrofit.class);
        final Call<ArrayList<TAG>> call = ativiInterface.getTarefasByIdAtividade(this.IdAtividade);
        call.enqueue(new Callback<ArrayList<TAG>>() {
            @Override
            public void onResponse(Call<ArrayList<TAG>> call, retrofit2.Response<ArrayList<TAG>> response) {
                listTags = response.body();
                if (listTags == null || listTags.size() == 0) {
                    mViewHolderInfTarefas.mViewTextListTarefaVaziaInfTarefas.setVisibility(View.VISIBLE);
                } else {
                    mViewHolderInfTarefas.mViewLinearContentBtnDefinirRegras.setVisibility(View.VISIBLE);
                    SetarRecyclerView();
                }
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TAG>> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderInfTarefas.mViewRecyclerViewInfTarefas = findViewById(R.id.recyclerViewInfTarefas);

        this.mViewHolderInfTarefas.mViewRecyclerViewInfTarefas.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mViewHolderInfTarefas.mViewFloatingActionButtonAddTarefa.getVisibility() == View.VISIBLE) {
                    mViewHolderInfTarefas.mViewFloatingActionButtonAddTarefa.hide();
                } else if (dy < 0 && mViewHolderInfTarefas.mViewFloatingActionButtonAddTarefa.getVisibility() != View.VISIBLE) {
                    mViewHolderInfTarefas.mViewFloatingActionButtonAddTarefa.show();
                }
            }

            /*@Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mViewHolderInfTarefas.mViewFloatingActionButtonAddTarefa.show();
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
                final int idTag = viewTarget.getId();
                PopupMenu popupMenu = new PopupMenu(viewTarget.getContext(), viewTarget);
                popupMenu.inflate(R.menu.options_list_tarefa);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnu_vinc_tarefa_tag:
                                Bundle bundle = new Bundle();
                                bundle.putInt("IdTag", idTag);
                                bundle.putInt("IdAtividade", IdAtividade);
                                Intent intent = new Intent(getApplicationContext(), VinculoTarefaETagActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case R.id.mnu_deletar_tarefa:
                                Toast.makeText(getApplicationContext(), "Deletado", Toast.LENGTH_LONG).show();
                                int positionDeletar = descobrePositionArrayListAtiv(idTag);
                                listTags.remove(positionDeletar);
                                ObservableRecycler();
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
        tarefasListAdapter = new TarefasListAdapter(listTags, listenerOptionsList);
        this.mViewHolderInfTarefas.mViewRecyclerViewInfTarefas.setAdapter(tarefasListAdapter);

        this.mViewHolderInfTarefas.mViewRecyclerViewInfTarefas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderInfTarefas.mViewRecyclerViewInfTarefas.setLayoutManager(linearLayoutManager);
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

    private int descobrePositionArrayListAtiv(int idTag) {
        for (int i = 0; i < listTags.size(); i++) {
            TAG tag = listTags.get(i);
            if (tag.getId() == idTag) {
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
    private class ViewHolderInfTarefas {

        private RecyclerView mViewRecyclerViewInfTarefas;
        private FloatingActionButton mViewFloatingActionButtonAddTarefa;
        private Button mViewBtnDefinirRegras;
        private TextView mViewTextListTarefaVaziaInfTarefas;
        private LinearLayout mViewLinearContentBtnDefinirRegras;
    }
}
