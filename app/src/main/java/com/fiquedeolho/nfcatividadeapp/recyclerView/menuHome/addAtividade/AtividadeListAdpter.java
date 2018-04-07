package com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.addAtividade;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerOptionsList;

import java.util.List;

public class AtividadeListAdpter extends RecyclerView.Adapter<AtividadeViewHolder> {

    // Lista de atividades
    private List<Atividade> mListAtividades;

    // Interface que define as ações
    private OnListClickInteractionListener mOnListClickInteractionListener;

    private OnListClickInteractionListenerOptionsList mOnListOptionListener;

    /**
     * Construtor
     */
    public AtividadeListAdpter(List<Atividade> atividades, OnListClickInteractionListener listener, OnListClickInteractionListenerOptionsList listOptions) {
        this.mListAtividades = atividades;
        this.mOnListClickInteractionListener = listener;
        this.mOnListOptionListener = listOptions;
    }

    /**
     * Responsável pela criação de linha
     */
    @Override
    public AtividadeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View atividadeView = inflater.inflate(R.layout.activity_row_atividade_list_add, parent, false);

        // Passa a ViewHolder
        return new AtividadeViewHolder(atividadeView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(AtividadeViewHolder holder, int position) {
        Atividade car = this.mListAtividades.get(position);
        holder.bindData(car, this.mOnListClickInteractionListener, this.mOnListOptionListener);
    }

    @Override
    public int getItemCount() {
        return this.mListAtividades.size();
    }
}