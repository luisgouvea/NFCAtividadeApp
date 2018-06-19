package com.fiquedeolho.nfcatividadeapp.recyclerView.menuHome.executarAtividade;


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

public class AtividadeExecutorListAdpter extends RecyclerView.Adapter<AtividadeExecutorViewHolder> {

    // Lista de atividades
    private List<Atividade> mListAtividades;

    private OnListClickInteractionListenerView mOnListOptionListener;

    private AtividadeExecutorViewHolder.ClickListener mListenerFiltroPesquisa;
    /**
     * Construtor
     */
    public AtividadeExecutorListAdpter(List<Atividade> atividades, OnListClickInteractionListenerView listOptions, AtividadeExecutorViewHolder.ClickListener listFiltroPesquisa) {
        this.mListAtividades = atividades;
        this.mOnListOptionListener = listOptions;
        this.mListenerFiltroPesquisa = listFiltroPesquisa;
    }

    /**
     * Responsável pela criação de linha
     */
    @Override
    public AtividadeExecutorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View atividadeView = null;
        if(viewType == 0){
            atividadeView = inflater.inflate(R.layout.activity_header_filtro_pesquisa_home, parent, false);
        }
        else {
            // Instancia o layout para manipulação dos elementos
            atividadeView = inflater.inflate(R.layout.activity_row_atividade_list_exec, parent, false);
        }

        // Passa a ViewHolder
        return new AtividadeExecutorViewHolder(atividadeView);
    }
    @Override
    public int getItemViewType (int position){
        return position;
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(AtividadeExecutorViewHolder holder, int position) {
        Atividade car = this.mListAtividades.get(position);
        holder.bindData(car, this.mOnListOptionListener, this.mListenerFiltroPesquisa, position);
    }

    @Override
    public int getItemCount() {
        return this.mListAtividades.size();
    }
}
