package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listPrecedencia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;

import java.util.List;

public class TarefasListPrecedenciaAdapter extends RecyclerView.Adapter<TarefasListPrecedenciaViewHolder> {

    private List<Tarefa> mListTarefas;

    // Interface que define as ações
    private OnListClickInteractionListenerView mOnListClickInteractionListener;

    private Tarefa mTarefaTarget;

    public TarefasListPrecedenciaAdapter(List<Tarefa> tarefas, Tarefa tarefaTarget  , OnListClickInteractionListenerView listener){
        this.mListTarefas = tarefas;
        this.mTarefaTarget = tarefaTarget;
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
        View tarefaView = inflater.inflate(R.layout.activity_row_tarefa_precedencia_list, parent, false);

        // Passa a ViewHolder
        return new TarefasListPrecedenciaViewHolder(tarefaView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(TarefasListPrecedenciaViewHolder holder, int position) {
        Tarefa tarefaDaList = this.mListTarefas.get(position);
        holder.bindData(tarefaDaList, mTarefaTarget, this.mOnListClickInteractionListener);
    }

    @Override
    public int getItemCount() {
        return this.mListTarefas.size();
    }
}
