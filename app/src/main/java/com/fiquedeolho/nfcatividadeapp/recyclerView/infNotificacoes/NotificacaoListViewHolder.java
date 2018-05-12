package com.fiquedeolho.nfcatividadeapp.recyclerView.infNotificacoes;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuario;
import com.fiquedeolho.nfcatividadeapp.util.Convert;

import org.w3c.dom.Text;

public class NotificacaoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Elemento de interface
    private LinearLayout container_row;
    private TextView descricaoNotificacao;
    private TextView dataNotificacao;
    private ClickListener listener;
    private TextView notificacaoVisualizada;
    private TextView notificacaoNaoVisualizada;

    /**
     * Construtor
     */
    public NotificacaoListViewHolder(View itemView) {
        super(itemView);
        this.descricaoNotificacao = itemView.findViewById(R.id.text_descricao_notificacao);
        this.container_row = itemView.findViewById(R.id.container_row_notificacao);
        this.dataNotificacao = itemView.findViewById(R.id.text_data_notificacao);
        this.notificacaoNaoVisualizada = itemView.findViewById(R.id.text_status_visualizada_nao);
        this.notificacaoVisualizada = itemView.findViewById(R.id.text_status_visualizada_sim);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final NotificacaoUsuario notificacaoUsuario , ClickListener listener ) {
        this.listener = listener;

        this.descricaoNotificacao.setText(Html.fromHtml(notificacaoUsuario.getDescricaoNotificacao()));
        this.dataNotificacao.setText(Convert.formatDate(notificacaoUsuario.getDataNotificacao()));
        this.container_row.setOnClickListener(this);
        if(notificacaoUsuario.getVisualizada()){
            this.notificacaoVisualizada.setVisibility(View.VISIBLE);
        }else{
            this.notificacaoNaoVisualizada.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        listener.containerRowClicked(v, getAdapterPosition());
    }

    public interface ClickListener {
        void containerRowClicked(View v, int position);
    }
}
