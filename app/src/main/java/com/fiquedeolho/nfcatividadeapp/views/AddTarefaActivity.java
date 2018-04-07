package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import java.util.ArrayList;

public class AddTarefaActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolderAddTarefa mViewHolderAddTarefa = new ViewHolderAddTarefa();
    private int IdAtividade;
    private ArrayList<TAG> listTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
            listTags = extras.getParcelableArrayList("listaTarefas");
        }

        this.mViewHolderAddTarefa.mViewBtnInputNomeTarefa = findViewById(R.id.input_nomeTarefa);
        this.mViewHolderAddTarefa.mViewBtnSalvarTarefa = findViewById(R.id.btn_salvar_tarefa);
        this.mViewHolderAddTarefa.mViewBtnSalvarTarefa.setOnClickListener(this);
    }

    /**
     Click no botao voltar da activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToInfTarefas(null, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_salvar_tarefa){
            TAG tag  = new TAG();
            tag.setNome(this.mViewHolderAddTarefa.mViewBtnInputNomeTarefa.getText().toString());
            tag.setIdAtividade(IdAtividade);
            backToInfTarefas(tag, true);
        }
    }

    private void backToInfTarefas(TAG tag, Boolean criarTarefa) {
        Intent resultIntent = new Intent(this, InfTarefasActivity.class);
        if(tag != null) {
            listTags.add(tag);
        }
        resultIntent.putExtra("listaTarefas", listTags);
        resultIntent.putExtra("IdAtividade", IdAtividade);
        if(criarTarefa){
            resultIntent.putExtra("criarTarefa", true);
        }
        setResult(Activity.RESULT_OK, resultIntent);
        startActivity(resultIntent);
        finish();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderAddTarefa {

        private Button mViewBtnSalvarTarefa;
        private EditText mViewBtnInputNomeTarefa;
    }
}