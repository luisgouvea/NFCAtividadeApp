package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listPrecedencia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;

import java.util.List;

public class TarefasListPrecedenciaAdapter extends RecyclerView.Adapter<TarefasListPrecedenciaViewHolder> {

    private List<TAG> mListTags;

    // Interface que define as ações
    private OnListClickInteractionListenerView mOnListClickInteractionListener;

    private TAG mTagTarget;

    public TarefasListPrecedenciaAdapter(List<TAG> tags, TAG tagTarget  , OnListClickInteractionListenerView listener){
        this.mListTags = tags;
        this.mTagTarget = tagTarget;
        this.mOnListClickInteractionListener = listener;
    }

    /**
     * Responsável pela criação de linha
     */
    @Override
    public TarefasListPrecedenciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View tagView = inflater.inflate(R.layout.activity_row_tarefa_precedencia_list, parent, false);

        // Passa a ViewHolder
        return new TarefasListPrecedenciaViewHolder(tagView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(TarefasListPrecedenciaViewHolder holder, int position) {
        TAG tagDaList = this.mListTags.get(position);
        holder.bindData(tagDaList, mTagTarget , this.mOnListClickInteractionListener);
    }

    @Override
    public int getItemCount() {
        return this.mListTags.size();
    }
}
