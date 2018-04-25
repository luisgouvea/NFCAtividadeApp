package com.fiquedeolho.nfcatividadeapp.recyclerView.infCheckNFC;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TarefaCheck;

public class RegistroCheckListViewHolder extends RecyclerView.ViewHolder{

    // Elemento de interface
    private TextView nomeTarefa;

    /**
     * Construtor
     */
    public RegistroCheckListViewHolder(View itemView) {
        super(itemView);
        this.nomeTarefa = itemView.findViewById(R.id.text_nome_tarefa_check);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final TarefaCheck ativTarefaHistoricoCheck) {

        // Altera valor
        this.nomeTarefa.setText(ativTarefaHistoricoCheck.getNome());
    }
}
