package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TagRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listRegras.TarefasListRegrasAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class RegrasTarefasActivity extends AppCompatActivity {

    private int IdAtividade;
    private ArrayList<TAG> listTags = new ArrayList<>();
    private ViewHolderRegrasTarefas mViewHolderRegrasTarefas = new ViewHolderRegrasTarefas();
    private TarefasListRegrasAdapter tarefasListRegrasAdapter;
    private ProgressDialog pDialog;

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
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                SetarRecyclerView();
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
        resultIntent.putExtra("IdAtividade", IdAtividade);
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