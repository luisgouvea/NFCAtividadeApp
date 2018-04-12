package com.fiquedeolho.nfcatividadeapp.recyclerView.infTarefas.listTags;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;

import java.util.List;

public class TarefasListTagViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView nomeTag;
    private final RadioButton radioButton;

    /**
     * Construtor
     */
    public TarefasListTagViewHolder(View itemView) {
        super(itemView);
        this.nomeTag = (TextView) itemView.findViewById(R.id.nome_tag);
        this.radioButton = (RadioButton) itemView.findViewById(R.id.radioButton_tag);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final TAG tag , final OnListClickInteractionListener listener ) {

        // Altera valor
        this.nomeTag.setText(tag.getNome());

        this.radioButton.setId(tag.getId());

        // Adciona evento de click
        this.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Metodo (onClick) da interface OnListClickInteractionListener,  implementada nesse projeto
                 */
                radioButton.setChecked(true);
                listener.onClick(tag.getId());
            }
        });

    }
}
