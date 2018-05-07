package com.fiquedeolho.nfcatividadeapp.recyclerView.infNotificacoes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuario;

import org.w3c.dom.Text;

public class NotificacaoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Elemento de interface
    private LinearLayout container_row;
    private TextView descricaoNotificacao;
    private ClickListener listener;

    /**
     * Construtor
     */
    public NotificacaoListViewHolder(View itemView) {
        super(itemView);
        this.descricaoNotificacao = itemView.findViewById(R.id.text_descricao_notificacao);
        this.container_row = itemView.findViewById(R.id.container_row_notificacao);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final NotificacaoUsuario notificacaoUsuario , ClickListener listener ) {
        this.listener = listener;
        // Altera valor
        this.descricaoNotificacao.setText(notificacaoUsuario.getDescricaoNotificacao());
        this.container_row.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.containerRowClicked(v, getAdapterPosition());
    }

    public interface ClickListener {
        void containerRowClicked(View v, int position);
    }
}
