package com.fiquedeolho.nfcatividadeapp.fragments.addTarefa;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.SharedPreferences.SavePreferences;
import com.fiquedeolho.nfcatividadeapp.interfaces.communicationActivity.ActivityCommunicator;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTags.TarefasListTagViewHolder;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.retrofit.interfaces.TagRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTags.TarefasListTagAdapter;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;
import com.fiquedeolho.nfcatividadeapp.views.AddTagActivity;
import com.fiquedeolho.nfcatividadeapp.views.AddTarefaActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentAddTarefaVincTag  extends Fragment implements View.OnClickListener, TarefasListTagViewHolder.ClickListener {

    private String identificacaoTagVinculada;
    private TarefasListTagAdapter tarefasListTagAdapter;
    private ViewHolderVincTag mViewHolderVincTag = new ViewHolderVincTag();
    private ArrayList<TAG> listaTags;
    private ProgressDialog pDialog;
    private ActivityCommunicator activityCommunicator;
    private TarefasListTagViewHolder.ClickListener listener = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_add_tarefa_list_tag, container, false);
        this.mViewHolderVincTag.mViewBtnFloatingActionAddTag = view.findViewById(R.id.btn_addFloatingAction_add_tarefa_add_tag);
        this.mViewHolderVincTag.mViewTextListTagVaziaAddTarefa = view.findViewById(R.id.textListTagVaziaAddTarefa);
        this.mViewHolderVincTag.mViewBtnFloatingActionAddTag.setOnClickListener(this);
        return view;
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag = getActivity().findViewById(R.id.recyclerViewAddTarefaVincTag);

        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mViewHolderVincTag.mViewBtnFloatingActionAddTag.getVisibility() == View.VISIBLE) {
                    mViewHolderVincTag.mViewBtnFloatingActionAddTag.hide();
                } else if (dy < 0 && mViewHolderVincTag.mViewBtnFloatingActionAddTag.getVisibility() != View.VISIBLE) {
                    mViewHolderVincTag.mViewBtnFloatingActionAddTag.show();
                }
            }
        });

        // 2 - Definir adapter passando listagem de tarefas e listener
        tarefasListTagAdapter = new TarefasListTagAdapter(listaTags, listener);
        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.setAdapter(tarefasListTagAdapter);

        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.setLayoutManager(linearLayoutManager);
    }

    public void ListAllTagsAddTarefaVincTag(AddTarefaActivity addTarefaActivity) {
        pDialog = new ProgressDialog(addTarefaActivity);
        pDialog.setCancelable(false);
        //pDialog.setMessage(getString(R.string.message_progress_dialog)); ISSO DA ERRO
        pDialog.setMessage("Aguarde...");
        pDialog.show();
        TagRetrofit tagInterface = BaseUrlRetrofit.retrofit.create(TagRetrofit.class);
        SavePreferences save = new SavePreferences(addTarefaActivity);

        final Call<ArrayList<TAG>> call = tagInterface.getTagsByIdUsuario(save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO));

        call.enqueue(new Callback<ArrayList<TAG>>() {
            @Override
            public void onResponse(Call<ArrayList<TAG>> call, retrofit2.Response<ArrayList<TAG>> response) {
                listaTags = response.body();
                if (listaTags == null || listaTags.size() == 0) {
                    mViewHolderVincTag.mViewTextListTagVaziaAddTarefa.setVisibility(View.VISIBLE);
                } else {
                    mViewHolderVincTag.mViewTextListTagVaziaAddTarefa.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_addFloatingAction_add_tarefa_add_tag){
            Intent intent = new Intent(getActivity(), AddTagActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void radioClicked(View v, int position) {
        TAG tag = listaTags.get(position);
        if (identificacaoTagVinculada != null) {
            RadioButton radio = (RadioButton) v;
            radio.setChecked(false);
        }
        identificacaoTagVinculada = tag.getIdentificadorTag();
        activityCommunicator.passDataToActivity(identificacaoTagVinculada);
    }

    private class ViewHolderVincTag {

        private RecyclerView mViewRecyclerViewAddTarefaVincTag;
        private TextView mViewTextListTagVaziaAddTarefa;
        private FloatingActionButton mViewBtnFloatingActionAddTag;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityCommunicator =(ActivityCommunicator)context;
    }
}
