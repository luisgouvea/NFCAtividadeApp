package com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.addAtividade;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Atividade;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListenerView;

import java.util.List;

public class AtividadeListAdpter extends RecyclerView.Adapter<AtividadeViewHolder> {

    // Lista de atividades
    private List<Atividade> mListAtividades;

    private OnListClickInteractionListenerView mOnListOptionListener;

    private AtividadeViewHolder.ClickListener mListenerFiltroPesquisa;

    /**
     * Construtor
     */
    public AtividadeListAdpter(List<Atividade> atividades, OnListClickInteractionListenerView listOptions, AtividadeViewHolder.ClickListener listFiltroPesquisa) {
        this.mListAtividades = atividades;
        this.mOnListOptionListener = listOptions;
        this.mListenerFiltroPesquisa = listFiltroPesquisa;
    }

    @Override
    public int getItemViewType (int position){
        return position;
    }
    /**
     * Responsável pela criação de linha
     */
    @Override
    public AtividadeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View atividadeView = null;
        if(viewType == 0){
            atividadeView = inflater.inflate(R.layout.activity_header_filtro_pesquisa_home, parent, false);
        }
        else {
            // Instancia o layout para manipulação dos elementos
            atividadeView = inflater.inflate(R.layout.activity_row_atividade_list_add, parent, false);
        }

        // Passa a ViewHolder
        return new AtividadeViewHolder(atividadeView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(AtividadeViewHolder holder, int position) {
        Atividade car = this.mListAtividades.get(position);
        holder.bindData(car, this.mOnListOptionListener, mListenerFiltroPesquisa, position);
    }

    @Override
    public int getItemCount() {
        return this.mListAtividades.size();
    }
}
