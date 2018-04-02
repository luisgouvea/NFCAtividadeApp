package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;

import java.util.ArrayList;

public class AddTarefaActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolderAddTarefa mViewHolderAddTarefa = new ViewHolderAddTarefa();
    private int idAtividade;
    private ArrayList<TAG> listTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idAtividade = extras.getInt("IdAtividade");
        }

        this.mViewHolderAddTarefa.mViewBtnSalvarTarefa = findViewById(R.id.btn_salvar_tarefa);
        this.mViewHolderAddTarefa.mViewBtnSalvarTarefa.setOnClickListener(this);
        BackToInfTarefas();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_salvar_tarefa){
            //addTarefa();
        }
    }

    private void BackToInfTarefas() {
        Intent resultIntent = new Intent(this, InfTarefasActivity.class);
        // TODO Add extras or a data URI to this intent as appropriate.
        TAG tag = new TAG();
        tag.setNome("Teste tag");
        listTags.add(tag);
        resultIntent.putExtra("listaTarefas", listTags);
        resultIntent.putExtra("idAtividade", idAtividade);
        setResult(Activity.RESULT_OK, resultIntent);
        startActivity(resultIntent);
        finish();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderAddTarefa {

        private Button mViewBtnSalvarTarefa;
    }
}
