package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listRegras;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;

import java.util.List;

public class TarefasListRegrasAdapter extends RecyclerView.Adapter<TarefasListRegrasViewHolder> {

    private List<Tarefa> mListTarefa;

    // Interface que define as ações
    private OnListClickInteractionListenerView mOnListClickInteractionListenerOptions;

    public TarefasListRegrasAdapter(List<Tarefa> tarefas, OnListClickInteractionListenerView listenerOptions){
        this.mListTarefa = tarefas;
        this.mOnListClickInteractionListenerOptions = listenerOptions;
    }

    /**
     * Responsável pela criação de linha
     */
    @Override
    public TarefasListRegrasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View tarefaView = inflater.inflate(R.layout.activity_row_tarefa_regras_list, parent, false);

        // Passa a ViewHolder
        return new TarefasListRegrasViewHolder(tarefaView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(TarefasListRegrasViewHolder holder, int position) {
        Tarefa tarefa = this.mListTarefa.get(position);
        holder.bindData(tarefa, this.mOnListClickInteractionListenerOptions);
    }

    @Override
    public int getItemCount() {
        return this.mListTarefa.size();
    }
}
