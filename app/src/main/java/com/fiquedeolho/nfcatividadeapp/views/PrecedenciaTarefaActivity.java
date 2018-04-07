package com.fiquedeolho.nfcatividadeapp.views;

import android.app.Activity;
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
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TagRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listPrecedencia.TarefasListPrecedenciaAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class PrecedenciaTarefaActivity extends AppCompatActivity implements View.OnClickListener {

    private int IdAtividade;
    private ArrayList<TAG> listTags = new ArrayList<>();
    private TAG tagTarget;
    private ViewHolderPrecedenciaTarefas mViewHolderPrecedenciaTarefas = new ViewHolderPrecedenciaTarefas();
    private TarefasListPrecedenciaAdapter tarefasListPrecedenciaAdapter;

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

        this.mViewHolderPrecedenciaTarefas.mViewButtonSalvarPrecedencia = findViewById(R.id.btn_salvar_precedencia);
        this.mViewHolderPrecedenciaTarefas.mViewButtonSalvarPrecedencia.setOnClickListener(this);

        SetarRecyclerView();
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderPrecedenciaTarefas.mViewRecyclerViewPrecedenciaTarefas = findViewById(R.id.recyclerViewPrecedenciaTarefas);

        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {
                TAG tagClicada = getTagTarget(id); // TAG clicada
                int position = getPositionTagTarget(tagTarget.getId());
                ArrayList<TAG> listEncTagTarget = tagTarget.getListEncadeamento();
                if (!listEncTagTarget.contains(tagClicada)) {
                    listEncTagTarget.add(tagClicada);
                    tagTarget.setListEncadeamento(listEncTagTarget);
                    listTags.set(position, tagTarget);
                }
            }
        };

        ArrayList<TAG> listAux = new ArrayList<TAG>(listTags);
        listAux = removeTagTarget(listAux);
        // 2 - Definir adapter passando listagem de tarefas e listener
        tarefasListPrecedenciaAdapter = new TarefasListPrecedenciaAdapter(listAux, tagTarget, listener);
        this.mViewHolderPrecedenciaTarefas.mViewRecyclerViewPrecedenciaTarefas.setAdapter(tarefasListPrecedenciaAdapter);

        this.mViewHolderPrecedenciaTarefas.mViewRecyclerViewPrecedenciaTarefas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderPrecedenciaTarefas.mViewRecyclerViewPrecedenciaTarefas.setLayoutManager(linearLayoutManager);
    }

    private ArrayList<TAG> removeTagTarget(ArrayList<TAG> list) {
        for (int i = 0; i < list.size(); i++) {
            TAG tagList = list.get(i);
            if (tagList.getId() == tagTarget.getId()) {
                list.remove(i);
                return list;
            }
        }
        return null;
    }

    private TAG getTagTarget(int idTag) {
        for (int i = 0; i < listTags.size(); i++) {
            TAG tag = listTags.get(i);
            if (tag.getId() == idTag) {
                return tag;
            }
        }
        return null;
    }

    private int getPositionTagTarget(int idTag) {
        for (int i = 0; i < listTags.size(); i++) {
            TAG tag = listTags.get(i);
            if (tag.getId() == idTag) {
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
        resultIntent.putExtra("listaTarefas", listTags);
        resultIntent.putExtra("IdAtividade", IdAtividade);
        setResult(Activity.RESULT_OK, resultIntent);
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
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.setCancelable(false);
        pDialog.show();
        TagRetrofit tagInterface = BaseUrlRetrofit.retrofit.create(TagRetrofit.class);
        final Call<Boolean> call = tagInterface.setarEncadeamentoTag(tagTarget);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                Boolean result = response.body();
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
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

        private TextView mViewTextNameTagTarget;
        private RecyclerView mViewRecyclerViewPrecedenciaTarefas;
        private Button mViewButtonSalvarPrecedencia;
    }
}
