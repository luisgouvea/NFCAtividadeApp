package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listRegras;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;

public class TarefasListRegrasViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView descricaoTarefa;
    private TextView apelidoTarefa;

    /**
     * Construtor
     */
    public TarefasListRegrasViewHolder(View itemView) {
        super(itemView);
        this.descricaoTarefa = (TextView) itemView.findViewById(R.id.text_title_nome_tarefa_regra);
        this.apelidoTarefa = (TextView) itemView.findViewById(R.id.text_subtitle_nome_tarefa_regra);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final TAG tag, final OnListClickInteractionListener listener ) {

        // Altera valor
        this.descricaoTarefa.setText(tag.getNome());
        this.apelidoTarefa.setText(tag.getNome());

        // Adciona evento de click
        this.descricaoTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Metodo (onClick) da interface OnListClickInteractionListener,  implementada nesse projeto
                 */
                listener.onClick(tag.getId());
            }
        });

    }
}
