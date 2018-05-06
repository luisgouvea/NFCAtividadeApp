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

public class TarefasListTagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Elemento de interface
    private TextView nomeTag;
    private final RadioButton radioButton;
    private ClickListener listener;

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
    public void bindData(final TAG tag , ClickListener listener ) {
        this.listener = listener;
        // Altera valor
        this.nomeTag.setText(tag.getNome());
        this.radioButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        radioButton.setChecked(true);
        listener.radioClicked(v, getAdapterPosition());
    }

    public interface ClickListener {
        void radioClicked(View v, int position);
    }
}
