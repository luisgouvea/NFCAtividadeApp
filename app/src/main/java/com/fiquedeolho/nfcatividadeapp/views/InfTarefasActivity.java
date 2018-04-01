package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.fiquedeolho.nfcatividadeapp.R;

public class InfTarefasActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolderInfTarefas mViewHolderInfTarefas = new ViewHolderInfTarefas();
    private int IdAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_tarefas);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
        }

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
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_definir_regras_tarefas) {

        } else if (id == R.id.btn_concluir_inf_tarefas) {

        } else if (id == R.id.btn_addFloatingAction_add_tarefa) {

            Bundle bundle = new Bundle();
            bundle.putInt("IdAtividade", IdAtividade);

            Intent intent = new Intent(this, AddTarefaActivity.class);
            intent.putExtras(bundle);

            startActivity(intent);

        }
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
