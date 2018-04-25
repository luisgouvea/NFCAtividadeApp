package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listPrecedencia;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Tarefa;
import com.fiquedeolho.nfcatividadeapp.models.TarefaPrecedente;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;

import java.util.ArrayList;

public class TarefasListPrecedenciaViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView nomeTarefa;
    private TextView comentario;
    private CheckBox antecedeTarefaTarget;

    /**
     * Construtor
     */
    public TarefasListPrecedenciaViewHolder(View itemView) {
        super(itemView);
        this.nomeTarefa = (TextView) itemView.findViewById(R.id.text_title_nome_tarefa_precedencia);
        this.comentario = (TextView) itemView.findViewById(R.id.text_subtitle_nome_tarefa_precedencia);
        this.antecedeTarefaTarget = (CheckBox) itemView.findViewById(R.id.checkbox_tarefa_precedencia);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Tarefa tarefaDaLista, Tarefa tarefaTarget, final OnListClickInteractionListenerView listener) {

        // Altera valor
        this.nomeTarefa.setText(tarefaDaLista.getNome());
        this.comentario.setText(tarefaDaLista.getComentario());

        this.antecedeTarefaTarget.setId(tarefaDaLista.getId());
        // Adciona evento de click
        this.antecedeTarefaTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Metodo (onClick) da interface OnListClickInteractionListener,  implementada nesse projeto
                 */
                listener.onClick(view);
            }
        });

        ArrayList<TarefaPrecedente> listAntecessoras = tarefaTarget.getListAntecessoras();
        if (listAntecessoras != null && listAntecessoras.size() > 0) {
            for (TarefaPrecedente tarefaAntecessora : listAntecessoras) {
                if (tarefaDaLista.getId() == tarefaAntecessora.getIdTarefaAntecessora()) {
                    this.antecedeTarefaTarget.setChecked(true);
                    break;
                }
            }
        }

    }
}
