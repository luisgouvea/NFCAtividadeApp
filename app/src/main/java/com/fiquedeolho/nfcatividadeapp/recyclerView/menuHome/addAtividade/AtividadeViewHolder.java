package com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.addAtividade;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerOptionsList;

public class AtividadeViewHolder extends RecyclerView.ViewHolder{

    // Elemento de interface
    private TextView descricaoAtividade;
    private TextView statusAtividade;
    private TextView popMenu;


    /**
     * Construtor
     */
    public AtividadeViewHolder(View itemView) {
        super(itemView);
        this.descricaoAtividade = (TextView) itemView.findViewById(R.id.text_title_nome_ativ);
        this.statusAtividade = (TextView) itemView.findViewById(R.id.text_subtitle_nome_ativ);
        this.popMenu = (TextView) itemView.findViewById(R.id.txtOptionAtivAdd);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Atividade atividade, final OnListClickInteractionListener listener, final OnListClickInteractionListenerOptionsList listenerOptions ) {

        // Altera valor
        this.descricaoAtividade.setText(atividade.getNome());
        this.statusAtividade.setText(atividade.getNome());


        // Adciona evento de click
        this.descricaoAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Metodo (onClick) da interface OnListClickInteractionListener,  implementada nesse projeto
                 */
                listener.onClick(atividade.getId());
            }
        });

        popMenu.setId(atividade.getId());
        this.popMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Metodo (onClick) da interface OnListClickInteractionListenerOptionsList,  implementada nesse projeto
                 * Nesse caso, o View eh um TextView
                 */
                listenerOptions.onClick(view);
            }
        });
    }
}
