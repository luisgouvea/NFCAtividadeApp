package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TarefaHistoricoCheck extends Tarefa {

    private Date DataExecucao;

    public TarefaHistoricoCheck(Parcel in) {
        super();
    }

    public TarefaHistoricoCheck(){
        super();
    }

    public static final Parcelable.Creator<TarefaHistoricoCheck> CREATOR = new Parcelable.Creator<TarefaHistoricoCheck>() {
        @Override
        public TarefaHistoricoCheck createFromParcel(Parcel in) {
            return new TarefaHistoricoCheck(in);
        }

        @Override
        public TarefaHistoricoCheck[] newArray(int size) {
            return new TarefaHistoricoCheck[size];
        }
    };

    public Date getDataExecucao() {
        return DataExecucao;
    }

    public void setDataExecucao(Date dataExecucao) {
        DataExecucao = dataExecucao;
    }
}
