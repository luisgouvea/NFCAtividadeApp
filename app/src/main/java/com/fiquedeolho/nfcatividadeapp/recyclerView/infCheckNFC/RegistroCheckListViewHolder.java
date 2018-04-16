package com.fiquedeolho.nfcatividadeapp.recyclerView.infCheckNFC;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.AtividadeTarefaCheck;

public class RegistroCheckListViewHolder extends RecyclerView.ViewHolder{

    // Elemento de interface
    private TextView nomeTarefa;

    /**
     * Construtor
     */
    public RegistroCheckListViewHolder(View itemView) {
        super(itemView);
        this.nomeTarefa = itemView.findViewById(R.id.text_title_nome_tag);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final AtividadeTarefaCheck ativTarefaCheck) {

        // Altera valor
        this.nomeTarefa.setText(ativTarefaCheck.getNomeTarefa());
    }
}
