package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.TarefaFluxoRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.TarefaRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.models.TarefaPrecedente;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listPrecedencia.TarefasListPrecedenciaAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class PrecedenciaTarefaActivity extends AppCompatActivity implements View.OnClickListener {

    private int IdAtividade;
    private ArrayList<Tarefa> listTarefas = new ArrayList<>();
    private Tarefa tarefaTarget;
    private ViewHolderPrecedenciaTarefas mViewHolderPrecedenciaTarefas = new ViewHolderPrecedenciaTarefas();
    private TarefasListPrecedenciaAdapter tarefasListPrecedenciaAdapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precedencia_tarefa);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
            tarefaTarget = extras.getParcelable("tarefaTarget");
        }
        this.mViewHolderPrecedenciaTarefas.mViewTextNameTarefaTarget = findViewById(R.id.name_tarefa_target);
        this.mViewHolderPrecedenciaTarefas.mViewTextNameTarefaTarget.setText(tarefaTarget.getNome());
        this.mViewHolderPrecedenciaTarefas.mViewTextNameTarefaTarget.setVisibility(View.VISIBLE);

        this.mViewHolderPrecedenciaTarefas.mViewButtonSalvarPrecedencia = findViewById(R.id.btn_salvar_precedencia);
        this.mViewHolderPrecedenciaTarefas.mViewButtonSalvarPrecedencia.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getListTarefas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
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
                listTarefas = response.body();
                SetarRecyclerView();
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tarefa>> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderPrecedenciaTarefas.mViewRecyclerViewPrecedenciaTarefas = findViewById(R.id.recyclerViewPrecedenciaTarefas);

        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        OnListClickInteractionListenerView listener = new OnListClickInteractionListenerView() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                Tarefa tarefaClicada = getTarefaTarget(id); // Tarefa clicada
                ArrayList<TarefaPrecedente> listEncTarefaTarget = tarefaTarget.getListAntecessoras();
                int positionTarefaClicada = getPositionTarefa(tarefaClicada.getIdTarefa(), listEncTarefaTarget);
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked() && !containsTarefaPrecedente(tarefaClicada.getIdTarefa())) {
                    TarefaPrecedente tarefaPrecedente = new TarefaPrecedente();
                    tarefaPrecedente.setIdTarefaAntecessora(tarefaClicada.getIdTarefa());
                    tarefaPrecedente.setIdTarefa(tarefaTarget.getIdTarefa()); // id_tarefa_target
                    listEncTarefaTarget.add(tarefaPrecedente);
                } else {
                    // usuario desmarcou o checkBox
                    listEncTarefaTarget.remove(positionTarefaClicada);
                }
                tarefaTarget.setListAntecessoras(listEncTarefaTarget);
            }
        };

        ArrayList<Tarefa> listAux = new ArrayList<Tarefa>(listTarefas);
        listAux = removeTarefaTarget(listAux);
        // 2 - Definir adapter passando listagem de tarefas e listener
        tarefasListPrecedenciaAdapter = new TarefasListPrecedenciaAdapter(listAux, tarefaTarget, listener);
        this.mViewHolderPrecedenciaTarefas.mViewRecyclerViewPrecedenciaTarefas.setAdapter(tarefasListPrecedenciaAdapter);

        this.mViewHolderPrecedenciaTarefas.mViewRecyclerViewPrecedenciaTarefas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderPrecedenciaTarefas.mViewRecyclerViewPrecedenciaTarefas.setLayoutManager(linearLayoutManager);
    }

    private boolean containsTarefaPrecedente(int idTarefa) {
        ArrayList<TarefaPrecedente> list = tarefaTarget.getListAntecessoras();
        for (int i = 0; i < list.size(); i++) {
            TarefaPrecedente tarefaPrecedente = list.get(i);
            if (tarefaPrecedente.getIdTarefa() == idTarefa) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Tarefa> removeTarefaTarget(ArrayList<Tarefa> list) {
        for (int i = 0; i < list.size(); i++) {
            Tarefa tarefaList = list.get(i);
            if (tarefaList.getIdTarefa() == tarefaTarget.getIdTarefa()) {
                list.remove(i);
                return list;
            }
        }
        return null;
    }

    private Tarefa getTarefaTarget(int idTarefa) {
        for (int i = 0; i < listTarefas.size(); i++) {
            Tarefa tarefa = listTarefas.get(i);
            if (tarefa.getIdTarefa() == idTarefa) {
                return tarefa;
            }
        }
        return null;
    }

    private int getPositionTarefa(int idTarefa, ArrayList<TarefaPrecedente> lista) {
        for (int i = 0; i < lista.size(); i++) {
            TarefaPrecedente tarefa = lista.get(i);
            if (tarefa.getIdTarefaAntecessora() == idTarefa) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Click no botao voltar da activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToInfTarefas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void backToInfTarefas() {
        Intent resultIntent = new Intent(this, RegrasTarefasActivity.class);
        resultIntent.putExtra("IdAtividade", IdAtividade);
        startActivity(resultIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_salvar_precedencia) {
            salvarPrecedencia();
        }
    }

    private void salvarPrecedencia() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.setCancelable(false);
        pDialog.show();
        TarefaFluxoRetrofit tarefaFluxoInterface = BaseUrlRetrofit.retrofit.create(TarefaFluxoRetrofit.class);
        final Call<Boolean> call = tarefaFluxoInterface.setarFluxoTarefa(tarefaTarget);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                Boolean result = response.body();
                backToInfTarefas();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderPrecedenciaTarefas {

        private TextView mViewTextNameTarefaTarget;
        private RecyclerView mViewRecyclerViewPrecedenciaTarefas;
        private Button mViewButtonSalvarPrecedencia;
    }
}
