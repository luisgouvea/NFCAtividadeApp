package com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.executarAtividade;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;


public class AtividadeViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView nomeAtividade;
    private TextView descricaoAtividade;
    private TextView popMenu;


    /**
     * Construtor
     */
    public AtividadeViewHolder(View itemView) {
        super(itemView);
        this.nomeAtividade = (TextView) itemView.findViewById(R.id.text_car_model);
        this.descricaoAtividade = (TextView) itemView.findViewById(R.id.text_view_details);
        this.popMenu = (TextView) itemView.findViewById(R.id.txtOptionDigit);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Atividade atividade, final OnListClickInteractionListener listener, final OnListClickInteractionListenerView listenerOptions ) {

        // Altera valor
        this.nomeAtividade.setText(atividade.getNome());
        this.descricaoAtividade.setText(atividade.getDescricao());


        // Adciona evento de click
        this.nomeAtividade.setOnClickListener(new View.OnClickListener() {
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
                 * Metodo (onClick) da interface OnListClickInteractionListenerView,  implementada nesse projeto
                 * Nesse caso, o View eh um TextView
                 */
                listenerOptions.onClick(view);
            }
        });
    }
}
