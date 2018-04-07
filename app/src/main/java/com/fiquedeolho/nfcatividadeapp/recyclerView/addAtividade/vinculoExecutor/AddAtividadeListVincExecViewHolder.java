package com.fiquedeolho.nfcatividadeapp.recyclerView.addAtividade.vinculoExecutor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.Usuario;
import com.fiquedeolho.nfcatividadeapp.recyclerView.OnListClickInteractionListener;

public class AddAtividadeListVincExecViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView nomeUsuario;
    private final RadioButton radioButton;

    /**
     * Construtor
     */
    public AddAtividadeListVincExecViewHolder(View itemView) {
        super(itemView);
        this.nomeUsuario = (TextView) itemView.findViewById(R.id.nome_usuario);
        this.radioButton = (RadioButton) itemView.findViewById(R.id.radioButton_usuario);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Usuario usuario , final OnListClickInteractionListener listener ) {

        // Altera valor
        this.nomeUsuario.setText(usuario.getNome());

        this.radioButton.setId(usuario.getId());

        // Adciona evento de click
        this.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Metodo (onClick) da interface OnListClickInteractionListener,  implementada nesse projeto
                 */
                radioButton.setChecked(true);
                listener.onClick(usuario.getId());
            }
        });

    }
}
