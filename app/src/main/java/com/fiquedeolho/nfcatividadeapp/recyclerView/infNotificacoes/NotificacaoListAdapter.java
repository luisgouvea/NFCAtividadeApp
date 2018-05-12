package com.fiquedeolho.nfcatividadeapp.recyclerView.infNotificacoes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.NotificacaoUsuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class NotificacaoListAdapter extends RecyclerView.Adapter<NotificacaoListViewHolder> {

    private List<Object> mListNotificacao;

    // Interface que define as ações
    private NotificacaoListViewHolder.ClickListener mOnListListener;

    public NotificacaoListAdapter(List<Object> notificacaoes, NotificacaoListViewHolder.ClickListener listOptions){
        this.mListNotificacao = notificacaoes;
        this.mOnListListener = listOptions;
    }
    /**
     * Responsável pela criação de linha
     */
    @Override
    public NotificacaoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View tagView = inflater.inflate(R.layout.activity_row_notificacao_list, parent, false);

        // Passa a ViewHolder
        return new NotificacaoListViewHolder(tagView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(NotificacaoListViewHolder holder, int position) {
        Object objNotificacao = this.mListNotificacao.get(position);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = gson.toJson(objNotificacao);
        NotificacaoUsuario notificacaoUsuario = gson.fromJson(json, NotificacaoUsuario.class);
        holder.bindData(notificacaoUsuario, this.mOnListListener);
    }

    @Override
    public int getItemCount() {
        return this.mListNotificacao.size();
    }
}
