package com.fiquedeolho.nfcatividadeapp.listAtividades.recyclerView;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;

import java.util.List;

public class AtividadeListAdpter extends RecyclerView.Adapter<AtividadeViewHolder> {

    // Lista de carros
    private List<Atividade> mListCars;

    // Interface que define as ações
    private OnListClickInteractionListener mOnListClickInteractionListener;

    /**
     * Construtor
     */
    public AtividadeListAdpter(List<Atividade> cars, OnListClickInteractionListener listener) {
        this.mListCars = cars;
        this.mOnListClickInteractionListener = listener;
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
        View carView = inflater.inflate(R.layout.activity_row_atividade_list, parent, false);

        // Passa a ViewHolder
        return new AtividadeViewHolder(carView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(AtividadeViewHolder holder, int position) {
        Atividade car = this.mListCars.get(position);
        holder.bindData(car, this.mOnListClickInteractionListener);
    }

    @Override
    public int getItemCount() {
        return this.mListCars.size();
    }
}
