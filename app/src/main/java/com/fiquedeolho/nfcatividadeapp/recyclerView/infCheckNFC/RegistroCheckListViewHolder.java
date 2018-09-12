package com.fiquedeolho.nfcatividadeapp.recyclerView.infCheckNFC;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiquedeolho.nfcatividadeapp.R;
import com.fiquedeolho.nfcatividadeapp.models.TarefaCheck;
import com.fiquedeolho.nfcatividadeapp.util.Convert;

public class RegistroCheckListViewHolder extends RecyclerView.ViewHolder{

    // Elemento de interface
    private TextView nomeTarefa;
    private TextView textStatusExecucaoCheck;
    private TextView textDataExecucaoCheck;
    private ImageView imageStatusCheckValido;
    private ImageView imageStatusCheckInvalido;
    private TextView textCiclo;

    /**
     * Construtor
     */
    public RegistroCheckListViewHolder(View itemView) {
        super(itemView);
        this.nomeTarefa = itemView.findViewById(R.id.text_nome_tarefa_check);
        this.textStatusExecucaoCheck = itemView.findViewById(R.id.text_status_check_nfc);
        this.imageStatusCheckValido = itemView.findViewById(R.id.image_check_valido);
        this.imageStatusCheckInvalido = itemView.findViewById(R.id.image_check_invalido);
        this.textDataExecucaoCheck = itemView.findViewById(R.id.text_data_check_nfc);
        this.textCiclo = itemView.findViewById(R.id.text_ciclo_check_nfc);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(TarefaCheck ativTarefaHistoricoCheck) {

        // Altera valor
        this.nomeTarefa.setText(ativTarefaHistoricoCheck.getNome());
        if(ativTarefaHistoricoCheck.getIdStatusCheckNFC() == 1){ //  CHECK INVALIDO
            this.textStatusExecucaoCheck.setText("Check inválido");
            this.imageStatusCheckInvalido.setVisibility(View.VISIBLE);
        }else{
            this.textStatusExecucaoCheck.setText("Check válido");
            this.imageStatusCheckValido.setVisibility(View.VISIBLE);
        }
        this.textDataExecucaoCheck.setText(Convert.formatDate(ativTarefaHistoricoCheck.getDataExecucao()));

        //this.textCiclo.setText("Ciclo " + ativTarefaHistoricoCheck.getCiclo());
        int ciclo = ativTarefaHistoricoCheck.getCiclo()+ 1;
        this.textCiclo.setText( ciclo + "º Ciclo");
    }
}
