package com.fiquedeolho.nfcatividadeapp.recyclerView.infCheckNFC;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TarefaCheck;

import java.util.List;

public class RegistroCheckListAdapter extends RecyclerView.Adapter<RegistroCheckListViewHolder> {

    private List<TarefaCheck> mListAtividadeTarefaCheck;

    public RegistroCheckListAdapter(List<TarefaCheck> checks){
        this.mListAtividadeTarefaCheck = checks;
    }
    /**
     * Responsável pela criação de linha
     */
    @Override
    public RegistroCheckListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View tagView = inflater.inflate(R.layout.activity_row_tarefa_check_list, parent, false);

        // Passa a ViewHolder
        return new RegistroCheckListViewHolder(tagView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(RegistroCheckListViewHolder holder, int position) {
        TarefaCheck ativTarefaCheck = this.mListAtividadeTarefaCheck.get(position);
        holder.bindData(ativTarefaCheck);
    }

    @Override
    public int getItemCount() {
        return this.mListAtividadeTarefaCheck.size();
    }
}
