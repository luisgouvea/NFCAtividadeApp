package com.fiquedeolho.nfcatividadeapp.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.SharedPreferences.SavePreferences;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TagRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTags.TagListAdapter;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class InfTagsActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolderInfTags mViewHolderInfTags = new ViewHolderInfTags();
    private ArrayList<TAG> listTags = new ArrayList<>();
    private TagListAdapter tagsListAdapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_tags);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /**
         * Pegando os elementos da Activity
         */
        this.mViewHolderInfTags.mViewFloatingActionButtonAddTag = findViewById(R.id.btn_addFloatingAction_add_tarefa);
        this.mViewHolderInfTags.mViewTextListTagVaziaInfTags = findViewById(R.id.textListTarefaVaziaInfTarefas);

        /**
         * Comportamento dos botoes
         */
        this.mViewHolderInfTags.mViewFloatingActionButtonAddTag.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getListTags();
    }

    private void getListTags() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setTitle(getString(R.string.title_progress_tag_list));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TagRetrofit tagInterface = BaseUrlRetrofit.retrofit.create(TagRetrofit.class);
        SavePreferences save = new SavePreferences(this);
        int idUsuario = save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO);
        final Call<ArrayList<TAG>> call = tagInterface.getTagsByIdUsuario(idUsuario);
        call.enqueue(new Callback<ArrayList<TAG>>() {
            @Override
            public void onResponse(Call<ArrayList<TAG>> call, retrofit2.Response<ArrayList<TAG>> response) {
                listTags = response.body();
                if (listTags == null || listTags.size() == 0) {
                    mViewHolderInfTags.mViewTextListTagVaziaInfTags.setVisibility(View.VISIBLE);
                } else {
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
        this.mViewHolderInfTags.mViewRecyclerViewInfTags = findViewById(R.id.recyclerViewInfTags);

        this.mViewHolderInfTags.mViewRecyclerViewInfTags.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mViewHolderInfTags.mViewFloatingActionButtonAddTag.getVisibility() == View.VISIBLE) {
                    mViewHolderInfTags.mViewFloatingActionButtonAddTag.hide();
                } else if (dy < 0 && mViewHolderInfTags.mViewFloatingActionButtonAddTag.getVisibility() != View.VISIBLE) {
                    mViewHolderInfTags.mViewFloatingActionButtonAddTag.show();
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
                final int idTarefa = viewTarget.getId();
                PopupMenu popupMenu = new PopupMenu(viewTarget.getContext(), viewTarget);
                popupMenu.inflate(R.menu.options_list_tag);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnu_deletar_tag:
                                Toast.makeText(getApplicationContext(), "Deletado", Toast.LENGTH_LONG).show();
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
        tagsListAdapter = new TagListAdapter(listTags, listenerOptionsList);
        this.mViewHolderInfTags.mViewRecyclerViewInfTags.setAdapter(tagsListAdapter);

        this.mViewHolderInfTags.mViewRecyclerViewInfTags.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.mViewHolderInfTags.mViewRecyclerViewInfTags.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_addFloatingAction_add_tag) {

            /*Bundle bundle = new Bundle();
            bundle.putInt("IdAtividade", IdAtividade);
            Intent intent = new Intent(this, AddTarefaActivity.class);
            intent.putExtras(bundle);

            startActivity(intent);*/
            //finish();
        }
    }

    /**
     * ViewHolder dos elementos
     */
    private class ViewHolderInfTags {

        private RecyclerView mViewRecyclerViewInfTags;
        private FloatingActionButton mViewFloatingActionButtonAddTag;
        private TextView mViewTextListTagVaziaInfTags;
    }
}
