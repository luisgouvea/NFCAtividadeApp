package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;

import java.util.ArrayList;

public class InfTarefasActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolderInfTarefas mViewHolderInfTarefas = new ViewHolderInfTarefas();
    private int idAtividade;
    private ArrayList<TAG> listTags = new ArrayList<>();
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
        this.mViewHolderInfTarefas.mViewBtnConcluirInfTarefa = findViewById(R.id.btn_concluir_inf_tarefas);
        this.mViewHolderInfTarefas.mViewBtnDefinirRegras = findViewById(R.id.btn_definir_regras_tarefas);
        this.mViewHolderInfTarefas.mViewFloatingActionButtonAddTarefa = findViewById(R.id.btn_addFloatingAction_add_tarefa);

        /**
         * Comportamento dos botoes
         */
        this.mViewHolderInfTarefas.mViewBtnConcluirInfTarefa.setOnClickListener(this);
        this.mViewHolderInfTarefas.mViewBtnDefinirRegras.setOnClickListener(this);
        this.mViewHolderInfTarefas.mViewFloatingActionButtonAddTarefa.setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAtividade = extras.getInt("IdAtividade"); // sempre vem da activity fragExecAtividade
            listTags = extras.getParcelableArrayList("listaTarefas");
        }
        if(listTags == null){
            // SELECT NO BANCO
        }
        ObservableRecycler();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_definir_regras_tarefas) {

        } else if (id == R.id.btn_concluir_inf_tarefas) {

        } else if (id == R.id.btn_addFloatingAction_add_tarefa) {

            Bundle bundle = new Bundle();
            bundle.putInt("IdAtividade", idAtividade);

            Intent intent = new Intent(this, AddTarefaActivity.class);
            intent.putExtras(bundle);

            startActivity(intent);
            finish();

        }
    }

    private void ObservableRecycler() {
        //infTarefasAdapter.notifyDataSetChanged();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderInfTarefas {

        private RecyclerView mViewRecyclerViewInfTarefas;
        private FloatingActionButton mViewFloatingActionButtonAddTarefa;
        private Button mViewBtnConcluirInfTarefa;
        private Button mViewBtnDefinirRegras;
    }
}
