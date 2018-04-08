package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listPrecedencia;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;

import java.util.ArrayList;

public class TarefasListPrecedenciaViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView nomeTarefa;
    private TextView palavraChave;
    private CheckBox antecedeTagTarget;

    /**
     * Construtor
     */
    public TarefasListPrecedenciaViewHolder(View itemView) {
        super(itemView);
        this.nomeTarefa = (TextView) itemView.findViewById(R.id.text_title_nome_tarefa_precedencia);
        this.palavraChave = (TextView) itemView.findViewById(R.id.text_subtitle_nome_tarefa_precedencia);
        this.antecedeTagTarget = (CheckBox) itemView.findViewById(R.id.checkbox_tarefa_precedencia);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final TAG tagDaLista, TAG tagTarget, final OnListClickInteractionListenerView listener) {

        // Altera valor
        this.nomeTarefa.setText(tagDaLista.getNome());
        this.palavraChave.setText(tagDaLista.getPalavraChave());

        this.antecedeTagTarget.setId(tagDaLista.getId());
        // Adciona evento de click
        this.antecedeTagTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Metodo (onClick) da interface OnListClickInteractionListener,  implementada nesse projeto
                 */
                listener.onClick(view);
            }
        });

        ArrayList<TAG> listAntecessoras = tagTarget.getListEncadeamento();
        if (listAntecessoras != null && listAntecessoras.size() > 0) {
            for (TAG tag : listAntecessoras) {
                if (tagDaLista.getId() == tag.getId()) {
                    this.antecedeTagTarget.setChecked(true);
                    break;
                }
            }
        }

    }
}
