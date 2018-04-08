package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTarefas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;

import java.util.List;

public class TarefasListAdapter extends RecyclerView.Adapter<TarefasListViewHolder> {

    private List<TAG> mListTags;

    // Interface que define as ações
    private OnListClickInteractionListenerView mOnListOptionListener;

    public TarefasListAdapter(List<TAG> tags, OnListClickInteractionListenerView listOptions){
        this.mListTags = tags;
        this.mOnListOptionListener = listOptions;
    }
    /**
     * Responsável pela criação de linha
     */
    @Override
    public TarefasListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View tagView = inflater.inflate(R.layout.activity_row_tarefa_list, parent, false);

        // Passa a ViewHolder
        return new TarefasListViewHolder(tagView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(TarefasListViewHolder holder, int position) {
        TAG tag = this.mListTags.get(position);
        holder.bindData(tag, this.mOnListOptionListener);
    }

    @Override
    public int getItemCount() {
        return this.mListTags.size();
    }
}
