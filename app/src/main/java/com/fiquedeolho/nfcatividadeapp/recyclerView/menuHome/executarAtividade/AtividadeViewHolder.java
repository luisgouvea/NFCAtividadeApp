package com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.executarAtividade;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.OnListClickInteractionListenerOptionsList;


public class AtividadeViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView descricaoAtividade;
    private TextView statusAtividade;
    private TextView popMenu;


    /**
     * Construtor
     */
    public AtividadeViewHolder(View itemView) {
        super(itemView);
        this.descricaoAtividade = (TextView) itemView.findViewById(R.id.text_car_model);
        this.statusAtividade = (TextView) itemView.findViewById(R.id.text_view_details);
        this.popMenu = (TextView) itemView.findViewById(R.id.txtOptionDigit);
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
