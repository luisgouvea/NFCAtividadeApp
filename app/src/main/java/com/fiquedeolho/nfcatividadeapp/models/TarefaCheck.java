package com.fiquedeolho.nfcatividadeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TarefaCheck extends Tarefa {

    private Date DataExecucao;

    public TarefaCheck(Parcel in) {
        super();
    }

    public TarefaCheck(){
        super();
    }

    public static final Parcelable.Creator<TarefaCheck> CREATOR = new Parcelable.Creator<TarefaCheck>() {
        @Override
        public TarefaCheck createFromParcel(Parcel in) {
            return new TarefaCheck(in);
        }

        @Override
        public TarefaCheck[] newArray(int size) {
            return new TarefaCheck[size];
        }
    };

    public Date getDataExecucao() {
        return DataExecucao;
    }

    public void setDataExecucao(Date dataExecucao) {
        DataExecucao = dataExecucao;
    }
}
