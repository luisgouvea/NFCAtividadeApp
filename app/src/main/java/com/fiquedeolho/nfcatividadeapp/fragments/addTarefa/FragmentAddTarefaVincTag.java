package com.fiquedeolho.nfcatividadeapp.fragments.addTarefa;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.BaseUrlRetrofit;
import com.fiquedeolho.nfcatividadeapp.interfaces.webAPIService.TagRetrofit;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTags.TarefasListTagAdapter;
import com.fiquedeolho.nfcatividadeapp.util.KeysSharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentAddTarefaVincTag  extends Fragment{

    private int idTagVinculada;
    private TarefasListTagAdapter tarefasListTagAdapter;
    private ViewHolderVincTag mViewHolderVincTag;
    private ArrayList<TAG> listaTags;
    private ProgressDialog pDialog;
    private ActivityCommunicator activityCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_add_tarefa_list_tag, container, false);
        return view;
    }

    private void SetarRecyclerView() {

        // 1 - Obter a recyclerview
        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag = getActivity().findViewById(R.id.recyclerViewAddTarefaVincTag);

        // Implementa o evento de click para passar por parâmetro para a ViewHolder
        OnListClickInteractionListener listener = new OnListClickInteractionListener() {
            @Override
            public void onClick(int id) {
                if (idTagVinculada != 0) {
                    RecyclerView re = mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag;
                    RadioButton radio = re.findViewById(idTagVinculada);
                    radio.setChecked(false);
                }
                TAG tag = getTagTarget(id);
                idTagVinculada = tag.getId();

                activityCommunicator.passDataToActivity(idTagVinculada);
            }
        };

        // 2 - Definir adapter passando listagem de tarefas e listener
        tarefasListTagAdapter = new TarefasListTagAdapter(listaTags, listener);
        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.setAdapter(tarefasListTagAdapter);

        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));

        // 3 - Definir um layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        this.mViewHolderVincTag.mViewRecyclerViewAddTarefaVincTag.setLayoutManager(linearLayoutManager);
    }

    private void ListAllTagsAddTarefaVincTag() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        //pDialog.setTitle(getString(R.string.title_progress_tarefa_list));
        pDialog.setMessage(getString(R.string.message_progress_dialog));
        pDialog.show();
        TagRetrofit tagInterface = BaseUrlRetrofit.retrofit.create(TagRetrofit.class);
        SavePreferences save = new SavePreferences(getActivity());

        final Call<ArrayList<TAG>> call = tagInterface.getTagsByIdUsuario(save.getSavedInt(KeysSharedPreference.ID_USUARIO_LOGADO));

        call.enqueue(new Callback<ArrayList<TAG>>() {
            @Override
            public void onResponse(Call<ArrayList<TAG>> call, retrofit2.Response<ArrayList<TAG>> response) {
                listaTags = response.body();

                if (listaTags == null || listaTags.size() == 0) {
                    mViewHolderVincTag.mViewTextListTagVaziaAddTarefa = getActivity().findViewById(R.id.textListTagVaziaAddTarefa);
                    mViewHolderVincTag.mViewTextListTagVaziaAddTarefa.setVisibility(View.VISIBLE);
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

    private TAG getTagTarget(int idTag) {
        for (int i = 0; i < listaTags.size(); i++) {
            TAG tag = listaTags.get(i);
            if (tag.getId() == idTag) {
                return tag;
            }
        }
        return null;
    }

    private class ViewHolderVincTag {

        private RecyclerView mViewRecyclerViewAddTarefaVincTag;
        private TextView mViewTextListTagVaziaAddTarefa;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityCommunicator =(ActivityCommunicator)context;
    }
}
