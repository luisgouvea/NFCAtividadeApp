package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;

import java.util.ArrayList;

public class PrecedenciaTarefaActivity extends AppCompatActivity {

    private int IdAtividade;
    private ArrayList<TAG> listTags = new ArrayList<>();
    private TAG tagTarget;
    private ViewHolderPrecedenciaTarefas mViewHolderPrecedenciaTarefas = new ViewHolderPrecedenciaTarefas();
    //private TarefasListRegrasAdapter tarefasListRegrasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precedencia_tarefa);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listTags = extras.getParcelableArrayList("listaTags");
            IdAtividade = extras.getInt("IdAtividade");
            tagTarget = extras.getParcelable("tagTarget");
        }
        this.mViewHolderPrecedenciaTarefas.mViewTextNameTagTarget = findViewById(R.id.name_tag_target);
        this.mViewHolderPrecedenciaTarefas.mViewTextNameTagTarget.setText(tagTarget.getNome());
        this.mViewHolderPrecedenciaTarefas.mViewTextNameTagTarget.setVisibility(View.VISIBLE);
    }

    /**
     Click no botao voltar da activity
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
        resultIntent.putExtra("listaTarefas", listTags);
        resultIntent.putExtra("IdAtividade", IdAtividade);
        setResult(Activity.RESULT_OK, resultIntent);
        startActivity(resultIntent);
        finish();
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderPrecedenciaTarefas {

        private TextView mViewTextNameTagTarget;
        private RecyclerView mViewRecyclerViewRegrasTarefas;
    }
}
