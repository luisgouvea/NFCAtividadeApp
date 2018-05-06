package com.fiquedeolho.nfcatividadeapp.recyclerView.infTags;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TAG;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;

public class TagListViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView nomeTag;
    private TextView popMenu;

    /**
     * Construtor
     */
    public TagListViewHolder(View itemView) {
        super(itemView);
        this.nomeTag = (TextView) itemView.findViewById(R.id.text_title_nome_tag);
        this.popMenu = (TextView) itemView.findViewById(R.id.txtOptionListTag);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final TAG tag, final OnListClickInteractionListenerView listenerOptions ) {

        // Altera valor
        this.nomeTag.setText(tag.getNome());


        //popMenu.setId(tag.getId());

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
