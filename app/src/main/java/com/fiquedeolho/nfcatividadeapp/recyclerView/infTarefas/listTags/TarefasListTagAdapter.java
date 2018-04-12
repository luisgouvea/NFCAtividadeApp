package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTags;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;

import java.util.List;

public class TarefasListTagAdapter extends RecyclerView.Adapter<TarefasListTagViewHolder>{

    private List<TAG> mListTag;

    // Interface que define as ações
    private OnListClickInteractionListener mOnListClickInteractionListener;

    public TarefasListTagAdapter(List<TAG> tags, OnListClickInteractionListener listener){
        this.mListTag = tags;
        this.mOnListClickInteractionListener = listener;
    }

    /**
     * Responsável pela criação de linha
     */
    @Override
    public TarefasListTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View tagView = inflater.inflate(R.layout.activity_row_add_tarefa_vinc_tag, parent, false);

        // Passa a ViewHolder
        return new TarefasListTagViewHolder(tagView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(TarefasListTagViewHolder holder, int position) {
        TAG tag = this.mListTag.get(position);
        holder.bindData(tag, this.mOnListClickInteractionListener);
    }

    @Override
    public int getItemCount() {
        return this.mListTag.size();
    }

}
