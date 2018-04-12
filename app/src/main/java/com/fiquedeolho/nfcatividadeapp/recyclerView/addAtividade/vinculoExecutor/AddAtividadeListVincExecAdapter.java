package com.fiquedeolho.nfcatividadeapp.recyclerView.addAtividade.vinculoExecutor;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Usuario;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;

import java.util.List;

public class AddAtividadeListVincExecAdapter extends RecyclerView.Adapter<AddAtividadeListVincExecViewHolder> {

    private List<Usuario> mListUsuario;

    // Interface que define as ações
    private OnListClickInteractionListener mOnListClickInteractionListener;

    public AddAtividadeListVincExecAdapter(List<Usuario> usuarios, OnListClickInteractionListener listener){
        this.mListUsuario = usuarios;
        this.mOnListClickInteractionListener = listener;
    }

    /**
     * Responsável pela criação de linha
     */
    @Override
    public AddAtividadeListVincExecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View tarefaView = inflater.inflate(R.layout.activity_row_add_ativ_vinc_exec, parent, false);

        // Passa a ViewHolder
        return new AddAtividadeListVincExecViewHolder(tarefaView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(AddAtividadeListVincExecViewHolder holder, int position) {
        Usuario usuario = this.mListUsuario.get(position);
        holder.bindData(usuario, this.mOnListClickInteractionListener);
    }

    @Override
    public int getItemCount() {
        return this.mListUsuario.size();
    }
}
