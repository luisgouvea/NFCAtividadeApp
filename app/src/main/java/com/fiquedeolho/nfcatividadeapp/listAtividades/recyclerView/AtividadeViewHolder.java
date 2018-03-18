package com.fiquedeolho.nfcatividadeapp.listAtividades.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;

/**
 * Created by Luis Eduardo on 18/03/2018.
 */

public class AtividadeViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView descricaoAtividade;
    private TextView statusAtividade;


    /**
     * Construtor
     */
    public AtividadeViewHolder(View itemView) {
        super(itemView);
        this.descricaoAtividade = (TextView) itemView.findViewById(R.id.text_car_model);
        this.statusAtividade = (TextView) itemView.findViewById(R.id.text_view_details);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Atividade atividade, final OnListClickInteractionListener listener) {

        // Altera valor
        this.descricaoAtividade.setText(atividade.getNome());
        this.statusAtividade.setText(atividade.getNome());


        // Adciona evento de click
        this.descricaoAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(atividade.getId());
            }
        });
    }
}
