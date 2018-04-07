package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listRegras.TarefasListRegrasAdapter;

import java.util.ArrayList;

public class RegrasTarefasActivity extends AppCompatActivity {

    private int IdAtividade;
    private ArrayList<TAG> listTags = new ArrayList<>();
    private ViewHolderRegrasTarefas mViewHolderRegrasTarefas = new ViewHolderRegrasTarefas();
    private TarefasListRegrasAdapter tarefasListRegrasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regras_tarefas);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IdAtividade = extras.getInt("IdAtividade");
            listTags = extras.getParcelableArrayList("listaTarefas");
        }
        SetarRecyclerView();
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
        Intent resultIntent = new Intent(this, InfTarefasActivity.class);
        resultIntent.putExtra("listaTarefas", listTags);
        resultIntent.putExtra("IdAtividade", IdAtividade);
        resultIntent.putExtra("backActivityRegras", true);
        setResult(Activity.RESULT_OK, resultIntent);
        startActivity(resultIntent);
        finish();
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderRegrasTarefas.mViewRecyclerViewRegrasTarefas = findViewById(R.id.recyclerViewRegrasTarefas);

        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {
                TAG tag = getTagTarget(id);
                Bundle bundle = new Bundle();
                bundle.putParcelable("tagTarget", tag);
                bundle.putInt("IdAtividade", IdAtividade);
                bundle.putParcelableArrayList("listaTags", listTags);

                Intent intent = new Intent(getApplicationContext(), PrecedenciaTarefaActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        };

        // 2 - Definir adapter passando listagem de tarefas e listener
        tarefasListRegrasAdapter = new TarefasListRegrasAdapter(listTags, listener);
        this.mViewHolderRegrasTarefas.mViewRecyclerViewRegrasTarefas.setAdapter(tarefasListRegrasAdapter);

        this.mViewHolderRegrasTarefas.mViewRecyclerViewRegrasTarefas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderRegrasTarefas.mViewRecyclerViewRegrasTarefas.setLayoutManager(linearLayoutManager);
    }


    private TAG getTagTarget(int idTag){
        for(int i = 0; i< listTags.size(); i++){
            TAG tag = listTags.get(i);
            if(tag.getId() == idTag){
                return tag;
            }
        }
        return null;
    }
    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderRegrasTarefas {

        private RecyclerView mViewRecyclerViewRegrasTarefas;
    }
}