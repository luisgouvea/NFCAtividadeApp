package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTarefas;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;

public class TarefasListViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView nomeTarefa;
    private TextView comentario;
    private TextView popMenu;

    /**
     * Construtor
     */
    public TarefasListViewHolder(View itemView) {
        super(itemView);
        this.nomeTarefa = (TextView) itemView.findViewById(R.id.text_title_nome_tarefa);
        this.comentario = (TextView) itemView.findViewById(R.id.text_subtitle_nome_tarefa);
        this.popMenu = (TextView) itemView.findViewById(R.id.txtOptionListTarefa);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Tarefa tarefa, final OnListClickInteractionListenerView listenerOptions ) {

        // Altera valor
        this.nomeTarefa.setText(tarefa.getNome());
        this.comentario.setText(tarefa.getComentario());


        popMenu.setId(tarefa.getId());
        // Adciona evento de click
        this.popMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Metodo (onClick) da interface OnListClickInteractionListenerView,  implementada nesse projeto
                 * Nesse caso, o View eh um TextView
                 */
                listenerOptions.onClick(view);
            }
        });
    }
}
